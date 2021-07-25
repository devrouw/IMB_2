package com.lollipop.imb.service.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PengajuanData(
    val code: Int? = 0,
    val message: String? = "-",
    val data: List<PengajuanResult>? = null,
    val status: String? = "-"
)