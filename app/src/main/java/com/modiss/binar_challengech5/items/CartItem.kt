package com.modiss.binar_challengech5.items

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val foodName: String,
    var totalPrice: Int,
    var price: Int,
    var quantity: Int,
    var catatan: String,
    val imageUrl: String

){
    fun calculateTotalPrice(): Int {
        return quantity * price
    }
}

