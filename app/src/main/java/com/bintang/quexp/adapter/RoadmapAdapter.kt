package com.bintang.quexp.adapter

import android.graphics.PorterDuff.Mode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bintang.quexp.R
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.data.roadmap.RoadmapItem
import com.bintang.quexp.databinding.ItemRoadmapBinding
import com.bumptech.glide.Glide

class RoadmapAdapter(val roadmapList: List<RoadmapItem>) :
    RecyclerView.Adapter<RoadmapAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRoadmapBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRoadmapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = roadmapList.get(position)
            binding.apply {
                txtAwards.text = data.awardsRuleTitle
                txtAwardsValue.text = data.awardsRuleDesc
                Glide.with(itemView.context)
                    .load(APIConfig.URL_IMG_AWARDS + data.awardsRuleImg)
                    .centerInside()
                    .into(imgAwards)

                Glide.with(itemView.context)
                    .load(APIConfig.URL_IMG_AWARDS_BANNER + data.awardsRuleImg)
                    .centerCrop()
                    .into(imgAwardsBanner)

                if (data.awardsValue == 1.0) {
                    progressLeft.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.colorAccent
                        )
                    )
                    progressRight.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.colorAccent
                        )
                    )
                    imgActive.visibility = View.GONE
                } else if (data.awardsValue == 0.5) {
                    progressLeft.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.colorAccent
                        )
                    )
                    progressRight.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.gray_9a
                        )
                    )
                    imgActive.visibility = View.VISIBLE
                }

                if (position == 0){
                    progressLeft.visibility = View.INVISIBLE
                    progressRight.visibility = View.VISIBLE
                } else if (position == itemCount - 1) {
                    progressLeft.visibility = View.VISIBLE
                    progressRight.visibility = View.INVISIBLE
                }

                if (progressLeft.visibility == View.INVISIBLE) {
                    imgHalfRight.visibility = View.VISIBLE
                    imgHalfLeft.visibility = View.GONE
                } else if (progressRight.visibility == View.INVISIBLE) {
                    imgHalfRight.visibility = View.GONE
                    imgHalfLeft.visibility = View.VISIBLE
                }

                if (data.awardsValue > 0.0){
                    if (imgHalfLeft.visibility == View.VISIBLE) {
                        imgHalfLeft.setColorFilter(ContextCompat.getColor(itemView.context, R.color.colorAccent))
                    }
                    if (imgHalfRight.visibility == View.VISIBLE) {
                        imgHalfRight.setColorFilter(ContextCompat.getColor(itemView.context, R.color.colorAccent))
                    }
                } else {
                    if (imgHalfLeft.visibility == View.VISIBLE) {
                        imgHalfLeft.setColorFilter(ContextCompat.getColor(itemView.context, R.color.gray_9a))
                    }
                    if (imgHalfRight.visibility == View.VISIBLE) {
                        imgHalfRight.setColorFilter(ContextCompat.getColor(itemView.context, R.color.gray_9a))
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return roadmapList.size
    }
}