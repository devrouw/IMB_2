package com.lollipop.imb.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lollipop.imb.databinding.ActivityInfoSyaratImbBinding

class InfoSyaratActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityInfoSyaratImbBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityInfoSyaratImbBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    }

}