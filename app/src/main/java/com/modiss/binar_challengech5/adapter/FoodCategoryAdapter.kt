package com.modiss.binar_challengech5.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.modiss.binar_challengech5.databinding.CategoryItemBinding
import com.modiss.binar_challengech5.items.FoodCategory

class FoodCategoryAdapter : ListAdapter<FoodCategory, FoodCategoryAdapter.ViewHolder>(FoodCategoryDiffCallback()) {

    inner class ViewHolder(private val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: FoodCategory) {
            binding.txtCategory.text = category.nama
            Glide.with(itemView)
                .load(category.imageUrl)
                .into(binding.imgCategory)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)
    }
}

private class FoodCategoryDiffCallback : DiffUtil.ItemCallback<FoodCategory>() {
    override fun areItemsTheSame(oldItem: FoodCategory, newItem: FoodCategory): Boolean {
        return oldItem.nama == newItem.nama
    }

    override fun areContentsTheSame(oldItem: FoodCategory, newItem: FoodCategory): Boolean {
        return oldItem == newItem
    }
}
