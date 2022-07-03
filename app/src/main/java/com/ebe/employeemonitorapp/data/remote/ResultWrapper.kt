package com.ebe.employeemonitorapp.data.remote

sealed class ResultWrapper<out T> {
    data class Success<out R>(val value: R) : ResultWrapper<R>()
    data class Failure(
        val message: String?,
        val statusCode: Int?
    ) : ResultWrapper<Nothing>()

    class Loading<T> : ResultWrapper<T>()
}
