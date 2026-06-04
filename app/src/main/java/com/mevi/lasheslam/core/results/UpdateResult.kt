package com.mevi.lasheslam.core.results

sealed class UpdateResult {
    object Required : UpdateResult()
    object NotRequired : UpdateResult()
}