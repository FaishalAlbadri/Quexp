package com.bintang.quexp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.data.banner.BannerItem
import com.bintang.quexp.databinding.ItemBannerBinding
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter

class BannerAdapter(val bannerList: List<BannerItem>) :
    SliderViewAdapter<BannerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemBannerBinding) :
        SliderViewAdapter.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent!!.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = bannerList.get(position)
            binding.apply {
                Glide.with(itemView.context)
                    .load(APIConfig.URL_IMG_BANNER + data.bannerImg)
                    .centerCrop()
                    .into(imgSlider)
            }
        }
    }

    override fun getCount(): Int {
        return bannerList.size
    }

}