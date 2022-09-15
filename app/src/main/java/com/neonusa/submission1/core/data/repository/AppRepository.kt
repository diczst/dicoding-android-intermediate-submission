package com.neonusa.submission1.core.data.repository

import android.util.Log
import com.neonusa.submission1.core.data.source.remote.RemoteDataSource
import com.neonusa.submission1.core.data.source.remote.network.Resource
import com.neonusa.submission1.core.data.source.remote.request.CreateRequest
import com.neonusa.submission1.core.data.source.remote.request.LoginRequest
import com.neonusa.submission1.core.data.source.remote.request.RegisterRequest
import com.neonusa.submission1.utils.UserPreference
import com.neonusa.submission1.utils.getErrorBody
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AppRepository(private val remoteDataSource: RemoteDataSource) {
    fun register(data: RegisterRequest) = flow {
        emit(Resource.loading(null))
        try {
            remoteDataSource.register(data).let {
                val body = it.body()
                if (it.isSuccessful) {
                    emit(Resource.success(body))
                    Log.i("AppRepository", "body: ${body.toString()}")
                } else {
                    emit(Resource.error(it.getErrorBody(ErrorCustom::class.java)?.message ?: "Unknown Error", null))
                    Log.i("TAG", "body.message: ${body?.toString()}")
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Terjadi Kesalahan", null))
        }
    }

    fun login(data: LoginRequest) = flow{
        emit(Resource.loading(null))
        try {
            remoteDataSource.login(data).let {
                val body = it.body()
                val user = body?.loginResult
                if (it.isSuccessful) {
                    UserPreference.isLogin = true
                    UserPreference.token = user?.token.toString()
                    emit(Resource.success(user))
                } else {
                    emit(Resource.error(it.getErrorBody(ErrorCustom::class.java)?.message ?: "Unknown Error", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Terjadi Kesalahan", null))
        }
    }

    fun getStories() = flow {
        emit(Resource.loading(null))
        try {
            remoteDataSource.getStories().let {
                if (it.isSuccessful) {
                    val body = it.body()
                    val listStory = body?.listStory

                    emit(Resource.success(listStory))
                    Log.i("TAG", "getUser: {${it.body()}}")
                } else {
                    emit(Resource.error(it.getErrorBody()?.message ?: "Default error dongs", null))
                    Log.i("TAG", "getUser: {${it.getErrorBody()?.message}}")

                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Terjadi Kesalahan", null))
        }
    }

    fun createUser(
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ) = flow {
        emit(Resource.loading(null))
        try {
            remoteDataSource.createStory(photo, description, lat,lon).let {
                if (it.isSuccessful) {
                    val body = it.body()
                    emit(Resource.success(body))
                    Log.i("AppRepository", "success: ${body?.message}")
                } else {
                    emit(Resource.error(it.getErrorBody()?.message ?: "Default error dongs", null))
                    Log.i("AppRepository", "failed: ${it.getErrorBody()?.message}")

                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Terjadi Kesalahan", null))
        }
    }

    data class ErrorCustom(
        val error: Boolean,
        val message: String
    )
}
