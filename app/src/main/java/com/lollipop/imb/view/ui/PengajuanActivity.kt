package com.lollipop.imb.view.ui

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lollipop.e_lapor.util.Constant
import com.lollipop.e_lapor.util.ResultOfNetwork
import com.lollipop.imb.databinding.FragmentDataAdministrasiBinding
import com.lollipop.imb.service.model.DataPengajuan
import com.lollipop.imb.viewModel.DataStoreViewModel
import com.lollipop.imb.viewModel.MainViewModel
import timber.log.Timber
import java.io.File


@RequiresApi(Build.VERSION_CODES.M)
class PengajuanActivity : AppCompatActivity() {

    val CODE_EKTP = 100
    val CODE_PBB = 101
    val CODE_PENGUASAAN = 102
    val CODE_SONDIR = 103
    val CODE_KAJI = 104
    val CODE_ANDALALIN = 105
    val CODE_BANGUNAN = 106

    private val PERMISSION_CODE = 1001
    private lateinit var _binding: FragmentDataAdministrasiBinding

    private lateinit var _viewModel: MainViewModel
    private lateinit var _viewModelDataStore: DataStoreViewModel
    private lateinit var _scanEktp: File
    private lateinit var _buktiPbb: File
    private lateinit var _scanImtn: File
    private lateinit var _dataSondir: File
    private lateinit var _kajian: File
    private lateinit var _andalalin: File
    private lateinit var _gambar: File

    private var _nik = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentDataAdministrasiBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        initializeViewModel()

