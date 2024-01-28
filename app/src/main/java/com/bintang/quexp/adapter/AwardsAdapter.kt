package com.bintang.quexp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bintang.quexp.api.APIConfig.Companion.URL_IMG_AWARDS
import com.bintang.quexp.data.awards.AwardsItem
import com.bintang.quexp.databinding.ItemAwardsBinding
import com.bumptech.glide.Glide

class AwardsAdapter(val awardsList: List<AwardsItem>) :
    RecyclerView.Adapter<AwardsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemAwardsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAwardsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = awardsList.get(position)
            binding.apply {
                txtAwards.text = data.awardsRuleTitle
                Glide.with(itemView.context)
                    .load(URL_IMG_AWARDS + data.awardsRuleImg)
                    .centerInside()
                    .into(imgAwards)

                layout.setOnClickListener {
                    Toast.makeText(itemView.context, "Under Development", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return awardsList.size
    }
}