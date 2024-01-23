package com.bintang.quexp.data.roadmap

import com.google.gson.annotations.SerializedName

data class RoadmapItem(

	@field:SerializedName("awards_rule_title")
	val awardsRuleTitle: String,

	@field:SerializedName("awards_rule_desc")
	val awardsRuleDesc: String,

	@field:SerializedName("awards_rule_img")
	val awardsRuleImg: String,

	@field:SerializedName("id_awards_rule")
	val idAwardsRule: String,

	@field:SerializedName("awards_value")
	val awardsValue: Double
)