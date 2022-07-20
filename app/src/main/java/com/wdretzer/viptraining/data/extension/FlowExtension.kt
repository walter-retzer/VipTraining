package com.wdretzer.viptraining.data.extension

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.io.IOException


fun <T : Any> Flow<DataResult<T>>.updateStatus() = this
    .retryWhen { cause, attempt ->
        if (cause is IOException && attempt < 4) {
            delay(5000)
            true
        } else {
            false
        }
    }
    .catch { e -> emit(DataResult.Error(e)) }
    .onStart { emit(DataResult.Loading(true)) }
    .onCompletion { emit(DataResult.Loading(false)) }
