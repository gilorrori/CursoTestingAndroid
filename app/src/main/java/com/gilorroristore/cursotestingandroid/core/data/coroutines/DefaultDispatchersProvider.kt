package com.gilorroristore.cursotestingandroid.core.data.coroutines

import com.gilorroristore.cursotestingandroid.core.domain.coroutines.DispatchersProviders
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DefaultDispatchersProvider @Inject constructor() : DispatchersProviders {
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val default: CoroutineDispatcher = Dispatchers.Default
}