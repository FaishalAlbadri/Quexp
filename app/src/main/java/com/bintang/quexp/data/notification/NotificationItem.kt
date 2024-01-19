package com.bintang.quexp.data.notification

import com.google.gson.annotations.SerializedName

data class NotificationItem(

	@field:SerializedName("notification_desc")
	val notificationDesc: String,

	@field:SerializedName("id_notification")
	val idNotification: String,

	@field:SerializedName("notification_created")
	val notificationCreated: String,

	@field:SerializedName("notification_title")
	val notificationTitle: String,

	@field:SerializedName("notification_img")
	val notificationImg: String
)