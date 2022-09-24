package com.neonusa.submission1.utils

import com.neonusa.submission1.core.data.source.model.Story
import com.neonusa.submission1.core.data.source.remote.network.ApiService
import com.neonusa.submission1.core.data.source.remote.request.LoginRequest
import com.neonusa.submission1.core.data.source.remote.request.RegisterRequest
import com.neonusa.submission1.core.data.source.remote.response.BaseListResponse
import com.neonusa.submission1.core.data.source.remote.response.BasicResponse
import com.neonusa.submission1.core.data.source.remote.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class ServiceDummy: ApiService {
    override suspend fun register(data: RegisterRequest): Response<BasicResponse> =
        Response.success(DataDummy.generateRegisterResponse())

    override suspend fun login(data: LoginRequest): Response<LoginResponse> =
        Response.success(DataDummy.generateLoginResponse())

    override suspend fun getStoriesLocations(): Response<BaseListResponse<Story>> =
        Response.success(DataDummy.generateStoriesLocationResponse())

    override suspend fun createStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): Response<BasicResponse> =
        Response.success(DataDummy.generateAddResponse())


    override suspend fun getPaginatedStories(
        page: Int,
        size: Int
    ): Response<BaseListResponse<Story>> =
        //todo: ubah
        Response.success(DataDummy.generateStoriesLocationResponse())


    // ERRORS
    fun loginError(): BasicResponse =
        DataDummy.generateLoginError()

    fun registerError(): BasicResponse =
        DataDummy.generateRegisterError()

    fun uploadError(): BasicResponse =
        DataDummy.generateAddError()
}