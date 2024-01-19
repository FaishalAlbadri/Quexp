package com.bintang.quexp.data.places

import com.google.gson.annotations.SerializedName

data class PlacesResponse(

	@field:SerializedName("places")
	val places: List<PlacesItem>,

	@field:SerializedName("message")
	val message: String
)