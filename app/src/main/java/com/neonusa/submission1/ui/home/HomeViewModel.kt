package com.neonusa.submission1.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.neonusa.submission1.core.data.repository.AppRepository

class HomeViewModel(private val repo: AppRepository): ViewModel() {
    fun stories() = repo.getStories().asLiveData()
}