package com.bintang.quexp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.data.places.PlacesItem
import com.bintang.quexp.databinding.ItemPlacesBinding
import com.bintang.quexp.ui.places.PlacesDetailActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class PlacesAdapter(val placesList: List<PlacesItem>) : RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemPlacesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlacesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = placesList.get(position)
            binding.apply {
                txtPlaces.text = data.placeTitle
                txtDesc.text = data.placeDesc
                txtRating.text = data.placeRating
                ratingPlaces.rating = data.placeRating.toFloat()
                Glide.with(itemView.context)
                    .load(APIConfig.URL_IMG_PLACES + data.placeImg)
                    .transform(CenterCrop(), RoundedCorners(16))
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