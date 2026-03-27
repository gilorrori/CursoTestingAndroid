package com.gilorroristore.cursotestingandroid.cart.presentation

sealed class CartEvent {
    data class ShowMessage(val message: String) : CartEvent()
}