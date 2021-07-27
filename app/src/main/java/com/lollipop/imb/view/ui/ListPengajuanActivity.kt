package com.lollipop.imb.view.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lollipop.e_lapor.util.Constant
import com.lollipop.e_lapor.util.ResultOfNetwork
import com.lollipop.imb.databinding.ActivityListPengajuanBinding
import com.lollipop.imb.service.model.LoginResult
import com.lollipop.imb.service.model.PengajuanResult
import com.lollipop.imb.view.adapter.PengajuanAdapter
import com.lollipop.imb.viewModel.DataStoreViewModel
import com.lollipop.imb.viewModel.MainViewModel
import timber.log.Timber

class ListPengajuanActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityListPengajuanBinding

    private lateinit var _viewModelDataStore: DataStoreViewModel
    private lateinit var _viewModel: MainViewModel
    private lateinit var _adapter: PengajuanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityListPengajuanBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        initializeViewModel()
        initializeAdapter()

        with(_binding){
            btAdd.setOnClickListener {
                startActivity(Intent(this@ListPengajuanActivity,PengajuanActivity::class.java))
            }
        }

        observableLiveData()
    }

    private fun observableLiveData() {
        _viewModelDataStore.userData.observe(this, {
            _viewModel.listData("list_pengajuan",it[0])
            Timber.d("nik adalah ${it[0]}")
        })

        _viewModel.listPengajuan.observe(this,{
            when (it) {
                is ResultOfNetwork.Success -> {
                    it.value.code?.let { it1 ->
                        isSuccessNetworkCallback(
                            it1,
                            it.value.data
                        )
                    }
                }
                is ResultOfNetwork.Failure -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initializeViewModel() {
        _viewModelDataStore = ViewModelProvider(this).get(DataStoreViewModel::class.java)
        _viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun initializeAdapter() {
        _binding.rvContent.layoutManager = LinearLayoutManager(this)
        _adapter = PengajuanAdapter()
        _adapter.setOnItemClickCallback(object : PengajuanAdapter.OnItemClickCallback{
            override fun onItemClick(item: PengajuanResult) {
                Timber.d("lihat data ${item.id}")
            }

        })
        _binding.rvContent.adapter = _adapter
    }

    private fun isSuccessNetworkCallback(code: Int, data: List<PengajuanResult>?) {
        when (code) {
            Constant.Network.REQUEST_NOT_FOUND -> {
                Toast.makeText(this@ListPengajuanActivity, "Data tidak ada", Toast.LENGTH_SHORT)
                    .show()
            }
            Constant.Network.REQUEST_SUCCESS -> {
                _adapter.setList(data)
            }
        }
    }

}