package com.bintang.quexp.data.places

import com.google.gson.annotations.SerializedName

data class PlacesItem(

	@field:SerializedName("id_place")
	val idPlace: String,

	@field:SerializedName("place_created")
	val placeCreated: String,

	@field:SerializedName("place_title")
	val placeTitle: String,

	@field:SerializedName("place_desc")
	val placeDesc: String,

	@field:SerializedName("place_qrcode")
	val placeQrcode: String,

	@field:SerializedName("place_gmaps")
	val placeGmaps: String,

	@field:SerializedName("place_img")
	val placeImg: String,

	@field:SerializedName("place_city")
	val placeCity: String,

	@field:SerializedName("place_rating")
	val placeRating: String
)