package com.bintang.quexp.data.local

data class UserData(
    val id_user: String,
    val user_name: String,
    val user_token: String,
    val isLogin: Boolean,
    val featureDiscovery: Boolean,
    val arDiscovery: Boolean
)