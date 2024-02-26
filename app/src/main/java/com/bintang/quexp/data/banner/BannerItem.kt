package com.bintang.quexp.data.banner

import com.google.gson.annotations.SerializedName

data class BannerItem(

	@field:SerializedName("banner_created")
	val bannerCreated: String,

	@field:SerializedName("banner_img")
	val bannerImg: String,

	@field:SerializedName("banner_title")
	val bannerTitle: String,

	@field:SerializedName("id_banner")
	val idBanner: String
)