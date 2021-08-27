package com.lollipop.imb.service.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProvinsiResult(
    val id: String = "-",
    val nama_provinsi: String = "-",
){
    override fun toString(): String {
        return nama_provinsi
    }
}