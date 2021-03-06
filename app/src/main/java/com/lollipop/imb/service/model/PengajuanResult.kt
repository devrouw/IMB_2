package com.lollipop.imb.service.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PengajuanResult(
    val id: String? = "-",
    val izin_mendirikan_bangunan: String? = "-",
    val scan_ektp: String? = "-",
    val scan_bukti_lunas_pbb: String? = "-",
    val scan_bukti_penguasaan_tanah: String? = "-",
    val perhitungan_data_sondir: String? = "-",
    val scan_kaji_lingkungan: String? = "-",
    val scan_andalalin: String? = "-",
    val gambar_bangunan: String? = "-",
    val no_ktp: String? = "-",
    val role: String? = "-",
    val sertifikat_IMB: String? = "-",
)