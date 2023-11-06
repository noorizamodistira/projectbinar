package com.modiss.binar_challengech5.api

import com.modiss.binar_challengech5.response.FoodCategoryResponse
import com.modiss.binar_challengech5.response.ListFoodResponse
import com.modiss.binar_challengech5.response.OrderRequest
import com.modiss.binar_challengech5.response.OrderResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("listmenu")
    fun getFoodItems(): Call<ListFoodResponse>

    @GET("category")
    fun getFoodCategory(): Call<FoodCategoryResponse>

    @POST("order")
    fun orderFood(@Body orderRequest: OrderRequest): Call<OrderResponse>
}

