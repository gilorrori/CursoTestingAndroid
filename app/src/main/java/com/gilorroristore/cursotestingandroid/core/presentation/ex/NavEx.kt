package com.gilorroristore.cursotestingandroid.core.presentation.ex

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.navigateTo(screen: NavKey) {
    add(screen)
}

fun NavBackStack<NavKey>.back() {
    // Comprobando si el navStack esta vacio (aunque nunca deberia estarlo)
    if (isEmpty()) return
    removeLastOrNull()
}

fun NavBackStack<NavKey>.backTo(targetScreen: NavKey) {
    if (isNotEmpty()) return

    /* Si la pantalla a la que quiero navegar NO esta en el NavBackStack */
    if (targetScreen !in this) return

    /* Recorrera hasta que se llegue a la pantalla que se quiere, removiendo el stack que no se necesita*/
    while (isNotEmpty() && last() != targetScreen) {
        removeLastOrNull()
    }
}