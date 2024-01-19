package com.bintang.quexp.data.news

import com.google.gson.annotations.SerializedName

data class NewsItem(

	@field:SerializedName("news_created")
	val newsCreated: String,

	@field:SerializedName("id_news")
	val idNews: String,

	@field:SerializedName("news_desc")
	val newsDesc: String,

	@field:SerializedName("news_title")
	val newsTitle: String,

	@field:SerializedName("news_img")
	val newsImg: String
)