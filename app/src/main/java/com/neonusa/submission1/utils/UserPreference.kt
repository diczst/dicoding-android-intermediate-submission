package com.neonusa.submission1.utils

import com.chibatching.kotpref.KotprefModel
import com.google.gson.Gson
import com.neonusa.submission1.core.data.source.model.User

object UserPreference: KotprefModel() {
    var isLogin by booleanPref(false)
    var token by stringPref()

    fun getUserToken(): String?{
        if(token.isEmpty()) return null
        return token
    }

//    fun setUserToken(token: String){
//        this.token = token
//    }
//
//    fun getUserToken(): String? {
//        if(token.isEmpty()) return null
//        return token
//    }
}