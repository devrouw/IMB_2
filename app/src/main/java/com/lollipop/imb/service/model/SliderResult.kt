package com.lollipop.imb.service.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SliderResult(
    val id: String? = "-",
    val image_url: String? = "-"
)