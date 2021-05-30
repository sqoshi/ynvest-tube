package com.app.ynvest_tube.repository

import com.app.ynvest_tube.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiRequestService {
    @GET("/users/register")
    fun getRegisterRequest(): Call<RegisterUserResponse>

    @GET("/auctions")
    fun getActionListRequest(): Call<AuctionListResponse>

    @GET("/auctions/{id}")
    fun getActionDetailsRequest(@Path("id") id: Int): Call<AuctionDetailsResponse>

    @POST("/auctions/{id}")
    fun getBidOnActionRequest(@Path("id") id: Int, @Body bidRequest: AuctionBidRequest): Call<AuctionDetailsResponse>

    @POST("/user")
    fun getUserRequest(@Body userId: UserIdRequest): Call<UserResponse>

    @POST("/user/details")
    fun getUserDetailsRequest(@Body userId: UserIdRequest): Call<UserDetailsResponse>
}