package com.example.learncore.api

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    /* region auth */

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("email") email: String?,
        @Field("password") password: String?
    ) : String

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String?,
        @Field("password") password: String?
    ) : String

    /*endregion*/
}