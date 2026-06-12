package com.mevi.lasheslam.data

import com.mevi.lasheslam.core.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class BaseRepository(@IoDispatcher protected val dispatcher: CoroutineDispatcher) {

    protected suspend inline fun <T> io(crossinline block: suspend () -> T): T =
        withContext(dispatcher) { block() }
}