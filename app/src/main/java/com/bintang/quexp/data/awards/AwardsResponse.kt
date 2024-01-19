package com.bintang.quexp.data.awards

import com.google.gson.annotations.SerializedName

data class AwardsResponse(

	@field:SerializedName("awards")
	val awards: List<AwardsItem>,

	@field:SerializedName("message")
	val message: String
)