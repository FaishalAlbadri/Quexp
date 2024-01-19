package com.bintang.quexp.data.user

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("user")
	val userItem: UserItem
)