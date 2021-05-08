package com.app.client.repository

import com.app.client.model.RegisterUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

class Repository {

    companion object {
        private lateinit var apiRequestService: ApiRequestService
        private lateinit var userId: UUID
    }

    public fun initialize(id: UUID) {
        apiRequestService = Retrofit.Builder()
            .baseUrl("https://best-api-on-planet-earth.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ApiRequestService::class.java)

        userId = id
    }

    public fun initializeWithUserRegistration(callback: (UUID) -> Unit) {
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
                val result = response.body()

                if (result == null)
                    throw Exception("Why the hell response is null")

                if (!response.isSuccessful)
                    throw NotImplementedError()

                userId = UUID.fromString(result.userId)

                callback(userId)
            }
        })
    }
}