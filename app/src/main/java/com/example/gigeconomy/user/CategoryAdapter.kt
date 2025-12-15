package com.example.gigeconomy.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gigeconomy.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val categoryList: List<String>,
    private val onItemClick: (String) -> Unit // click listener lambda
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
        val categoryName = categoryList[position]
        holder.binding.txtCategoryName.text = categoryName

        holder.itemView.setOnClickListener {
            onItemClick(categoryName) // pass the selected category
        }
    }

    override fun getItemCount(): Int = categoryList.size
}
