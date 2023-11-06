package com.modiss.binar_challengech5.response

import com.modiss.binar_challengech5.items.FoodCategory

data class FoodCategoryResponse(
    val status: Boolean,
    val code: Int,
    val message: String,
    val data: List<FoodCategory>
)
