package com.app.ynvest_tube.repository

import android.widget.Toast
import com.app.ynvest_tube.model.*
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*


class Repository {

    companion object {
        private lateinit var apiRequestService: ApiRequestService
        private lateinit var userId: UUID
    }

    fun initialize(id: UUID) {
        apiRequestService = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(ApiRequestService::class.java)

        userId = id
    }

    fun initializeWithUserRegistration(successCallback: (UUID) -> Unit, failedCallback: () -> Unit) {
        apiRequestService = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ApiRequestService::class.java)

        apiRequestService.getRegisterRequest().enqueue(object : Callback<RegisterUserResponse> {

            override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
                failedCallback()
            }

            override fun onResponse(
                call: Call<RegisterUserResponse>,
                response: Response<RegisterUserResponse>
            ) {
                if (!response.isSuccessful) {
                    failedCallback()
                    return
                }

                val result = response.body()

                if (result == null) {
                    failedCallback()
                    return
                }

                userId = UUID.fromString(result.userId)

                successCallback(userId)
            }
        })
    }

    fun getActionList(successCallback: (ArrayList<Auction>) -> Unit, failedCallback: () -> Unit) {
        apiRequestService.getActionListRequest().enqueue(object : Callback<AuctionListResponse> {

            override fun onFailure(call: Call<AuctionListResponse>, t: Throwable) {
                failedCallback()
            }

            override fun onResponse(
                call: Call<AuctionListResponse>,
                response: Response<AuctionListResponse>
            ) {
                if (!response.isSuccessful) {
                    failedCallback()
                    return
                }

                val result = response.body()

                if (result == null) {
                    failedCallback()
                    return
                }

                successCallback(result.activeAuctions)
            }
        })
    }

    fun getActionDetails(successCallback: (AuctionDetailsResponse) -> Unit, failedCallback: () -> Unit, auctionId: Int) {
        apiRequestService.getActionDetailsRequest(auctionId).enqueue(object : Callback<AuctionDetailsResponse> {

            override fun onFailure(call: Call<AuctionDetailsResponse>, t: Throwable) {
                failedCallback()
            }

            override fun onResponse(
                call: Call<AuctionDetailsResponse>,
                response: Response<AuctionDetailsResponse>
            ) {
                if (!response.isSuccessful) {
                    failedCallback()
                    return
                }

                val result = response.body()

                if (result == null) {
                    failedCallback()
                    return
                }

                successCallback(result)
            }
        })
    }

    fun getUser(callback: (User) -> Unit, failedCallback: () -> Unit) {
        apiRequestService.getUserRequest(userId.toString()).enqueue(object : Callback<UserResponse> {

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                failedCallback()
            }

            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                if (!response.isSuccessful) {
                    failedCallback()
                    return
                }

                val result = response.body()

                if (result == null){
                    failedCallback()
                    return
                }

                callback(result.user)
            }
        })
    }
}