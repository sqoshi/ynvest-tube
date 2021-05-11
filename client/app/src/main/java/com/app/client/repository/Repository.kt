package com.app.client.repository

import com.app.client.model.Auction
import com.app.client.model.AuctionListResponse
import com.app.client.model.RegisterUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class Repository {

    companion object {
        private lateinit var apiRequestService: ApiRequestService
        private lateinit var userId: UUID
    }

    fun initialize(id: UUID) {
        apiRequestService = Retrofit.Builder()
            .baseUrl("https://best-api-on-planet-earth.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ApiRequestService::class.java)

        userId = id
    }

    fun initializeWithUserRegistration(callback: (UUID) -> Unit) {
        apiRequestService = Retrofit.Builder()
            .baseUrl("https://best-api-on-planet-earth.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ApiRequestService::class.java)

        apiRequestService.getRegisterRequest().enqueue(object : Callback<RegisterUserResponse> {

            override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
                throw NotImplementedError()
            }

            override fun onResponse(
                call: Call<RegisterUserResponse>,
                response: Response<RegisterUserResponse>
            ) {
                if (!response.isSuccessful)
                    throw NotImplementedError()

                val result = response.body()

                if (result == null)
                    throw Exception("Why the hell response is null")

                userId = UUID.fromString(result.userId)

                callback(userId)
            }
        })
    }

    fun getActionList(callback: (ArrayList<Auction>) -> Unit) {
        apiRequestService.getActionListRequest().enqueue(object : Callback<AuctionListResponse> {

            override fun onFailure(call: Call<AuctionListResponse>, t: Throwable) {
                throw NotImplementedError()
            }

            override fun onResponse(
                call: Call<AuctionListResponse>,
                response: Response<AuctionListResponse>
            ) {
                if (!response.isSuccessful)
                    throw NotImplementedError()

                val result = response.body()

                if (result == null)
                    throw Exception("Why the hell response is null")

                callback(result.auctions)
            }
        })
    }
}