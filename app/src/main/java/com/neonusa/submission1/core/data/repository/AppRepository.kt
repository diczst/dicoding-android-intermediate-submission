package com.neonusa.submission1.core.data.repository

import android.util.Log
import com.neonusa.submission1.core.data.source.remote.RemoteDataSource
import com.neonusa.submission1.core.data.source.remote.network.Resource
import com.neonusa.submission1.core.data.source.remote.request.LoginRequest
import com.neonusa.submission1.core.data.source.remote.request.RegisterRequest
import com.neonusa.submission1.utils.UserPreference
import kotlinx.coroutines.flow.flow

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
                    emit(Resource.error(body?.message ?: "Default error dongs", null))
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
                    emit(Resource.success(body?.loginResult))
                    Log.i("TAG", "token : ${body?.loginResult?.token.toString()}: ")

                } else {
                    emit(Resource.error(body?.message ?: "Default error dongs", null))

                    // dalam kasus lain apabila error response berbeda
//                    emit(Resource.error( it.getErrorBody(ErrorCustom::class.java)?
//                                             .namaAtribut ?: "Error Default", null))
                    Log.e("Login Error : ", it.message())
                }
            }
        } catch (e: Exception) {
            emit(Resource.error(e.message ?: "Terjadi Kesalahan", null))
            Log.e("Login Error : ", e.message ?: "Terjadi Kesalahan")
        }
    }

}