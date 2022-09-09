package com.neonusa.submission1.core.data.source.remote.response

import com.neonusa.submission1.core.data.source.model.User

data class LoginResponse (
    val message: String,
    val loginResult: User
)