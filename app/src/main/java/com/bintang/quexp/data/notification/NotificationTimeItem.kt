package com.bintang.quexp.data.notification

import com.google.gson.annotations.SerializedName

data class NotificationTimeItem(

	@field:SerializedName("notification")
	val notification: List<NotificationItem>,

	@field:SerializedName("notification_created")
	val notificationCreated: String
)