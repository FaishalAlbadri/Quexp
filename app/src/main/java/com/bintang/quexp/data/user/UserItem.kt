package com.bintang.quexp.data.user

import com.google.gson.annotations.SerializedName

data class UserItem(

	@field:SerializedName("user_email")
	val userEmail: String,

	@field:SerializedName("user_password")
	val userPassword: String,

	@field:SerializedName("user_name")
	val userName: String,

	@field:SerializedName("user_phone")
	val userPhone: String,

	@field:SerializedName("user_city")
	val userCity: String,

	@field:SerializedName("user_created")
	val userCreated: String,

	@field:SerializedName("id_user")
	val idUser: String,

	@field:SerializedName("user_token")
	val userToken: String,

	@field:SerializedName("user_img")
	val userImg: String
)