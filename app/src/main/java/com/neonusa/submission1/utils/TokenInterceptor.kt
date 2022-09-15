package com.neonusa.submission1.utils

import okhttp3.Interceptor
import okhttp3.Request
import okio.IOException

class TokenInterceptor(val bearerToken: String) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {

        //rewrite the request to add bearer token
        val newRequest: Request = chain.request().newBuilder()
            .header("Authorization", "Bearer $bearerToken")
            .build()
        return chain.proceed(newRequest)
    }
}