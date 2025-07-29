package com.nuun.track.core.configs.state

import androidx.compose.runtime.Composable

sealed class ViewState<out R> {
    object Loading : ViewState<Nothing>()
    object Empty : ViewState<Nothing>()
    data class Success<out T>(val data: T) : ViewState<T>()
    data class Error(val exception: Exception) : ViewState<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Empty -> "Empty"
            is Loading -> "Loading"
            is Error -> "Error[exception=$exception]"
        }
    }

    fun onSuccessData(
        block: (R) -> Unit
    ): ViewState<R> {
        if(this is Success) block.invoke(this.data)
        return this
    }

    @Composable
    fun onSuccess(
        block: @Composable (R) -> Unit
    ): ViewState<R> {
        if(this is Success) block.invoke(this.data)
        return this
    }

    @Composable
    fun onError(
        block: @Composable (Exception) -> Unit
    ): ViewState<R> {
        if(this is Error) block.invoke(this.exception)
        return this
    }

    @Composable
    fun onEmpty(
        block: @Composable () -> Unit
    ): ViewState<R> {
        if (this is Empty) block.invoke()
        return this
    }

    @Composable
    fun onLoading(
        block: @Composable () -> Unit
    ): ViewState<R> {
        if (this is Loading) block.invoke()
        return this
    }

    fun isSuccess(): Boolean = this is Success
}