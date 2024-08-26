package com.machine.machine.repository

import com.machine.machine.model.common.ApiResult
import com.machine.machine.network.RequestBodies

interface RemoteDataSource {
    fun loginUser(body: RequestBodies.LoginBody, callback: (ApiResult<Any>) -> Unit)
    fun getPictures(callback: (ApiResult<Any>) -> Unit)
}