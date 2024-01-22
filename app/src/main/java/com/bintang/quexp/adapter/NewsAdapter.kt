package com.bintang.quexp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintang.quexp.api.APIConfig.Companion.URL_IMG_NEWS
import com.bintang.quexp.data.news.NewsItem
import com.bintang.quexp.databinding.ItemNewsBinding
import com.bumptech.glide.Glide

class NewsAdapter(val newsList: List<NewsItem>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            val data = newsList.get(position)
            binding.apply {
                txtTitle.text = data.newsTitle
                Glide.with(itemView.context)
                    .load(URL_IMG_NEWS + data.newsImg)
                    .centerCrop()
                    .into(imgNews)

                layout.setOnClickListener {

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }
}