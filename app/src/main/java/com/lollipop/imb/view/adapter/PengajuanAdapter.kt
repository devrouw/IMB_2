package com.lollipop.imb.view.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.imb.R
import com.lollipop.imb.databinding.ItemListPengajuanBinding
import com.lollipop.imb.service.model.PengajuanResult

class PengajuanAdapter : RecyclerView.Adapter<PengajuanAdapter.ViewHolder>() {
    private var _items: MutableList<PengajuanResult> = ArrayList()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setList(list: List<PengajuanResult>?) {
        if (list == null) return
        this._items.clear()
        this._items.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PengajuanAdapter.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_pengajuan, parent, false)
    )

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val item = _items[position]
            with(binding) {
                idPengajuan.text = "- ${item.id}"
            }
        }
    }

    override fun getItemCount() = _items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemListPengajuanBinding.bind(view)
    }

    interface OnItemClickCallback {
        fun onItemClick(item: PengajuanResult)
    }
}