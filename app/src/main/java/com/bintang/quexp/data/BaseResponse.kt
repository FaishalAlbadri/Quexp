package com.bintang.quexp.data

import com.google.gson.annotations.SerializedName

data class BaseResponse(
	@field:SerializedName("message")
	val message: String
)