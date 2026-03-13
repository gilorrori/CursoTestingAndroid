package com.gilorroristore.cursotestingandroid.core.domain.coroutines

import kotlinx.coroutines.CoroutineDispatcher

/* Clase necesaria para mas adelante poder hacer el test de las coroutines de una manera mas practica */
interface DispatchersProviders {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}