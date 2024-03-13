package com.bintang.quexp.data.ranking

import com.google.gson.annotations.SerializedName

data class RankingResponse(

	@field:SerializedName("ranking")
	val ranking: List<RankingItem>,

	@field:SerializedName("message")
	val message: String
)