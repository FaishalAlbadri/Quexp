package com.bintang.quexp.data.banner

import com.google.gson.annotations.SerializedName

data class BannerResponse(

	@field:SerializedName("banner")
	val banner: List<BannerItem>,

	@field:SerializedName("message")
	val message: String
)