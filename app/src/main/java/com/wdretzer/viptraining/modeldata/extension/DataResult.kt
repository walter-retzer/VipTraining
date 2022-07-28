package com.wdretzer.viptraining.modeldata.extension

sealed class DataResult<out T: Any> {
    data class Success <out T: Any> (val dataResult: T): DataResult<T>()
    data class Error(val error: Throwable) : DataResult<Nothing>()
    data class Loading(val isLoading: Boolean) : DataResult<Nothing>()
    data class Warning(val message: String): DataResult<Nothing>()
    object Empty: DataResult<Nothing>()
}
