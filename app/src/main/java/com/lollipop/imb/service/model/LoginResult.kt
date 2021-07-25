package com.lollipop.imb.service.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResult(
    val id: String? = "-",
    val email: String? = "-",
    val password: String? = "-",
    val no_ktp: String? = "-",
    val id_role: String? = "-"
)
