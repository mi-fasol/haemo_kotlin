package com.example.haemo_kotlin.model.retrofit.user

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("studentId") val studentId: String,
    @SerializedName("pwd") val password: String
)
