package com.bintang.quexp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bintang.quexp.R
import com.bintang.quexp.data.local.ARData
import com.bintang.quexp.databinding.ItemArBinding


class ARAdapter(val arList: List<ARData>, val onItemClicked: (ARData) -> Unit) : RecyclerView.Adapter<ARAdapter.ViewHolder>() {

    var selectedItem = 0

    inner class ViewHolder(val binding: ItemArBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = arList.get(position)
            binding.apply {
                txtTitle.text = data.title
                itemView.setOnClickListener {
                    onItemClicked(data)
                }
                if (position == selectedItem){
                    root.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_ar_selected)
                    txtTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                } else {
                    root.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_default_color)
                    txtTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray_9a))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return arList.size
    }
}