package com.neonusa.submission1.core.data.source.remote.response

data class BaseListResponse<T>(
    val error: String? = null,
    val message: String? = null,
    val listStory: List<T> = emptyList()
)