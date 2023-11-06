package com.modiss.binar_challengech5.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.modiss.binar_challengech5.databinding.CartItemBinding
import com.modiss.binar_challengech5.items.CartItem
import com.modiss.binar_challengech5.viewmodel.CartViewModel


class CartAdapter(private val viewModel: CartViewModel,
                  private val onItemClick: (CartItem) -> Unit) :
    ListAdapter<CartItem, CartAdapter.CartViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }


    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = getItem(position)

        holder.bind(currentItem)

        holder.binding.btnDecrease.setOnClickListener {
            currentItem.quantity++
            updateCartItem(currentItem)
            currentItem.totalPrice = currentItem.calculateTotalPrice()
            notifyItemChanged(holder.bindingAdapterPosition)
        }

        holder.binding.btnIncrease.setOnClickListener {
            currentItem.quantity--
            currentItem.totalPrice = currentItem.calculateTotalPrice()
            if (currentItem.quantity < 1) {
                showDeleteConfirmationDialog(holder)
            }
            updateCartItem(currentItem)
            notifyItemChanged(holder.bindingAdapterPosition)
        }

        holder.binding.btnDelete.setOnClickListener {
            showDeleteConfirmationDialog(holder)
        }
    }
    fun calculateTotalPrice(): Int {
        var totalPrice = 0
        currentList.forEach { cartItem ->
            totalPrice += cartItem.totalPrice
        }

        return totalPrice
    }
    private fun updateCartItem(cartItem: CartItem) {
        viewModel.updateCartItem(cartItem)
    }


    inner class CartViewHolder(val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            Glide.with(itemView)
                .load(cartItem.imageUrl)
                .into(binding.imgCartItem)
            binding.txtCartItemName.text = cartItem.foodName
            binding.txtCartItemPrice.text = "Rp. ${cartItem.totalPrice}"
            binding.txtItemQuantity.text = cartItem.quantity.toString()

            if (binding.txtItemQuantity.text == "0"){
                binding.txtItemQuantity.text = "1"
            }

        }
    }
    private fun showDeleteConfirmationDialog(holder: CartViewHolder) {
        val position = holder.bindingAdapterPosition
        if (position != RecyclerView.NO_POSITION) {
            val cartItem = getItem(position)

            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Delete") { _, _ ->
                    onItemClick(cartItem)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CartItem>() {
            override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
                return oldItem.foodName == newItem.foodName
            }

            override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
