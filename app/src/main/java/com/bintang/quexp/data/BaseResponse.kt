package com.bintang.quexp.data

import com.google.gson.annotations.SerializedName

data class BaseResponse(
	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("alert_message")
	val alertMessage: String,

	@field:SerializedName("place_desc")
	val placeDesc: String
)