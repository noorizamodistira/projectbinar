package com.modiss.binar_challengech5.items

data class FoodItem(
    val image_url: String,
    val nama: String,
    val harga_format: String,
    val harga: Int,
    val detail: String,
    val alamat_resto: String
)
