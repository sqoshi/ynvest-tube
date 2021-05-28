package com.app.ynvest_tube.repository

import com.app.ynvest_tube.model.AuctionDetailsResponse
import com.app.ynvest_tube.model.AuctionListResponse
import com.app.ynvest_tube.model.RegisterUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiRequestService {
    @GET("/users/register")
    fun getRegisterRequest(): Call<RegisterUserResponse>

    @GET("/auctions")
    fun getActionListRequest(): Call<AuctionListResponse>

    @GET("/auctions/{id}")
    fun getActionDetailsRequest(@Path("id") id: Int): Call<AuctionDetailsResponse>
}