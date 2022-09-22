package com.neonusa.submission1.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.neonusa.submission1.core.data.repository.AppRepository

class MapsViewModel(private val repo: AppRepository):ViewModel() {
    fun storiesLocations() = repo.getStoriesLocations().asLiveData()
}