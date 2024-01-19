package com.bintang.quexp.data.news

import com.google.gson.annotations.SerializedName

data class NewsResponse(

	@field:SerializedName("news")
	val news: List<NewsItem>,

	@field:SerializedName("message")
	val message: String
)