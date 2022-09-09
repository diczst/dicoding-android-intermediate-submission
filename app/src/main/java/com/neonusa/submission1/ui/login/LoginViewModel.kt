package com.neonusa.submission1.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.neonusa.submission1.core.data.repository.AppRepository
import com.neonusa.submission1.core.data.source.remote.request.LoginRequest

class LoginViewModel(private val repo: AppRepository): ViewModel() {
    fun login(data: LoginRequest) = repo.login(data).asLiveData()
}