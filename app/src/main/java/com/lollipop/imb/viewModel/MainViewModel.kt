package com.lollipop.imb.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lollipop.e_lapor.repository.MainRepository
import com.lollipop.e_lapor.util.ResultOfNetwork
import com.lollipop.imb.service.model.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainViewModel : ViewModel() {
    private val _repository = MainRepository()

    val daftarAkun: LiveData<ResultOfNetwork<KirimData>>
    val loginAkun: LiveData<ResultOfNetwork<LoginData>>
    val bannerData: LiveData<ResultOfNetwork<SliderData>>
    val listPengajuan: LiveData<ResultOfNetwork<PengajuanData>>
    val progressBar: LiveData<Boolean>

    init {
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
}