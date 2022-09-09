package com.neonusa.submission1.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.neonusa.submission1.core.data.repository.AppRepository
import com.neonusa.submission1.core.data.source.remote.request.RegisterRequest

class RegisterViewModel(private val repo: AppRepository): ViewModel() {
    fun register(data: RegisterRequest) = repo.register(data).asLiveData()
}