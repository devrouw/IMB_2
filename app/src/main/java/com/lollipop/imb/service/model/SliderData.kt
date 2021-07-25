package com.lollipop.imb.service.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SliderData(
    val code: Int? = 0,
    val message: String? = "-",
    val data: List<SliderResult>? = null,
    val status: String? = "-"
)