package com.neonusa.submission1.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.neonusa.submission1.core.data.repository.AppRepository
import com.neonusa.submission1.core.data.source.model.Story

class HomeViewModel(private val repo: AppRepository): ViewModel() {
    val paginatedStories: LiveData<PagingData<Story>> =
        repo.getPaginatedStories().cachedIn(viewModelScope)
}