        with(_binding){
            btUnggahEktp.setOnClickListener {
                //check permission at runtime
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                        requestPermissions(permissions, PERMISSION_CODE)
                    } else{
                        openGalleryForPdf(CODE_EKTP)
                    }
                }else{
                    openGalleryForPdf(CODE_EKTP);
                }
            }

            btUnggahPbb.setOnClickListener {
                //check permission at runtime
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                        requestPermissions(permissions, PERMISSION_CODE)
                    } else{
                        openGalleryForPdf(CODE_PBB)
                    }
                }else{
                    openGalleryForPdf(CODE_PBB);
                }
            }

            btUnggahImtn.setOnClickListener {
                //check permission at runtime
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                        requestPermissions(permissions, PERMISSION_CODE)
                    } else{
                        openGalleryForPdf(CODE_PENGUASAAN)
                    }
                }else{
                    openGalleryForPdf(CODE_PENGUASAAN);
                }
            }

            btUnggahDataSondir.setOnClickListener {
                //check permission at runtime
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                        requestPermissions(permissions, PERMISSION_CODE)
                    } else{
                        openGalleryForPdf(CODE_SONDIR)
                    }
                }else{
                    openGalleryForPdf(CODE_SONDIR);
                }
            }

            btUnggahKajian.setOnClickListener {
                //check permission at runtime
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                        requestPermissions(permissions, PERMISSION_CODE)
                    } else{
                        openGalleryForPdf(CODE_KAJI)
                    }
                }else{
                    openGalleryForPdf(CODE_KAJI);
                }
            }

            btUnggahAndalalin.setOnClickListener {
                //check permission at runtime
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                        requestPermissions(permissions, PERMISSION_CODE)
                    } else{
                        openGalleryForPdf(CODE_ANDALALIN)
                    }
                }else{
                    openGalleryForPdf(CODE_ANDALALIN);
                }
            }

            btUnggahGambar.setOnClickListener {
                //check permission at runtime
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                        requestPermissions(permissions, PERMISSION_CODE)
                    } else{
                        openGalleryForPdf(CODE_BANGUNAN)
                    }
                }else{
                    openGalleryForPdf(CODE_BANGUNAN);
                }
            }

            btSubmit.setOnClickListener {
                _viewModel.submitData("input_pengajuan",_nik, DataPengajuan(
                    _scanEktp,
                    _buktiPbb,
                    _scanImtn,
                    _dataSondir,
                    _kajian,
                    _andalalin,
                    _gambar
                ))
            }
        }

        observableLiveData()
    }

    private fun initializeViewModel() {
        _viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        _viewModelDataStore = ViewModelProvider(this).get(DataStoreViewModel::class.java)
    }

    private fun observableLiveData() {
        _viewModelDataStore.userData.observe(this, {
            _nik = it[0]
        })
        _viewModel.daftarAkun.observe(this@PengajuanActivity, { result ->
            when (result) {
                is ResultOfNetwork.Success -> {
                    result.value.code?.let { isSuccessNetworkCallback(it) }
                }
                is ResultOfNetwork.Failure -> {
                    Toast.makeText(this@PengajuanActivity, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        val loadingDialog = LoadingDialog(this)
        _viewModel.progressBar.observe(this@PengajuanActivity, {
            Timber.d("progress bar $it")
            if (it) {
                loadingDialog.startLoadingDialog()
            } else {
                loadingDialog.dismiss()
            }
        })
    }

    private fun isSuccessNetworkCallback(code: Int) {
        when (code) {
            Constant.Network.REQUEST_NOT_FOUND -> {
                Toast.makeText(this@PengajuanActivity, "Gagal mengirim data", Toast.LENGTH_SHORT)
                    .show()
            }
            Constant.Network.REQUEST_SUCCESS -> {
                Toast.makeText(this@PengajuanActivity, "Berhasil mengirim data", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    private fun openGalleryForPdf(code: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        startActivityForResult(Intent.createChooser(intent,"Pilih PDF"), code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val filePath = data?.data?.let { getRealPathFromURI(this, it) }
            val _imageName = File(data?.data!!.path).name
            val file = File(filePath)
            when(requestCode){
                CODE_EKTP -> {
                    _scanEktp = file
                    _binding.tvEktp.text = _imageName
                }
                CODE_PBB -> {
                    _buktiPbb = file
                    _binding.tvBuktiPbb.text = _imageName
                }
                CODE_PENGUASAAN -> {
                    _scanImtn = file
                    _binding.tvImtn.text = _imageName
                }
                CODE_SONDIR -> {
                    _dataSondir = file
                    _binding.tvDataSondir.text = _imageName
                }
                CODE_KAJI -> {
                    _kajian = file
                    _binding.tvKajianLingkungan.text = _imageName
                }
                CODE_ANDALALIN -> {
                    _andalalin = file
                    _binding.tvAndalalin.text = _imageName
                }
                CODE_BANGUNAN -> {
                    _gambar = file
                    _binding.tvGambarBangunan.text = _imageName
                }
            }
        }
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
                    openGalleryForPdf(CODE_EKTP)
                }else{
                    Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun getRealPathFromURI(context: Context, uri: Uri): String? {
        when {
            // DocumentProvider
            DocumentsContract.isDocumentUri(context, uri) -> {
                when {
                    // ExternalStorageProvider
                    isExternalStorageDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]
                        // This is for checking Main Memory
                        return if ("primary".equals(type, ignoreCase = true)) {
                            if (split.size > 1) {
                                Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                            } else {
                                Environment.getExternalStorageDirectory().toString() + "/"
                            }
                            // This is for checking SD Card
                        } else {
                            "storage" + "/" + docId.replace(":", "/")
                        }
                    }
                    isDownloadsDocument(uri) -> {
                        val fileName = getFilePath(context, uri)
                        if (fileName != null) {
                            return Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName
                        }
                        var id = DocumentsContract.getDocumentId(uri)
                        if (id.startsWith("raw:")) {
                            id = id.replaceFirst("raw:".toRegex(), "")
                            val file = File(id)
                            if (file.exists()) return id
                        }
                        val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                        return getDataColumn(context, contentUri, null, null)
                    }
                    isMediaDocument(uri) -> {
                        val docId = DocumentsContract.getDocumentId(uri)
                        val split = docId.split(":").toTypedArray()
                        val type = split[0]
                        var contentUri: Uri? = null
                        when (type) {
                            "image" -> {
                                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            }
                            "video" -> {
                                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            }
                            "audio" -> {
                                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                            }
                        }
                        val selection = "_id=?"
                        val selectionArgs = arrayOf(split[1])
                        return getDataColumn(context, contentUri, selection, selectionArgs)
                    }
                }
            }
            "content".equals(uri.scheme, ignoreCase = true) -> {
                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(context, uri, null, null)
            }
            "file".equals(uri.scheme, ignoreCase = true) -> {
                return uri.path
            }
        }
        return null
    }

    fun getDataColumn(context: Context, uri: Uri?, selection: String?,
                      selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            if (uri == null) return null
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs,
                null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }


    fun getFilePath(context: Context, uri: Uri?): String? {
        var cursor: Cursor? = null
        val projection = arrayOf(
            MediaStore.MediaColumns.DISPLAY_NAME
        )
        try {
            if (uri == null) return null
            cursor = context.contentResolver.query(uri, projection, null, null,
                null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

}