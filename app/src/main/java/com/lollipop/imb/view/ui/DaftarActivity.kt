package com.lollipop.imb.view.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lollipop.imb.util.Constant
import com.lollipop.imb.util.ResultOfNetwork
import com.lollipop.imb.databinding.ActivityRegisterBinding
import com.lollipop.imb.service.model.Akun
import com.lollipop.imb.viewModel.MainViewModel

class DaftarActivity : AppCompatActivity() {
    private lateinit var _binding : ActivityRegisterBinding

    private lateinit var _viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        initializeViewModel()

        with(_binding){
            btRegister.setOnClickListener {
                _viewModel.daftar("daftar",Akun(
                    "${etNoKtp.text}",
                    "${etNamaLengkap.text}",
                    "${etProvinsi.text}",
                    "${etAlamat.text}",
                    "${etNoHp.text}",
                    "${etEmail.text}",
                    "${etPassword.text}"
                ))
            }
        }

        observableLiveData()
    }

    private fun observableLiveData() {
        _viewModel.daftarAkun.observe(this@DaftarActivity, { result ->
            when(result){
                is ResultOfNetwork.Success -> {
                    result.value.code?.let { isSuccessNetworkCallback(it) }
                }
                is ResultOfNetwork.Failure -> {
                    Toast.makeText(this@DaftarActivity, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun isSuccessNetworkCallback(code: Int) {
        when (code) {
            Constant.Network.REQUEST_NOT_FOUND -> {
                Toast.makeText(this@DaftarActivity, "Gagal mengirim data", Toast.LENGTH_SHORT).show()
            }
            Constant.Network.REQUEST_SUCCESS -> {
                Toast.makeText(this@DaftarActivity, "Berhasil mengirim data", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun initializeViewModel() {
        _viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }


}