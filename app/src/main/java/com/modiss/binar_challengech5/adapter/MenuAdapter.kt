package com.modiss.binar_challengech5.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.modiss.binar_challengech5.databinding.MenuItemBinding
import com.modiss.binar_challengech5.items.FoodItem

class MenuAdapter(
    private val onItemClick: (FoodItem) -> Unit
) : ListAdapter<FoodItem, MenuAdapter.ViewHolder>(MenuDiffCallback()) {

    inner class ViewHolder(private val binding: MenuItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(menuItem: FoodItem) {

            binding.txtFoodName.text = menuItem.nama
            binding.txtFoodPrice.text = "Rp. ${menuItem.harga}"
            Glide.with(itemView)
                .load(menuItem.image_url)
                .into(binding.imgFood)

            itemView.setOnClickListener {
                onItemClick(menuItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class MenuDiffCallback : DiffUtil.ItemCallback<FoodItem>() {
    override fun areItemsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
        return oldItem.nama == newItem.nama
    }

    override fun areContentsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
        return oldItem == newItem
    }
}
