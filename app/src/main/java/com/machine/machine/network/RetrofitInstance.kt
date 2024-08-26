package com.machine.machine.network

import com.machine.machine.BuildConfig
import com.machine.machine.constant.ApiConst
import com.machine.machine.util.lazyNullCacheable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


public class RetrofitInstance {

    companion object {
        var authApi: API? by lazyNullCacheable {
            retrofitAuth().create(API::class.java)
        }

        var otherClientApi: API? by lazyNullCacheable {
            retrofitOther().create(API::class.java)
        }
        var ClientApi: API? by lazyNullCacheable {
            retrofitClient().create(API::class.java)
        }
        var mapClientApi: API? by lazyNullCacheable {
            retrofitClientForMap().create(API::class.java)
        }


        fun clean() {
            authApi = null
            ClientApi = null
            otherClientApi = null
            mapClientApi = null
        }

        private fun retrofitAuth(): Retrofit {
            val httpClient = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                httpClient.addInterceptor(logging)
            }
            httpClient.addInterceptor(AuthHeaderInterceptor())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
            return Retrofit.Builder()
                .baseUrl(ApiConst.BASE_AUTH)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
        }

        private fun retrofitClient(): Retrofit {
            val httpClient = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                httpClient.addInterceptor(logging)
            }
            httpClient.addInterceptor(HeaderInterceptor())
                .addInterceptor(UnauthorizedInterceptor())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
            return Retrofit.Builder()
                .baseUrl(ApiConst.BASE_AUTH)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
        }

        private fun retrofitClientForMap(): Retrofit {
            val httpClient = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                httpClient.addInterceptor(logging)
            }
            httpClient.addInterceptor(HeaderInterceptor())
                .addInterceptor(UnauthorizedInterceptor())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
            return Retrofit.Builder()
                .baseUrl(ApiConst.BASE_MAP_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
        }

        private fun retrofitOther(): Retrofit {
            val httpClient = OkHttpClient.Builder()
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
                httpClient.addInterceptor(logging)
            }
            httpClient.addInterceptor(HeaderInterceptor())
                .addInterceptor(UnauthorizedInterceptor())
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
            return Retrofit.Builder()
                .baseUrl(ApiConst.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
        }

    }
}