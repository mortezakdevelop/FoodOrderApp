package com.morteza.foodorderapplication.network

import com.morteza.foodorderapplication.models.BodyRegister
import com.morteza.foodorderapplication.models.ResponseRecipes
import com.morteza.foodorderapplication.models.ResponseRegister
import com.morteza.foodorderapplication.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.*

interface ApiServices {
    
    @POST("users/connect")
    suspend fun postRegister(@Query(API_KEY) apiKey: String, @Body body: BodyRegister): Response<ResponseRegister>

    @GET("recipes/complexSearch")
    suspend fun getRecipes(@QueryMap query: Map<String,String>):Response<ResponseRecipes>

}