package com.lollipop.imb.view.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.imb.R
import com.lollipop.imb.databinding.ItemListPengajuanBinding
import com.lollipop.imb.service.model.PengajuanResult
import com.shreyaspatil.MaterialDialog.MaterialDialog

class PengajuanAdapter : RecyclerView.Adapter<PengajuanAdapter.ViewHolder>() {
    private var _items: MutableList<PengajuanResult> = ArrayList()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setList(list: List<PengajuanResult>?) {
        if (list == null) return
        this._items.clear()
        this._items.addAll(list)
        notifyDataSetChanged()
    }

    fun removeAllData() {
        val size = _items.size
        this._items.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PengajuanAdapter.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_pengajuan, parent, false)
    )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val item = _items[position]
            with(binding) {
                idPengajuan.text = "- ${(position+1)}"
                when(item.role){
                    "0" -> {
                        btUpload.visibility = View.GONE
                        btDownload.visibility = View.GONE
                        tvStatusAjuan.text = "Dalam pengajuan"
                    }
                    "1" -> {
                        btUpload.visibility = View.GONE
                        btDownload.visibility = View.GONE
                        tvStatusAjuan.text = "Sedang di proses"
                    }
                    "2" -> {
                        btUpload.visibility = View.VISIBLE
                        btDownload.visibility = View.GONE
                        tvStatusAjuan.text = "Menunggu Pembayaran"
                        llAjuan.setOnClickListener {
                            onItemClickCallback.onItemClick(item)
                        }
                    }
                    "3" -> {
                        btUpload.visibility = View.GONE
                        btDownload.visibility = View.GONE
                        tvStatusAjuan.text = "Menunggu Konfirmasi"
                    }
                    "4" -> {
                        btUpload.visibility = View.GONE
                        btDownload.visibility = View.VISIBLE
                        tvStatusAjuan.text = "Pembayaran Diterima, IMB Terbit"
                    }

                }
                btUpload.setOnClickListener {
                    onItemClickCallback.onUploadClick(item)
                }
                btDownload.setOnClickListener {
                    onItemClickCallback.onDownloadClick(item)
                }
            }
        }
    }

    override fun getItemCount() = _items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemListPengajuanBinding.bind(view)
    }

    interface OnItemClickCallback {
        fun onItemClick(item: PengajuanResult)
        fun onUploadClick(item: PengajuanResult)
        fun onDownloadClick(item: PengajuanResult)
    }
}