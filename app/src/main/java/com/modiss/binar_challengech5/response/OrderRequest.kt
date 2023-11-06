package com.modiss.binar_challengech5.response

import com.modiss.binar_challengech5.items.OrderItem

data class OrderRequest(
    val username: String,
    val total: Int,
    val orders: List<OrderItem>
)
