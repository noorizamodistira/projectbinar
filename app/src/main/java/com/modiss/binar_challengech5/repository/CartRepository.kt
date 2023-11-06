package com.modiss.binar_challengech5.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.modiss.binar_challengech5.database.CartItemDao
import com.modiss.binar_challengech5.items.CartItem


class CartRepository(private val cartItemDao: CartItemDao) {

    val allCartItems: LiveData<List<CartItem>> = cartItemDao.getAllCartItems()

    fun insertCartItem(cartItem: CartItem) {
        cartItemDao.insertCartItem(cartItem)
    }

    fun deleteCartItem(cartItem: CartItem) {
        cartItemDao.deleteCartItem(cartItem)
    }
    fun updateCartItem(cartItem: CartItem) {
        cartItemDao.updateCartItem(cartItem)
    }
    fun deleteAllCartItems() {
        cartItemDao.deleteAllCartItems()
    }

}
