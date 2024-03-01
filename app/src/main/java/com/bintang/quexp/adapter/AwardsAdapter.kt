package com.bintang.quexp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bintang.quexp.api.APIConfig.Companion.URL_IMG_AWARDS
import com.bintang.quexp.data.awards.AwardsItem
import com.bintang.quexp.databinding.ItemAwardsBinding
import com.bintang.quexp.ui.news.NewsDetailActivity
import com.bintang.quexp.ui.share.ShareAwardsActivity
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
                    .load(URL_IMG_AWARDS + data.awardsRuleImg + ".png")
                    .centerInside()
                    .into(imgAwards)

                layout.setOnClickListener {
                    itemView.context.startActivity(
                        Intent(
                            itemView.context,
                            ShareAwardsActivity::class.java
                        )
                            .putExtra("title", data.awardsRuleTitle)
                            .putExtra("img", data.awardsRuleImg + ".png")
                            .putExtra("desc", data.awardsRuleDesc)
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return awardsList.size
    }
}