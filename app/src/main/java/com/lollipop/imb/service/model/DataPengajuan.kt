package com.lollipop.imb.service.model

import java.io.File

data class DataPengajuan(
    val formulir_imb: File,
    val scan_ektp: File,
    val scan_bukti_lunas_pbb: File,
    val scan_bukti_penguasaan_tanah: File,
    val perhitungan_data_sondir: File,
    val scan_kaji_lingkungan: File,
    val scan_andalalin: File,
    val gambar_bangunan: File,
)
