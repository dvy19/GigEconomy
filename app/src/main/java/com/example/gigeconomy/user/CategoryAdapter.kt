package com.example.gigeconomy.user


import android.view.LayoutInflater
import com.example.gigeconomy.R

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gigeconomy.databinding.ItemCategoryBinding


class CategoryAdapter(
    private val CategoryList: List<String>
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding: ItemCategoryBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val CategoryName = CategoryList[position]

        holder.binding.txtCategoryName.text = CategoryName





    }

    override fun getItemCount(): Int = CategoryList.size
}
