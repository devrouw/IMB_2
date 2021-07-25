package com.lollipop.e_lapor.repository

import androidx.lifecycle.MutableLiveData
import com.lollipop.e_lapor.util.ResultOfNetwork
import com.lollipop.imb.service.model.*
import com.lollipop.imb.service.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class MainRepository() {
    val dataResult = MutableLiveData<ResultOfNetwork<KirimData>>()
    val loginResult = MutableLiveData<ResultOfNetwork<LoginData>>()
    val bannerResult = MutableLiveData<ResultOfNetwork<SliderData>>()
    val pengajuanResult = MutableLiveData<ResultOfNetwork<PengajuanData>>()
    val progressBar = MutableLiveData<Boolean>()

    suspend fun daftarAkun(case: String, akun: Akun) =
        withContext(Dispatchers.IO){
            dataResult.postValue(ResultOfNetwork.Success(
                RetrofitClient.ftp.daftar(case,akun.no_ktp,akun.nama_lengkap,akun.provinsi,akun.alamat,akun.no_hp,
                    akun.email,akun.password)
            ))
        }

    suspend fun loginAkun(case: String, email: String, password: String) =
        withContext(Dispatchers.IO){
            loginResult.postValue(ResultOfNetwork.Success(
                RetrofitClient.ftp.login(case,email,password)
            ))
        }

    suspend fun pengajuan(case: String, noKtp: String, data: DataPengajuan){
        val reqBody1: RequestBody = RequestBody.create("multipart/form-file".toMediaTypeOrNull(), data.scan_ektp)
        val reqBody2: RequestBody = RequestBody.create("multipart/form-file".toMediaTypeOrNull(), data.scan_bukti_lunas_pbb)
        val reqBody3: RequestBody = RequestBody.create("multipart/form-file".toMediaTypeOrNull(), data.scan_bukti_penguasaan_tanah)
        val reqBody4: RequestBody = RequestBody.create("multipart/form-file".toMediaTypeOrNull(), data.perhitungan_data_sondir)
        val reqBody5: RequestBody = RequestBody.create("multipart/form-file".toMediaTypeOrNull(), data.scan_kaji_lingkungan)
        val reqBody6: RequestBody = RequestBody.create("multipart/form-file".toMediaTypeOrNull(), data.scan_andalalin)
        val reqBody7: RequestBody = RequestBody.create("multipart/form-file".toMediaTypeOrNull(), data.gambar_bangunan)

        val partImage1: MultipartBody.Part = MultipartBody.Part.createFormData("scan_ektp", data.scan_ektp.name, reqBody1)
        val partImage2: MultipartBody.Part = MultipartBody.Part.createFormData("scan_bukti_lunas_pbb", data.scan_bukti_lunas_pbb.name, reqBody2)
        val partImage3: MultipartBody.Part = MultipartBody.Part.createFormData("scan_bukti_penguasaan_tanah", data.scan_bukti_penguasaan_tanah.name, reqBody3)
        val partImage4: MultipartBody.Part = MultipartBody.Part.createFormData("perhitungan_data_sondir", data.perhitungan_data_sondir.name, reqBody4)
        val partImage5: MultipartBody.Part = MultipartBody.Part.createFormData("scan_kaji_lingkungan", data.scan_kaji_lingkungan.name, reqBody5)
        val partImage6: MultipartBody.Part = MultipartBody.Part.createFormData("scan_andalalin", data.scan_andalalin.name, reqBody6)
        val partImage7: MultipartBody.Part = MultipartBody.Part.createFormData("gambar_bangunan", data.gambar_bangunan.name, reqBody7)

        withContext(Dispatchers.IO){
            dataResult.postValue(ResultOfNetwork.Success(
                RetrofitClient.ftp.pengajuan(case.toRequestBody(("text/plain".toMediaTypeOrNull())),
                    partImage1,
                    partImage2,
                    partImage3,
                    partImage4,
                    partImage5,
                    partImage6,
                    partImage7,
                    noKtp.toRequestBody(("text/plain".toMediaTypeOrNull())))
            ))
        }
        progressBar.postValue(false)
    }

    suspend fun banner(case: String) =
        withContext(Dispatchers.IO){
            bannerResult.postValue(ResultOfNetwork.Success(
                RetrofitClient.ftp.banner(case)
            ))
        }

    suspend fun listPengajuan(case: String, noKtp: String) =
        withContext(Dispatchers.IO){
            pengajuanResult.postValue(ResultOfNetwork.Success(
                RetrofitClient.ftp.listPengajuan(case,noKtp)
            ))
        }

}