package com.bintang.quexp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.data.places.PlacesItem
import com.bintang.quexp.databinding.ItemPlacesSearchBinding
import com.bintang.quexp.ui.places.PlacesDetailActivity
import com.bumptech.glide.Glide

class PlacesSearchAdapter(val placesList: List<PlacesItem>) : RecyclerView.Adapter<PlacesSearchAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemPlacesSearchBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlacesSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = placesList.get(position)
            binding.apply {
                txtPlaces.text = data.placeTitle
                txtLocation.text = "${data.placeCity}, Indonesia"
                txtRating.text = data.placeRating
                ratingPlaces.rating = data.placeRating.toFloat()
                Glide.with(itemView.context)
                    .load(APIConfig.URL_IMG_PLACES + data.placeImg)
                    .centerCrop()
                    .into(imgPlaces)

                root.setOnClickListener {
                    itemView.context.startActivity(
                        Intent(
                            itemView.context,
                            PlacesDetailActivity::class.java
                        )
                            .putExtra("place_title", data.placeTitle)
                            .putExtra("place_rating", data.placeRating)
                            .putExtra("place_desc", data.placeDesc)
                            .putExtra("place_gmaps", data.placeGmaps)
                            .putExtra("place_city", data.placeCity)
                            .putExtra("place_img", data.placeImg)
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return placesList.size
    }
}