package com.nuun.track.core.configs.state

sealed class ResultState<out R> {

    data class Success<out T>(val data: T, private val timeStamp: Long = System.currentTimeMillis()) : ResultState<T>()
    data class Error(val exception: Exception, private val timeStamp: Long = System.currentTimeMillis()) : ResultState<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }

    fun onSuccess(
        block: (R) -> Unit
    ): ResultState<R> {
        if (this is Success) block.invoke(this.data)
        return this
    }

    suspend fun onSuspendSuccess(
        block: suspend (R) -> Unit
    ): ResultState<R> {
        if (this is Success) block.invoke(this.data)
        return this
    }

    fun onError(
        block: (Exception) -> Unit
    ): ResultState<R> {
        if (this is Error) block.invoke(this.exception)
        return this
    }

    suspend fun onSuspendError(
        block: suspend (Exception) -> Unit
    ): ResultState<R> {
        if (this is Error) block.invoke(this.exception)
        return this
    }

    fun isSuccess(): Boolean {
        return this is Success
    }
}

fun <T> ResultState<T>.getDataOrNull(): T? = if (this is ResultState.Success) this.data else null

inline fun <T, R> ResultState<T>.map(transform: (T) -> R): ResultState<R> {
    return when (this) {
        is ResultState.Success -> {
            ResultState.Success(transform.invoke(this.data))
        }
        is ResultState.Error -> {
            ResultState.Error(this.exception)
        }
    }
}