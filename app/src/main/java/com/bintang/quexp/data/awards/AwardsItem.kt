package com.bintang.quexp.data.awards

import com.google.gson.annotations.SerializedName

data class AwardsItem(

	@field:SerializedName("awards_rule_title")
	val awardsRuleTitle: String,

	@field:SerializedName("awards_rule_desc")
	val awardsRuleDesc: String,

	@field:SerializedName("awards_rule_img")
	val awardsRuleImg: String,

	@field:SerializedName("awards_created")
	val awardsCreated: String,

	@field:SerializedName("id_awards")
	val idAwards: String
)