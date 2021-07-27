package com.lollipop.imb.view.ui

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.lollipop.imb.util.Constant
import com.lollipop.imb.util.ResultOfNetwork
import com.lollipop.imb.databinding.ActivityMainBinding
import com.lollipop.imb.databinding.DialogConfirmationBinding
import com.lollipop.imb.service.model.SliderResult
import com.lollipop.imb.view.adapter.SliderAdapter
import com.lollipop.imb.viewModel.DataStoreViewModel
import com.lollipop.imb.viewModel.MainViewModel
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding

    private lateinit var _dialogBinding: DialogConfirmationBinding
    private lateinit var _viewModelDataStore: DataStoreViewModel
    private lateinit var _viewModel: MainViewModel
    private var _status = false

    private lateinit var _dialog: Dialog

    private lateinit var _adapter: SliderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        initializeViewModel()
        dialogBinding()
        initializeAdapter()

        with(_binding){
            btInfo.setOnClickListener {
                startActivity(Intent(this@MainActivity,InfoSyaratActivity::class.java))
            }
            btMasukKeluar.setOnClickListener {
                when(_status){
                    true -> {
                        _dialog.show()
                    }
                    false -> {
                        startActivity(Intent(this@MainActivity,LoginActivity::class.java))
                    }
                }
            }
            btPengajuan.setOnClickListener {
                when(_status){
                    true -> {
                        startActivity(Intent(this@MainActivity,ListPengajuanActivity::class.java))
                    }
                    false -> {
                        startActivity(Intent(this@MainActivity,LoginActivity::class.java))
                    }
                }
            }
            btDownload.setOnClickListener {
                startActivity(Intent(this@MainActivity,FormulirActivity::class.java))
            }

            _dialog = Dialog(this@MainActivity)
            _dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            _dialog.setContentView(_dialogBinding.root)
            _dialog.window?.setLayout((resources.displayMetrics.widthPixels), ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        observeDataStore()
    }

    private fun dialogBinding() {
        with(_dialogBinding) {
            btIya.setOnClickListener {
                _viewModelDataStore.setLoginStatus(false)
            }
            btTidak.setOnClickListener {
                _dialog.dismiss()
            }
        }
    }

    private fun initializeAdapter() {
        _adapter = SliderAdapter()
        _adapter.setOnItemClickCallback(object : SliderAdapter.OnItemClickCallback{
            override fun onItemClick(item: SliderResult) {
                Timber.d("item ${item.image_url}")
            }

        })
        _binding.imageSlider.setSliderAdapter(_adapter)
        _binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM)
        _binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
    }

    private fun observeDataStore() {
        _viewModelDataStore.loginStatus.observe(this, {
            _status = it
            if(!it){
                _binding.btMasukKeluar.text = "Masuk"
            }else{
                _binding.btMasukKeluar.text = "Keluar"
            }
        })

        _viewModel.bannerData("banner")

        _viewModel.bannerData.observe(this, {
            when (it) {
                is ResultOfNetwork.Success -> {
                    isSuccessNetworkCallback(
                        it.value.code,
                        it.value.data
                    )
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
        _dialogBinding = DialogConfirmationBinding.inflate(layoutInflater)
    }

    private fun isSuccessNetworkCallback(code: Int?, data: List<SliderResult>?) {
        when (code) {
            Constant.Network.REQUEST_NOT_FOUND -> {
                Toast.makeText(this, "Tidak ada koneksi internet",Toast.LENGTH_LONG).show()
            }
            Constant.Network.REQUEST_SUCCESS -> {
                data.let {
                    _adapter.setList(data)
                    _binding.imageSlider.startAutoCycle()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        observeDataStore()
    }


}