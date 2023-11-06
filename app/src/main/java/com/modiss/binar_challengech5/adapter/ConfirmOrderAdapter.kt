package com.modiss.binar_challengech5.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.modiss.binar_challengech5.databinding.OrderItemBinding
import com.modiss.binar_challengech5.items.CartItem


class ConfirmOrderAdapter :
    ListAdapter<CartItem, ConfirmOrderAdapter.ViewHolder>(CartItemDiffCallback()) {

    inner class ViewHolder(private val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItem) {
            Glide.with(itemView)
                .load(cartItem.imageUrl)
                .into(binding.imgCartItem)
            binding.txtCartItemName.text = cartItem.foodName
            binding.txtCartItemPrice.text = "Rp. ${cartItem.totalPrice}"
            binding.txtItemQuantity.text = cartItem.quantity.toString()
        }
    }
    fun calculateTotalPrice(): Int {
        var totalPrice = 0
        currentList.forEach { cartItem ->
            totalPrice += cartItem.totalPrice
        }

        return totalPrice
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = OrderItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = getItem(position)
        holder.bind(cartItem)
    }

    private class CartItemDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}

