package com.neonusa.submission1.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.neonusa.submission1.core.data.repository.AppRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddViewModel(private val repo: AppRepository): ViewModel() {
    fun createStory(photo: MultipartBody.Part, description: RequestBody, lat: RequestBody, lon: RequestBody) = repo.addStory(photo,description,lat,lon).asLiveData()
}