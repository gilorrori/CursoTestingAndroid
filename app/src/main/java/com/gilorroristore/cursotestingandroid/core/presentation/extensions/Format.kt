package com.gilorroristore.cursotestingandroid.core.presentation.extensions

import java.util.Locale

fun Double.toPriceAmount(): String{
    return String.format(Locale.getDefault(), "%.2f", this)
}