package com.bintang.quexp.data.visited

import com.google.gson.annotations.SerializedName

data class VisitedResponse(

    @field:SerializedName("visited")
	val visited: List<VisitedItem>,

    @field:SerializedName("message")
	val message: String
)