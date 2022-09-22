package com.neonusa.submission1.core.data.source.remote.network

import com.google.gson.GsonBuilder
import com.neonusa.submission1.utils.TokenInterceptor
import com.neonusa.submission1.utils.UserPreference
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {

    private const val BASE_URL = "https://story-api.dicoding.dev/v1/"

    private val client: Retrofit
        get() {
            val gson = GsonBuilder()
                .setLenient()
                .create()

//             without bearer token
//             val interceptor = HttpLoggingInterceptor()
//             interceptor.level = HttpLoggingInterceptor.Level.BODY

            // | --------------------------------------------------------------------------
            // | Catatan
            // | --------------------------------------------------------------------------
            // | interceptor ini tidak work saat aplikasi pertama kali diinstal
            // | sebab saat itu TokenInterceptor(null), bahkan walaupun sudah login
            // | karena objek retrofit yang digunakan masih sama saat sebelum ada token
            // | __________________________________________________________________________

//            val interceptor = TokenInterceptor(UserPreference.getUserToken().toString())

            val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        }

    val provideApiService: ApiService
        get() = client.create(ApiService::class.java)


}