package com.machine.machine.network;

import androidx.annotation.NonNull;

import com.machine.machine.EventConstant.EventBus;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by Amit Rawat on 3/11/2022.
 */
class UnauthorizedInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (response.code() == 401) {
            EventBus.INSTANCE.tokenExpired();
        }
        else
        {

        }
        return response;
    }
}


