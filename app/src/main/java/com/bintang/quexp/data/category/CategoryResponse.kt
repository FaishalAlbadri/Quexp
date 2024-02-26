package com.bintang.quexp.data.category

import com.google.gson.annotations.SerializedName

data class CategoryResponse(

	@field:SerializedName("category")
	val category: List<CategoryItem>,

	@field:SerializedName("message")
	val message: String
)