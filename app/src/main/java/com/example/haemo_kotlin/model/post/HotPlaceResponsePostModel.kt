package com.example.haemo_kotlin.model.post

import com.google.gson.annotations.SerializedName

data class HotPlaceResponsePostModel(
    @SerializedName("hpId") val hpId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("address") val address: String,
    @SerializedName("date") val date: String,
    @SerializedName("wishing") val wish: Int,
)