package com.bintang.quexp.data.notification

import com.google.gson.annotations.SerializedName

data class NotificationResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("notification_time")
	val notificationTime: List<NotificationTimeItem>
)