package com.lollipop.imb.view.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kofigyan.stateprogressbar.StateProgressBar
import com.lollipop.imb.R
import com.lollipop.imb.databinding.FragmentDataTanahBinding
import com.lollipop.imb.view.ui.PengajuanActivity
import com.lollipop.imb.viewModel.MainViewModel

@RequiresApi(Build.VERSION_CODES.M)
class DataTanahFragment : Fragment() {
    private var _bindingFragment: FragmentDataTanahBinding? = null
    private val _binding get() = _bindingFragment!!

    private lateinit var _viewModel: MainViewModel
    private lateinit var _mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingFragment = FragmentDataTanahBinding.inflate(inflater,container,false)
        initializeViewModel()
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnPrev = view.rootView.findViewById<FloatingActionButton>(R.id.btn_prev)
        val btnNext = view.rootView.findViewById<FloatingActionButton>(R.id.btn_next)

        btnNext.setOnClickListener {
            val _dataAdministrasiFragment = DataAdministrasiFragment()
//            (activity as PengajuanActivity).nextFragment(StateProgressBar.StateNumber.FIVE)
//            (activity as PengajuanActivity).replaceFragment(_dataAdministrasiFragment)
        }

        btnPrev.setOnClickListener {
            val _jenisBangunanFragment = JenisBangunanFragment()
//            (activity as PengajuanActivity).nextFragment(StateProgressBar.StateNumber.THREE)
//            (activity as PengajuanActivity).replaceFragment(_jenisBangunanFragment)
        }

        observableLiveData()
    }

    private fun observableLiveData() {

    }

    private fun initializeViewModel() {
        _viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }
}