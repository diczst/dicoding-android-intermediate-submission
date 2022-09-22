package com.neonusa.submission1.utils

import com.chibatching.kotpref.KotprefModel

object UserPreference: KotprefModel() {
    var isLogin by booleanPref(false)
    var token by stringPref()

    fun setUserToken(token: String){
        this.token = token
    }

    fun getUserToken(): String?{
        if(token.isEmpty()) return null
        return token
    }
}