package com.machine.machine.network

import com.machine.machine.constant.FieldConst
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class AuthHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader(FieldConst.ACCEPT, FieldConst.APPLICATION_JSON)
            .addHeader(FieldConst.ACCEPT_LANGUAGE, Locale.getDefault().language)
            .build()
        return chain.proceed(request)
    }
}