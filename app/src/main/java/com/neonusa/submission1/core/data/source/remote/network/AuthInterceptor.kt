package com.neonusa.submission1.core.data.source.remote.network

import com.neonusa.submission1.utils.UserPreference
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        UserPreference.getUserToken().let {
            requestBuilder.addHeader("Authorization","Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}