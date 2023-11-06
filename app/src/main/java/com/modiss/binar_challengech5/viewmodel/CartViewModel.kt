package com.modiss.binar_challengech5.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modiss.binar_challengech5.items.CartItem
import com.modiss.binar_challengech5.repository.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel(private val repository: CartRepository) : ViewModel() {

    val allCartItems: LiveData<List<CartItem>> = repository.allCartItems


    fun insertCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertCartItem(cartItem)
            }
        }
    }
    fun updateCartItem(cartItem: CartItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCartItem(cartItem)
        }
    }

    fun deleteCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteCartItem(cartItem)
            }
        }
    }
    fun deleteAllCartItems() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteAllCartItems()
            }
        }
    }



}
