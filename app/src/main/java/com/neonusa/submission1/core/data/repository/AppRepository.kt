package com.neonusa.submission1.core.data.repository
import android.util.Log
import com.neonusa.submission1.core.data.source.remote.RemoteDataSource
import com.neonusa.submission1.core.data.source.remote.network.ApiConfig
import com.neonusa.submission1.core.data.source.remote.network.ApiService
import com.neonusa.submission1.core.data.source.remote.network.Resource
import com.neonusa.submission1.core.data.source.remote.request.LoginRequest
import com.neonusa.submission1.core.data.source.remote.request.RegisterRequest
import com.neonusa.submission1.utils.UserPreference
import com.neonusa.submission1.utils.getErrorBody
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.core.context.GlobalContext.get
import retrofit2.create

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

    fun login(data: LoginRequest) = flow{
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

    data class ErrorCustom(
        val error: Boolean,
        val message: String
    )
}
