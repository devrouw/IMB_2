package com.lollipop.imb.view.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lollipop.imb.R
import com.lollipop.imb.util.Constant
import com.lollipop.imb.util.ResultOfNetwork
import com.lollipop.imb.databinding.ActivityRegisterBinding
import com.lollipop.imb.service.model.Akun
import com.lollipop.imb.service.model.ProvinsiResult
import com.lollipop.imb.viewModel.MainViewModel

class DaftarActivity : AppCompatActivity() {
    private lateinit var _binding : ActivityRegisterBinding

    private lateinit var _viewModel : MainViewModel

    private var _idProvinsi = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        initializeViewModel()

        with(_binding){
            etProvinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // You can define your actions as you want
                }
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    val selectedObject = etProvinsi.selectedItem as ProvinsiResult
                    _idProvinsi = selectedObject.id
                }
            }

            btRegister.setOnClickListener {
                _viewModel.daftar("daftar",Akun(
                    "${etNoKtp.text}",
                    "${etNamaLengkap.text}",
                    _idProvinsi,
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

        _viewModel.listProvinsi.observe(this,{ result ->
            when(result){
                is ResultOfNetwork.Success -> {
                    when (result.value.code) {
                        Constant.Network.REQUEST_NOT_FOUND -> {
                            Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
                        }
                        Constant.Network.REQUEST_SUCCESS -> {
                            val list = ArrayList<ProvinsiResult>()
                            result.value.data?.forEach {
                                list.add(it)
                            }

                            val _adapter = ArrayAdapter(this,
                                R.layout.spinner_list,list)
                            _binding.etProvinsi.adapter = _adapter
                        }
                    }
                }
                is ResultOfNetwork.Failure -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
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
        _viewModel.getListProvinsi("provinsi")
    }


}