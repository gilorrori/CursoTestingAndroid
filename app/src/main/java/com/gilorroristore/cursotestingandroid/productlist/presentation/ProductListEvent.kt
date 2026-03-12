package com.gilorroristore.cursotestingandroid.productlist.presentation

/**
 * Se crea una clase para mensajes para que no se dependa del estado de la UI, ya que si se requiere
 * que el mensaje solo aparezca una vez, por ejemplo al girar la pantalla se mostraria n veces
 * y con esta clase se previene esto.
 */
sealed interface ProductListEvent {
    data class ShowMessage(val message: String): ProductListEvent
}