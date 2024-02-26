package com.bintang.quexp.data.category

import com.google.gson.annotations.SerializedName

data class CategoryItem(

	@field:SerializedName("category_name")
	val categoryName: String,

	@field:SerializedName("id_category")
	val idCategory: String,

	@field:SerializedName("category_created")
	val categoryCreated: String,

	@field:SerializedName("category_img")
	val categoryImg: String
)