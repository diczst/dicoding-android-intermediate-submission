package com.neonusa.submission1.core.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.neonusa.submission1.core.data.paging.StoryPagingSource
import com.neonusa.submission1.core.data.source.model.Story
import com.neonusa.submission1.core.data.source.remote.RemoteDataSource
import com.neonusa.submission1.core.data.source.remote.network.Resource
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
                } else {
                    emit(Resource.error(it.getErrorBody(ErrorCustom::class.java)?.message ?: "Unknown Error", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Error", null))
        }
    }

    fun login(data: LoginRequest) = flow {
        emit(Resource.loading(null))
        try {
            remoteDataSource.login(data).let {
                val body = it.body()
                val user = body?.loginResult
                if (it.isSuccessful) {
                    UserPreference.isLogin = true
                    UserPreference.setUserToken(user?.token.toString())
                    emit(Resource.success(user))
                } else {
                    emit(Resource.error(it.getErrorBody(ErrorCustom::class.java)?.message ?: "Unknown Error", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Error", null))
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
                } else {
                    emit(Resource.error(it.getErrorBody()?.message ?: "Response Fail", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Error", null))
        }
    }

    fun getStoriesLocations() = flow {
        emit(Resource.loading(null))
        try {
            remoteDataSource.getStoriesLocations().let {
                if (it.isSuccessful) {
                    val body = it.body()
                    val listStory = body?.listStory
                    emit(Resource.success(listStory))
                } else {
                    emit(Resource.error(it.getErrorBody()?.message ?: "Response Fail", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Error", null))
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
                } else {
                    emit(Resource.error(it.getErrorBody()?.message ?: "Response Fail", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Error", null))
        }
    }

    fun getPaginatedStories(): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(remoteDataSource)
            }
        ).liveData
    }

    data class ErrorCustom(
        val error: Boolean,
        val message: String
    )
}
