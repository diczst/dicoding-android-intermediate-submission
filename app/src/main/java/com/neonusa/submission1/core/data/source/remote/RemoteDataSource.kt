package com.neonusa.submission1.core.data.source.remote

import com.neonusa.submission1.core.data.source.remote.network.ApiService
import com.neonusa.submission1.core.data.source.remote.request.LoginRequest
import com.neonusa.submission1.core.data.source.remote.request.RegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.core.context.GlobalContext.get

class RemoteDataSource(private val apiService: ApiService) {

    suspend fun register(data: RegisterRequest) = apiService.register(data)
    suspend fun login(data: LoginRequest) = apiService.login(data)
    suspend fun getStories() = apiService.getStories()
    suspend fun getStoriesLocations() = apiService.getStoriesLocations()

    suspend fun getPaginatedStories(page: Int, size: Int) = apiService.getPaginatedStories(page,size)

    suspend fun createStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ) = apiService.createStory(photo,description,lat,lon)
}