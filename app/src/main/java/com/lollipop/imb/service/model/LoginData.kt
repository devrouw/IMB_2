package com.lollipop.imb.service.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginData(
    val code: Int?=0,
    val message: String?="-",
    val data: List<LoginResult>?=null,
    val status: String?="-"
)
