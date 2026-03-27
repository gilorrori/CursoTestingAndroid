package com.gilorroristore.cursotestingandroid.core.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.gilorroristore.cursotestingandroid.cart.presentation.CartScreen
import com.gilorroristore.cursotestingandroid.detail.presentation.DetailScreen
import com.gilorroristore.cursotestingandroid.productlist.presentation.ProductListScreen
import com.gilorroristore.cursotestingandroid.settings.presentation.SettingsScreen

@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    // La pantalla que iniciara primero
    val backStack = rememberNavBackStack(Screen.ProductList)
    val entries = entryProvider<NavKey> {

        entry<Screen.ProductList> {
            ProductListScreen(
                navToSettings = {
                    backStack.add(Screen.Settings)
                }, navToProductDetail = { productId: String ->
                    backStack.add(Screen.ProductDetail(productId = productId))
                }, navToCart = {
                    backStack.add(Screen.Cart)
                }
            )
        }

        entry<Screen.Settings> {
            SettingsScreen(
                onBack = { backStack.remove(it) }
            )
        }

        entry<Screen.Cart> {
            CartScreen(onBack = {
                backStack.removeLastOrNull()
            })
        }

        entry<Screen.ProductDetail> {
            DetailScreen(
                productId = it.productId,
                onBack = { backStack.remove(it) }
            )
        }
    }

    //Cargando las entries en un composable
    NavDisplay(
        backStack = backStack,
        entryProvider = entries,
        onBack = { backStack.removeLastOrNull() },
        transitionSpec = {
            slideInHorizontally { it } + fadeIn() togetherWith
                    slideOutHorizontally { -it } + fadeOut()
        },
        popTransitionSpec = {
            slideInHorizontally { -it } + fadeIn() togetherWith
                    slideOutHorizontally { it } + fadeOut()
        }
    )
}