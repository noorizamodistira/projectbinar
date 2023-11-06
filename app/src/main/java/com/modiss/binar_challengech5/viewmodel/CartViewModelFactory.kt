package com.modiss.binar_challengech5.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.modiss.binar_challengech5.database.AppDatabase
import com.modiss.binar_challengech5.repository.CartRepository


class CartViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            val cartItemDao = AppDatabase.getDatabase(application).cartItemDao()
            val repository = CartRepository(cartItemDao)
            return CartViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
