package com.bintang.quexp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.data.ranking.RankingItem
import com.bintang.quexp.databinding.ItemRankingBinding
import com.bumptech.glide.Glide

class RankingAdapter(val rankingList: List<RankingItem>) :
    RecyclerView.Adapter<RankingAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRankingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = rankingList.get(position)
            val number = position + 4
            binding.apply {
                txtNumber.text = number.toString()
                txtTrophy.text = "${data.count} Lencana"
                txtUsername.text = data.userName
                Glide.with(itemView.context)
                    .load(APIConfig.URL_IMG_PROFILE + data.userImg)
                    .circleCrop()
                    .into(imgUser)
            }
        }
    }

    override fun getItemCount(): Int {
        return rankingList.size
    }
}