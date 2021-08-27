package com.lollipop.imb.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lollipop.imb.repository.MainRepository
import com.lollipop.imb.util.ResultOfNetwork
import com.lollipop.imb.service.model.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainViewModel : ViewModel() {
    private val _repository = MainRepository()

    val uploadBukti: LiveData<ResultOfNetwork<KirimData>>
    val daftarAkun: LiveData<ResultOfNetwork<KirimData>>
    val loginAkun: LiveData<ResultOfNetwork<LoginData>>
    val bannerData: LiveData<ResultOfNetwork<SliderData>>
    val listPengajuan: LiveData<ResultOfNetwork<PengajuanData>>
    val dataPengajuan: LiveData<ResultOfNetwork<PengajuanData>>
    val listProvinsi: LiveData<ResultOfNetwork<ProvinsiData>>
    val progressBar: LiveData<Boolean>

    init {
        this.listProvinsi = _repository.provinsiResult
        this.dataPengajuan = _repository.dataPengajuanResult
        this.uploadBukti = _repository.dataResult
        this.daftarAkun = _repository.dataResult
        this.loginAkun = _repository.loginResult
        this.bannerData = _repository.bannerResult
        this.listPengajuan = _repository.pengajuanResult
        this.progressBar = _repository.progressBar
    }

    fun daftar(case: String, akun: Akun) {
        viewModelScope.launch {
            try {
                _repository.daftarAkun(case, akun)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> _repository.dataResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[IO] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                    is HttpException -> {
                        _repository.dataResult
                            .postValue(
                                ResultOfNetwork.Failure(
                                    "[HTTP] error ${throwable.message} please retry",
                                    throwable
                                )
                            )
                    }
                    else -> _repository.dataResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[Unknown] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                }
            }
        }
    }

    fun login(case: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _repository.loginAkun(case, email, password)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> _repository.loginResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[IO] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                    is HttpException -> {
                        _repository.loginResult
                            .postValue(
                                ResultOfNetwork.Failure(
                                    "[HTTP] error ${throwable.message} please retry",
                                    throwable
                                )
                            )
                    }
                    else -> _repository.loginResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[Unknown] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                }
            }
        }
    }

    fun submitData(case: String, noKtp: String, data: DataPengajuan) {
        viewModelScope.launch {
            try {
                _repository.progressBar.postValue(true)
                _repository.pengajuan(case, noKtp, data)
            } catch (throwable: Throwable) {
                _repository.progressBar.postValue(false)
                when (throwable) {
                    is IOException -> _repository.dataResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[IO] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                    is HttpException -> {
                        _repository.dataResult
                            .postValue(
                                ResultOfNetwork.Failure(
                                    "[HTTP] error ${throwable.message} please retry",
                                    throwable
                                )
                            )
                    }
                    else -> _repository.dataResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[Unknown] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                }
            }
        }
    }

    fun bannerData(case: String) {
        viewModelScope.launch {
            try {
                _repository.banner(case)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> _repository.bannerResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[IO] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                    is HttpException -> {
                        _repository.bannerResult
                            .postValue(
                                ResultOfNetwork.Failure(
                                    "[HTTP] error ${throwable.message} please retry",
                                    throwable
                                )
                            )
                    }
                    else -> _repository.bannerResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[Unknown] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                }
            }
        }
    }

    fun listData(case: String, noKtp: String) {
        viewModelScope.launch {
            try {
                _repository.listPengajuan(case, noKtp)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> _repository.pengajuanResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[IO] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                    is HttpException -> {
                        _repository.pengajuanResult
                            .postValue(
                                ResultOfNetwork.Failure(
                                    "[HTTP] error ${throwable.message} please retry",
                                    throwable
                                )
                            )
                    }
                    else -> _repository.pengajuanResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[Unknown] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                }
            }
        }
    }

    fun uploadBukti(case: String, id: String, imageBase: String, imageName: String) {
        viewModelScope.launch {
            try {
                _repository.progressBar.postValue(true)
                _repository.uploadBukti(case, id, imageBase, imageName)
            } catch (throwable: Throwable) {
                _repository.progressBar.postValue(false)
                when (throwable) {
                    is IOException -> _repository.dataResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[IO] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                    is HttpException -> {
                        _repository.dataResult
                            .postValue(
                                ResultOfNetwork.Failure(
                                    "[HTTP] error ${throwable.message} please retry",
                                    throwable
                                )
                            )
                    }
                    else -> _repository.dataResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[Unknown] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                }
            }
        }
    }

    fun dataPengajuan(case: String, id: String) {
        viewModelScope.launch {
            try {
                _repository.dataPengajuan(case, id)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> _repository.dataPengajuanResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[IO] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                    is HttpException -> {
                        _repository.dataPengajuanResult
                            .postValue(
                                ResultOfNetwork.Failure(
                                    "[HTTP] error ${throwable.message} please retry",
                                    throwable
                                )
                            )
                    }
                    else -> _repository.dataPengajuanResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[Unknown] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                }
            }
        }
    }

    fun getListProvinsi(case: String) {
        viewModelScope.launch {
            try {
                _repository.listProvinsi(case)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> _repository.provinsiResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[IO] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                    is HttpException -> {
                        _repository.provinsiResult
                            .postValue(
                                ResultOfNetwork.Failure(
                                    "[HTTP] error ${throwable.message} please retry",
                                    throwable
                                )
                            )
                    }
                    else -> _repository.provinsiResult
                        .postValue(
                            ResultOfNetwork.Failure(
                                "[Unknown] error ${throwable.message} please retry",
                                throwable
                            )
                        )
                }
            }
        }
    }
}