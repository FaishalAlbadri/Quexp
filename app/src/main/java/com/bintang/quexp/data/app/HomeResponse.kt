package com.bintang.quexp.data.app

import com.bintang.quexp.data.banner.BannerItem
import com.bintang.quexp.data.category.CategoryItem
import com.bintang.quexp.data.news.NewsItem
import com.bintang.quexp.data.places.PlacesItem
import com.bintang.quexp.data.ranking.RankingItem
import com.google.gson.annotations.SerializedName

data class HomeResponse(

    @field:SerializedName("banner")
    val banner: List<BannerItem>,

    @field:SerializedName("ranking")
    val ranking: List<RankingItem>,

    @field:SerializedName("category")
    val category: List<CategoryItem>,

    @field:SerializedName("places_populer")
    val placesPopuler: List<PlacesItem>,

    @field:SerializedName("news")
    val news: List<NewsItem>,

    @field:SerializedName("message")
    val message: String
)