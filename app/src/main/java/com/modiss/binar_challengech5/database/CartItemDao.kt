package com.modiss.binar_challengech5.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.modiss.binar_challengech5.items.CartItem

@Dao
interface CartItemDao {
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): LiveData<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCartItem(cartItem: CartItem)

    @Update
    fun updateCartItem(cartItem: CartItem)

    @Delete
    fun deleteCartItem(cartItem: CartItem)

    @Query("DELETE FROM cart_items")
    fun deleteAllCartItems()


}
