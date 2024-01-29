package com.bintang.quexp.data.visited

import com.google.gson.annotations.SerializedName

data class VisitedItem(

	@field:SerializedName("place_title")
	val placeTitle: String,

	@field:SerializedName("place_rating")
	val placeRating: String,

	@field:SerializedName("visited_created")
	val visitedCreated: String,

	@field:SerializedName("id_visited")
	val idVisited: String,

	@field:SerializedName("place_desc")
	val placeDesc: String,

	@field:SerializedName("place_gmaps")
	val placeGmaps: String,

	@field:SerializedName("place_city")
	val placeCity: String,

	@field:SerializedName("place_img")
	val placeImg: String
)