package com.bintang.quexp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintang.quexp.databinding.ItemDiscoveredArBinding

class DiscoveredARAdapter(val descList: List<String>) :
    RecyclerView.Adapter<DiscoveredARAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemDiscoveredArBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDiscoveredArBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val number = position + 1
            binding.apply {
                txtNumber.text = number.toString() + "."
                txtDesc.text = descList.get(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return descList.size
    }
}