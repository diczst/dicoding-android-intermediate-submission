package com.neonusa.submission1.core.data.source.remote.network

import com.neonusa.submission1.core.data.source.model.Story
import com.neonusa.submission1.core.data.source.remote.request.LoginRequest
import com.neonusa.submission1.core.data.source.remote.request.RegisterRequest
import com.neonusa.submission1.core.data.source.remote.response.BaseListResponse
import com.neonusa.submission1.core.data.source.remote.response.LoginResponse
import com.neonusa.submission1.core.data.source.remote.response.BasicResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("register")
    suspend fun register(
        @Body data: RegisterRequest
    ): Response<BasicResponse>

    @POST("login")
    suspend fun login(
        @Body data: LoginRequest
    ): Response<LoginResponse>

    @GET("stories")
    suspend fun getStories(
    ): Response<BaseListResponse<Story>>

    @GET("stories?location=1")
    suspend fun getStoriesLocations(
    ): Response<BaseListResponse<Story>>

    @Multipart
    @POST("stories")
    suspend fun createStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): Response<BasicResponse>

    @GET("stories")
    suspend fun getPaginatedStories(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<BaseListResponse<Story>>

}