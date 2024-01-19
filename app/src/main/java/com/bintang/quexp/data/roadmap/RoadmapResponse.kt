package com.bintang.quexp.data.roadmap

import com.google.gson.annotations.SerializedName

data class RoadmapResponse(

	@field:SerializedName("roadmap")
	val roadmap: List<RoadmapItem>,

	@field:SerializedName("message")
	val message: String
)