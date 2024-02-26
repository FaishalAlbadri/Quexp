package com.bintang.quexp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.data.category.CategoryItem
import com.bintang.quexp.databinding.ItemCategoryRoadmapBinding
import com.bintang.quexp.ui.roadmap.RoadmapActivity
import com.bumptech.glide.Glide

class CategoryRoadmapAdapter(val categoryList: List<CategoryItem>) :
    RecyclerView.Adapter<CategoryRoadmapAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCategoryRoadmapBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryRoadmapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = categoryList.get(position)
            binding.apply {
                txtCategory.text = data.categoryName
                Glide.with(itemView.context)
                    .load(APIConfig.URL_IMG_CATEGORY + data.categoryImg)
                    .centerCrop()
                    .into(imgCategory)

                btnCategory.setOnClickListener {
                    itemView.context.startActivity(
                        Intent(
                            itemView.context,
                            RoadmapActivity::class.java
                        )
                            .putExtra("id", data.idCategory)
                            .putExtra("name", data.categoryName)
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}