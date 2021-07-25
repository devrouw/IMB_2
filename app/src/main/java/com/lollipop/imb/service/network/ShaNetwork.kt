package com.lollipop.imb.service.network

import com.lollipop.imb.service.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ShaNetwork {
    @FormUrlEncoded
    @POST("api.php")
    suspend fun daftar(
        @Field("case") case : String,
        @Field("no_ktp") noKtp : String,
        @Field("nama_lengkap") namaLengkap : String,
        @Field("provinsi") provinsi : String,
        @Field("alamat") alamat : String,
        @Field("no_hp") noHp : String,
        @Field("email") email : String,
        @Field("password") password : String
    ) : KirimData

    @FormUrlEncoded
    @POST("api.php")
    suspend fun login(
        @Field("case") case : String,
        @Field("email") email : String,
        @Field("password") password : String
    ) : LoginData

    @Multipart
    @POST("api.php")
    suspend fun pengajuan(
        @Part("case") case : RequestBody,
        @Part scan_ektp : MultipartBody.Part,
        @Part scan_bukti_lunas_pbb : MultipartBody.Part,
        @Part scan_bukti_penguasaan_tanah : MultipartBody.Part,
        @Part perhitungan_data_sondir : MultipartBody.Part,
        @Part scan_kaji_lingkungan : MultipartBody.Part,
        @Part scan_andalalin : MultipartBody.Part,
        @Part gambar_bangunan : MultipartBody.Part,
        @Part("no_ktp") id_akun : RequestBody
    ) : KirimData

    @FormUrlEncoded
    @POST("api.php")
    suspend fun banner(
        @Field("case") case : String
    ) : SliderData

    @FormUrlEncoded
    @POST("api.php")
    suspend fun listPengajuan(
        @Field("case") case : String,
        @Field("no_ktp") noKtp : String
    ) : PengajuanData
}