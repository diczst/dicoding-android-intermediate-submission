package com.neonusa.submission1.utils

import com.neonusa.submission1.core.data.source.model.Story
import com.neonusa.submission1.core.data.source.model.User
import com.neonusa.submission1.core.data.source.remote.response.BaseListResponse
import com.neonusa.submission1.core.data.source.remote.response.BasicResponse
import com.neonusa.submission1.core.data.source.remote.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

object DataDummy {

    fun generateStories(): List<Story> {
        val stories = arrayListOf<Story>()

        for (i in 0 until 10){
            val data = Story(
                id = "story-2mB5Btjyb2d1RiHC",
                name =  "JohanLie",
                description = "semangat submissionnya",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1663941710395_aB0QaC4I.jpg",
                lat = "2",
                lon = "2"
            )
            stories.add(data)
        }

        return stories
    }

    fun generateStoriesLocationResponse(): BaseListResponse<Story> {
        val error = "false"
        val message = "Stories fetched successfully"
        val stories = mutableListOf<Story>()

        for (i in 0 until 10){
            val data = Story(
                id = "story-2mB5Btjyb2d1RiHC",
                name =  "JohanLie",
                description = "semangat submissionnya",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1663941710395_aB0QaC4I.jpg",
                lat = "2",
                lon = "2"
            )
            stories.add(data)
        }
        return BaseListResponse(error, message, stories)
    }

    fun generateLoginResponse(): LoginResponse {
        val user = User(
            name = "Johan Liebert",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUxSUHU4enpoRzZEb3dsYUwiLCJpYXQiOjE2NjM5NDYwNzV9.95oC4yapTsF_HQqVv5yxRGX7bT00LSr7a9J9CX97-vc"
        )
        return LoginResponse("success", user)
    }

    fun generateAddResponse(): BasicResponse =
        BasicResponse(false, "success")

    fun generateRegisterResponse(): BasicResponse =
        BasicResponse(false, "success")

    fun generateMultipartFile(): MultipartBody.Part =
        MultipartBody.Part.create("text".toRequestBody())

    fun generateRequestBody(): RequestBody =
        "text".toRequestBody()

    // ERRORS
    fun generateLoginError(): BasicResponse =
        BasicResponse(true, "user not found")

    fun generateRegisterError(): BasicResponse =
        BasicResponse(true, "Email is already taken")

    fun generateAddError(): BasicResponse =
        BasicResponse(true, "Error")
}