package com.lollipop.imb.view.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.lollipop.imb.R
import com.lollipop.imb.util.Constant
import com.lollipop.imb.util.ResultOfNetwork
import com.lollipop.imb.databinding.ActivityListPengajuanBinding
import com.lollipop.imb.service.model.LoginResult
import com.lollipop.imb.service.model.PengajuanResult
import com.lollipop.imb.util.ImageUtils
import com.lollipop.imb.view.adapter.PengajuanAdapter
import com.lollipop.imb.viewModel.DataStoreViewModel
import com.lollipop.imb.viewModel.MainViewModel
import com.shreyaspatil.MaterialDialog.MaterialDialog
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File

class ListPengajuanActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityListPengajuanBinding

    private val PERMISSION_CODE = 1001
    val REQUEST_CODE = 1002

    private lateinit var _viewModelDataStore: DataStoreViewModel
    private lateinit var _viewModel: MainViewModel
    private lateinit var _adapter: PengajuanAdapter

    private var _id = ""
    private var _imageName = ""
    private var _imageBase = ""
    private var _nik = ""
    private var _url = ""
    private var _filename = ""

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
        val loadingDialog = LoadingDialog(this)
        _viewModel.progressBar.observe(this@ListPengajuanActivity, {
            if (it) {
                loadingDialog.startLoadingDialog()
            } else {
                loadingDialog.dismiss()
            }
        })

        _viewModelDataStore.userData.observe(this, {
            _viewModel.listData("list_pengajuan",it[0])
            _nik = it[0]
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

        _viewModel.dataPengajuan.observe(this, {
            when (it) {
                is ResultOfNetwork.Success -> {
                    when (it.value.code) {
                        Constant.Network.REQUEST_NOT_FOUND -> {
                            Toast.makeText(this@ListPengajuanActivity, "Data tidak ada", Toast.LENGTH_SHORT)
                                .show()
                        }
                        Constant.Network.REQUEST_SUCCESS -> {
                            if(it.value.data?.get(0)?.sertifikat_IMB != null){
                                _filename = it.value.data[0].sertifikat_IMB.toString().replace(" ","_")
                                _url =
                                    "http://imb.sha-dev.com/assets/pengajuan/$_filename"
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                                        requestPermissions(permissions, PERMISSION_CODE)
                                    } else{
                                        download(_url, _filename)
                                    }
                                }else{
                                    download(_url, _filename)
                                }
                            }else{
                                Toast.makeText(this@ListPengajuanActivity, "IMB Belum tersedia, harap menunggu", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
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
                val mDialog = MaterialDialog.Builder(this@ListPengajuanActivity)
                    .setTitle("Silakan Lakukan Pembayaran")
                    .setMessage(getString(R.string.txt_pembayaran))
                    .setCancelable(true)
                    .setPositiveButton(
                        "Upload Bukti Transfer"
                    ) { dialogInterface, which ->
                        //check permission at runtime
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                                requestPermissions(permissions, PERMISSION_CODE)
                            } else{
                                _id = item.id.toString()
                                openGalleryForImage()
                            }
                        }else{
                            _id = item.id.toString()
                            openGalleryForImage();
                        }
                    }
                    .build()

                // Show Dialog

                // Show Dialog
                mDialog.show()
            }

            override fun onUploadClick(item: PengajuanResult) {
                //check permission at runtime
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                        requestPermissions(permissions, PERMISSION_CODE)
                    } else{
                        _id = item.id.toString()
                        openGalleryForImage()
                    }
                }else{
                    _id = item.id.toString()
                    openGalleryForImage();
                }
            }

            override fun onDownloadClick(item: PengajuanResult) {
                item.id?.let { _viewModel.dataPengajuan("data_pengajuan", it) }
            }

        })
        _binding.rvContent.adapter = _adapter
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            val filePath = data?.data?.let { getRealPathFromURI(it) }
            val bitmap: Bitmap = ImageUtils.getInstant().getCompressedBitmap(filePath)
            val output1 = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, output1)
            val byte_arr1 = output1.toByteArray()
            _imageBase = Base64.encodeToString(byte_arr1, Base64.DEFAULT)
            if (filePath != null) {
                _imageName = File(data?.data?.path).name
                Toast.makeText(this,_imageName,Toast.LENGTH_LONG).show()
            }
            uploadImage()
        }
    }

    fun getRealPathFromURI(contentUri: Uri): String? {
        var path: String? = null
        val proj = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor: Cursor? = contentUri.let {
            getApplicationContext().getContentResolver()
                .query(it, proj, null, null, null)
        }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                path = cursor.getString(column_index)
            }
        }
        if (cursor != null) {
            cursor.close()
        }
        return path
    }

    private fun uploadImage(){
        _viewModel.uploadBukti("upload_bukti",_id,_imageBase,_imageName)
        _viewModel.uploadBukti.observe(this, {
            when (it) {
                is ResultOfNetwork.Success -> {
                    when (it.value.code) {
                        Constant.Network.REQUEST_NOT_FOUND -> {
                            Toast.makeText(this@ListPengajuanActivity, "Data tidak ada", Toast.LENGTH_SHORT)
                                .show()
                        }
                        Constant.Network.REQUEST_SUCCESS -> {
                            Toast.makeText(this,"Berhasil Upload Bukti Trf!",Toast.LENGTH_LONG).show()
                            _viewModel.listData("list_pengajuan",_nik)
                        }
                    }
                }
                is ResultOfNetwork.Failure -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun download(url: String, fileName: String) {
        PRDownloader.download(
            url,
            baseContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath,
            fileName
        )
            .build()
            .setOnProgressListener {
                // Update the progress
                _binding.progressBar.progress = 0
                _binding.progressBar.max = it.totalBytes.toInt()
                _binding.progressBar.progress = it.currentBytes.toInt()
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    // Update the progress bar to show the completeness
                    _binding.progressBar.max = 100
                    _binding.progressBar.progress = 100

                    Toast.makeText(baseContext,"$fileName berhasil di download di ${baseContext.getExternalFilesDir(
                        Environment.DIRECTORY_DOWNLOADS)?.absolutePath}", Toast.LENGTH_LONG).show()
                }

                override fun onError(error: com.downloader.Error?) {
                    Toast.makeText(baseContext, "Failed to download the $url", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    download(_url,_filename)
                }else{
                    Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isSuccessNetworkCallback(code: Int, data: List<PengajuanResult>?) {
        when (code) {
            Constant.Network.REQUEST_NOT_FOUND -> {
                Toast.makeText(this@ListPengajuanActivity, "Data tidak ada", Toast.LENGTH_SHORT)
                    .show()
            }
            Constant.Network.REQUEST_SUCCESS -> {
                _adapter.removeAllData()
                _adapter.setList(data)
            }
        }
    }

}