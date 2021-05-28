package com.app.ynvest_tube.repository

import com.app.ynvest_tube.model.AuctionDetails
import com.app.ynvest_tube.model.AuctionListResponse
import com.app.ynvest_tube.model.RegisterUserResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiRequestService {
    @GET("/users/register")
    fun getRegisterRequest(): Call<RegisterUserResponse>

    @GET("/auctions")
    fun getActionListRequest(): Call<AuctionListResponse>

    @GET("/auctions/{id}")
    fun getActionDetailsRequest(id: Int): Call<AuctionDetails>
}