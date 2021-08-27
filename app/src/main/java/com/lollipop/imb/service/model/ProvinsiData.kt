package com.lollipop.imb.service.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProvinsiData(
    val code: Int? = 0,
    val message: String? = "-",
    val data: List<ProvinsiResult>? = null,
    val status: String? = "-"
)