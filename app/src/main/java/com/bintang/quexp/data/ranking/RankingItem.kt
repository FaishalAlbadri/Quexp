package com.bintang.quexp.data.ranking

import com.google.gson.annotations.SerializedName

data class RankingItem(

	@field:SerializedName("user_name")
	val userName: String,

	@field:SerializedName("count")
	val count: String,

	@field:SerializedName("id_user")
	val idUser: String,

	@field:SerializedName("user_img")
	val userImg: String
)