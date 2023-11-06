package com.modiss.binar_challengech5.items

import com.google.gson.annotations.SerializedName

data class FoodCategory(
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("nama")
    val nama: String
)
