package com.gilorroristore.cursotestingandroid.core.presentation.navigation

import androidx.compose.animation.core.tween
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
import com.gilorroristore.cursotestingandroid.core.presentation.ex.back
import com.gilorroristore.cursotestingandroid.core.presentation.ex.navigateTo
import com.gilorroristore.cursotestingandroid.detail.presentation.DetailScreen
import com.gilorroristore.cursotestingandroid.productlist.presentation.ProductListScreen
import com.gilorroristore.cursotestingandroid.settings.presentation.SettingsScreen

@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    // La pantalla que iniciara primero
    val backStack = rememberNavBackStack(Screen.ProductList)
    val TRANSITION_DURATION = 350

    val entries = entryProvider<NavKey> {

        entry<Screen.ProductList> {
            ProductListScreen(
                navToSettings = {
                    backStack.navigateTo(Screen.Settings)
                }, navToProductDetail = { productId: String ->
                    backStack.navigateTo(Screen.ProductDetail(productId = productId))
                }, navToCart = {
                    backStack.navigateTo(Screen.Cart)
                }
            )
        }

        entry<Screen.Settings> {
            SettingsScreen(
                onBack = { backStack.back() }
            )
        }

        entry<Screen.Cart> {
            CartScreen(onBack = {
                backStack.back()
            })
        }

        entry<Screen.ProductDetail> {
            DetailScreen(
                productId = it.productId,
                onBack = { backStack.back() }
            )
        }
    }

    //Cargando las entries en un composable
    NavDisplay(
        backStack = backStack,
        entryProvider = entries,
        onBack = { backStack.back() },
        // Animación de entrada
        transitionSpec = {
            slideInHorizontally(
                // donde inicia al hacer esto se empieza desde fuera para que entre la nueva screen
                animationSpec = tween(TRANSITION_DURATION),
                initialOffsetX = { it }
            ) togetherWith slideOutHorizontally(
                animationSpec = tween(TRANSITION_DURATION),
                targetOffsetX = { -it }
            )
        },
        popTransitionSpec = {
            slideInHorizontally(
                animationSpec = tween(TRANSITION_DURATION),
                initialOffsetX = { -it }
            ) togetherWith slideOutHorizontally(
                animationSpec = tween(TRANSITION_DURATION),
                targetOffsetX = { it }
            )
        },
        // Funciona a partir de Android 13 con el swipe back
        predictivePopTransitionSpec = {
            slideInHorizontally(
                animationSpec = tween(TRANSITION_DURATION),
                initialOffsetX = { -it }
            ) togetherWith slideOutHorizontally(
                animationSpec = tween(TRANSITION_DURATION),
                targetOffsetX = { it }
            )
        }
    )
}