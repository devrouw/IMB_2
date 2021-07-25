package com.lollipop.imb.service.model

data class Akun(
    val no_ktp: String = "",
    val nama_lengkap: String = "",
    val provinsi: String = "",
    val alamat: String = "",
    val no_hp: String = "",
    val email: String = "",
    val password: String = "-"
)