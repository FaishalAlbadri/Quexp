package com.bintang.quexp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bintang.quexp.api.APIConfig.Companion.URL_IMG_PLACES
import com.bintang.quexp.data.visited.VisitedItem
import com.bintang.quexp.databinding.ItemVisitedBinding
import com.bumptech.glide.Glide

class VisitedAdapter(val visitedList: List<VisitedItem>) :
    RecyclerView.Adapter<VisitedAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemVisitedBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemVisitedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = visitedList.get(position)
            binding.apply {
                txtPlace.text = data.placeTitle
                txtRating.text = data.placeRating
                Glide.with(itemView.context)
                    .load(URL_IMG_PLACES + data.placeImg)
                    .centerCrop()
                    .into(imgPlaces)

                layout.setOnClickListener {
                    Toast.makeText(itemView.context, "Under Development", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return visitedList.size
    }
}