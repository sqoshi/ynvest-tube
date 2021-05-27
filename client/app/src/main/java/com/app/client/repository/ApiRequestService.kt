package com.app.client.repository

import com.app.client.model.AuctionDetails
import com.app.client.model.AuctionListResponse
import com.app.client.model.RegisterUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiRequestService {
    @GET("/users/register")
    fun getRegisterRequest(): Call<RegisterUserResponse>

    @GET("/auctions")
    fun getActionListRequest(): Call<AuctionListResponse>

    @GET("/auctions/{id}")
    fun getActionDetailsRequest(id: Int): Call<AuctionDetails>
}