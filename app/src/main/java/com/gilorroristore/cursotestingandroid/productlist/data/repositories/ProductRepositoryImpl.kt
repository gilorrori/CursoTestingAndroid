package com.gilorroristore.cursotestingandroid.productlist.data.repositories

import com.gilorroristore.cursotestingandroid.core.domain.coroutines.DispatchersProviders
import com.gilorroristore.cursotestingandroid.productlist.data.local.LocalDataSource
import com.gilorroristore.cursotestingandroid.productlist.data.local.database.entities.ProductEntity
import com.gilorroristore.cursotestingandroid.productlist.data.mappers.toDomain
import com.gilorroristore.cursotestingandroid.productlist.data.mappers.toEntity
import com.gilorroristore.cursotestingandroid.productlist.data.remote.RemoteDataSource
import com.gilorroristore.cursotestingandroid.productlist.domain.models.Product
import com.gilorroristore.cursotestingandroid.productlist.domain.repositories.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    val remoteDataSource: RemoteDataSource,
    val localDataSource: LocalDataSource,
    val dispatchers: DispatchersProviders
) : ProductRepository {

    /* funciona para ejecutar una corrutina fuera de toda la funcionalidad del localdatasource, para que
    * si algo falla, esto sea independiente
    * Con SupervisorJob() ->  Si una coroutine falla → las demás continúan  */
    private val refreshScope = CoroutineScope(SupervisorJob() + dispatchers.io)

    /* Bloque la corrutina hasta que se termine aunque se suscriban varias veces a esta funcion, para
    * que no se hagan multiples llamadas al entrar a la pantalla donde se consume el ws*/
    private val refreshMutex = Mutex()

    override fun getProducts(): Flow<List<Product>> {
        return localDataSource.getAllProducts()
            /* Lo que devuelve la bd es una lista de ProductEntity entonces hay que iterarla y luego sobre
            * cada elemento hay que iterar para mapper/transformar a un Product de Domain
            *
            *  Con mapNotNull solo llegaran modelos que se han filtrado en este caso que no sean null
            * */
            .map { productEntities -> productEntities.mapNotNull { it.toDomain() } }

            // Se ejecutará cada que alguien se suscriba a este flow, nos sirve para refrescar siempre a lo ultimo del ws
            .onStart {
                refreshScope.launch {
                    /*sino puede bloquarla es porque ya se esta haciendo esto sin haber terminado
                    * y se retorna sin ejecutar otra vez*/

                    if (!refreshMutex.tryLock()) return@launch

                    try {
                        refreshProduct()
                    } catch (e: Exception) {

                    } finally {
                        //Cuando termine sea por catch o terminar desbloquea el mutex de la corrutina
                        refreshMutex.unlock()
                    }
                }
            }
            .catch {
                // Error desde la funcion getallProducts
            }
    }

    override fun getProductById(id: String): Flow<Product?> {
        val productEntity: Flow<ProductEntity?> = localDataSource.getProductById(id)
        return productEntity.map { it?.toDomain() }.catch { e ->
            //Analitycs para saber si crash room
        }
    }

    // Obtiene datos de internet por eso es un suspend fun
    override suspend fun refreshProduct() {
        withContext(dispatchers.io) {
            //getOrThrow ya que interesa que se obtenga o que truene para controlar con un try-catch
            val products = remoteDataSource.getProducts().getOrThrow()

            // Mapeando lo que nos llega del WS a la bd
            val productsEntity = products.map { it.toEntity() }
            localDataSource.saveProducts(productsEntity)

        }
    }
}