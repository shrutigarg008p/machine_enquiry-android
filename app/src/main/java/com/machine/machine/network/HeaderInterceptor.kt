package com.machine.machine.network

import com.machine.machine.constant.FieldConst
import com.machine.machine.offline.SharedPref
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        request = request.newBuilder()
            .addHeader(FieldConst.ACCEPT, FieldConst.APPLICATION_JSON)
            .addHeader(FieldConst.AUTHORIZATION, SharedPref.getToken()!!)
            .addHeader(FieldConst.ACCEPT_LANGUAGE, Locale.getDefault().language)
            .build()
        return chain.proceed(request)
    }
}