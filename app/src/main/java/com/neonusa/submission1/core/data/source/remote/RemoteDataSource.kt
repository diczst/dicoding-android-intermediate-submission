package com.neonusa.submission1.core.data.source.remote

import com.neonusa.submission1.core.data.source.remote.network.ApiService
import com.neonusa.submission1.core.data.source.remote.request.LoginRequest
import com.neonusa.submission1.core.data.source.remote.request.RegisterRequest

class RemoteDataSource(private val apiService: ApiService) {
    suspend fun register(data: RegisterRequest) = apiService.register(data)
    suspend fun login(data: LoginRequest) = apiService.login(data)
}