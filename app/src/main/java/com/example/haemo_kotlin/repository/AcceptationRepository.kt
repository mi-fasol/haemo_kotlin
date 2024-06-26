package com.example.haemo_kotlin.repository

import android.util.Log
import com.example.haemo_kotlin.model.retrofit.acceptation.AcceptationModel
import com.example.haemo_kotlin.model.retrofit.acceptation.AcceptationResponseModel
import com.example.haemo_kotlin.model.retrofit.user.LoginModel
import com.example.haemo_kotlin.model.retrofit.user.MailModel
import com.example.haemo_kotlin.model.retrofit.user.UserModel
import com.example.haemo_kotlin.model.retrofit.user.UserResponseModel
import com.example.haemo_kotlin.network.RetrofitClient
import retrofit2.Response
import javax.inject.Inject

class AcceptationRepository @Inject constructor(private val retrofitClient: RetrofitClient) {

    suspend fun getJoinUserByPId(pId: Int): Response<List<AcceptationResponseModel>> {
        return retrofitClient.service.getJoinUserByPId(pId)
    }

    suspend fun getAttendeeListByPId(pId: Int): Response<List<UserResponseModel>> {
        return retrofitClient.service.getAttendeeListByPId(pId)
    }

    suspend fun registerAcceptRequest(accept: AcceptationModel): Response<AcceptationResponseModel> {
        return retrofitClient.service.registerAcceptRequest(accept)
    }

    suspend fun deleteAcceptRequest(uId: Int, pId: Int): Response<Boolean> {
        return retrofitClient.service.deleteAcceptRequest(uId, pId)
    }

    suspend fun allowUserToJoin(uId: Int, pId: Int): Response<Boolean> {
        return retrofitClient.service.allowUserToJoin(uId, pId)
    }
}