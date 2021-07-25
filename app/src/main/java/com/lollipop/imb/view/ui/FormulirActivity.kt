package com.lollipop.imb.view.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import com.lollipop.imb.databinding.ActivityContohFormulirBinding
import io.reactivex.disposables.Disposables
import timber.log.Timber
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStreamReader

class FormulirActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityContohFormulirBinding

    private var disposable = Disposables.disposed()
    private lateinit var _file: File

    private val PERMISSION_CODE = 1001
    private var _url = ""
    private var _filename = ""

    companion object {
        private const val FILE_NAME = "TestPdf.pdf"
        private const val URL = "https://www.hq.nasa.gov/alsj/a17/A17_FlightPlan.pdf"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityContohFormulirBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        // Initialize PRDownloader with read and connection timeout
        val config = PRDownloaderConfig.newBuilder()
            .setReadTimeout(30000)
            .setConnectTimeout(30000)
            .build()
        PRDownloader.initialize(applicationContext, config)

        with(_binding){
            ivSuratImb.setOnClickListener {
                _url =
                    "http://imb.sha-dev.com/android/unggah/Surat_Kuasa_IMB.pdf"
                _filename = "Surat_Kuasa_IMB.pdf"
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
            }

            ivJaminanKonstruksi.setOnClickListener {
                _url = "http://imb.sha-dev.com/android/unggah/SURAT_PERNYATAAN_JAMINAN_KONSTRUKSI_PEMILIK.docx"
                _filename = "SURAT_PERNYATAAN_JAMINAN_KONSTRUKSI_PEMILIK.docx"
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
            }

            ivSuratPenanaman.setOnClickListener {
                _url = "http://imb.sha-dev.com/android/unggah/SP_PENANAMAN_POHON.docx"
                _filename = "SP_PENANAMAN_POHON.docx"
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
            }

            ivSuratKuasa.setOnClickListener {
                _url = "http://imb.sha-dev.com/android/unggah/SURAT_KUASA_MENGATASNAMAKAN_IMB.docx"
                _filename = "SURAT_KUASA_MENGATASNAMAKAN_IMB.docx"
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
            }

            ivJaminanPemilik.setOnClickListener {
                _url = "http://imb.sha-dev.com/android/unggah/SURAT_PERNYATAAN_JAMINAN_KONSTRUKSI_PEMILIK.docx"
                _filename = "SURAT_PERNYATAAN_JAMINAN_KONSTRUKSI_PEMILIK.docx"
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
            }

            ivMenjagaTurap.setOnClickListener {
                _url = "http://imb.sha-dev.com/android/unggah/surat_pernyataan_menjaga_turap.doc"
                _filename = "surat_pernyataan_menjaga_turap.doc"
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
            }
        }
    }

    private fun readFile(fileName: String) {
        return try {
            val reader = BufferedReader(InputStreamReader(baseContext.openFileInput(fileName)))
            reader.use {
                val sb = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    sb.append(line)
                }
                val text = sb.toString()
                Toast.makeText(baseContext, text, Toast.LENGTH_LONG).show()
            }
        } catch (ex: FileNotFoundException) {
            Toast.makeText(baseContext, "Error in reading the file $fileName", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun download(url: String, fileName: String) {
        PRDownloader.download(
            url,
            baseContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath,
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

                    // Read the file
//                    readFile(fileName)
                    Toast.makeText(baseContext,"$fileName berhasil di download di ${baseContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.absolutePath}", Toast.LENGTH_LONG).show()
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

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

}