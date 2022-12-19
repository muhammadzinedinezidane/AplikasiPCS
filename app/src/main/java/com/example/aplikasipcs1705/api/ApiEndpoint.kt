package com.example.aplikasipcs1705.api

import com.example.aplikasipcs1705.response.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiEndpoint {
    @FormUrlEncoded
    @POST ("login")
    fun login(
        @Field("email") email : String,
        @Field("password") password :String
    ): retrofit2.Call<LoginResponse>
}
