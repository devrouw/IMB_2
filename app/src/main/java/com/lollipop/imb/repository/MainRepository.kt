package com.lollipop.imb.repository

import androidx.lifecycle.MutableLiveData
import com.lollipop.imb.util.ResultOfNetwork
import com.lollipop.imb.service.model.*
import com.lollipop.imb.service.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class MainRepository {
    val dataResult = MutableLiveData<ResultOfNetwork<KirimData>>()
    val loginResult = MutableLiveData<ResultOfNetwork<LoginData>>()
    val bannerResult = MutableLiveData<ResultOfNetwork<SliderData>>()
    val pengajuanResult = MutableLiveData<ResultOfNetwork<PengajuanData>>()
    val dataPengajuanResult = MutableLiveData<ResultOfNetwork<PengajuanData>>()
    val provinsiResult = MutableLiveData<ResultOfNetwork<ProvinsiData>>()
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
        val reqBody: RequestBody =
            data.formulir_imb.asRequestBody("multipart/form-file".toMediaTypeOrNull())
        val reqBody1: RequestBody =
            data.scan_ektp.asRequestBody("multipart/form-file".toMediaTypeOrNull())
        val reqBody2: RequestBody =
            data.scan_bukti_lunas_pbb.asRequestBody("multipart/form-file".toMediaTypeOrNull())
        val reqBody3: RequestBody =
            data.scan_bukti_penguasaan_tanah.asRequestBody("multipart/form-file".toMediaTypeOrNull())
        val reqBody4: RequestBody =
            data.perhitungan_data_sondir.asRequestBody("multipart/form-file".toMediaTypeOrNull())
        val reqBody5: RequestBody =
            data.scan_kaji_lingkungan.asRequestBody("multipart/form-file".toMediaTypeOrNull())
        val reqBody6: RequestBody =
            data.scan_andalalin.asRequestBody("multipart/form-file".toMediaTypeOrNull())
        val reqBody7: RequestBody =
            data.gambar_bangunan.asRequestBody("multipart/form-file".toMediaTypeOrNull())

        val partImage: MultipartBody.Part = MultipartBody.Part.createFormData("formulir_imb", data.formulir_imb.name, reqBody)
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
                    partImage,
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

    suspend fun dataPengajuan(case: String, id: String) =
        withContext(Dispatchers.IO){
            dataPengajuanResult.postValue(ResultOfNetwork.Success(
                RetrofitClient.ftp.dataPengajuan(case,id)
            ))
        }

    suspend fun uploadBukti(case: String, id: String, imageBase: String, imageName: String) =
        withContext(Dispatchers.IO){
            dataResult.postValue(ResultOfNetwork.Success(
                RetrofitClient.ftp.uploadBukti(case,id,imageBase,imageName)
            ))
            progressBar.postValue(false)
        }

    suspend fun listProvinsi(case: String) =
        withContext(Dispatchers.IO){
            provinsiResult.postValue(ResultOfNetwork.Success(
                RetrofitClient.ftp.listProvinsi(case)
            ))
        }

}