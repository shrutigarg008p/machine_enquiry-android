package com.machine.machine.model.common

sealed class ApiResult<out T : Any?>

class Loading<T> : ApiResult<T>()

data class Success<out T : Any?>(val data: T) : ApiResult<T>()

data class ApiError(val exception: Exception) : ApiResult<Nothing>()