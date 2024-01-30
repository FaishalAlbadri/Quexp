package com.bintang.quexp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bintang.quexp.data.notification.NotificationTimeItem
import com.bintang.quexp.databinding.ItemNotificationBaseBinding

class NotificationBaseAdapter(val notificationList: List<NotificationTimeItem>) :
    RecyclerView.Adapter<NotificationBaseAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemNotificationBaseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationBaseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = notificationList.get(position)
            binding.apply {
                txtDate.text = data.notificationCreated
                rvNotification.apply {
                    layoutManager = LinearLayoutManager(this.context)
                    adapter = NotificationAdapter(data.notification)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }
}