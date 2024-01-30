package com.bintang.quexp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.data.notification.NotificationItem
import com.bintang.quexp.databinding.ItemNotificationBinding
import com.bumptech.glide.Glide

class NotificationAdapter(val notificationList: List<NotificationItem>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = notificationList.get(position)
            binding.apply {
                txtTitle.text = data.notificationTitle
                txtDesc.text = data.notificationDesc
                txtCreaetedTime.text = data.notificationCreated

                Glide.with(itemView.context)
                    .load(APIConfig.BASE_URL + data.notificationImg)
                    .circleCrop()
                    .into(imgNotification)
            }
        }
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }
}