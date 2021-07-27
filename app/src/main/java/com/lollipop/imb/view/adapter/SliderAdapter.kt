package com.lollipop.imb.view.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.lollipop.imb.util.Constant
import com.lollipop.imb.R
import com.lollipop.imb.databinding.ImageLayoutSliderItemBinding
import com.lollipop.imb.service.model.SliderResult
import com.lollipop.imb.util.GlideUtil
import com.smarteist.autoimageslider.SliderViewAdapter


class SliderAdapter: SliderViewAdapter<SliderAdapter.ViewHolder>() {
    private var _items: MutableList<SliderResult> = ArrayList()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setList(list: List<SliderResult>?) {
        if (list == null) return
        this._items.clear()
        this._items.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val item = _items[position]
            with(binding) {
                GlideUtil.buildDefaultGlide(itemView.context,
                    "${Constant.URL.IMAGE_URL}${item.image_url}",ivAutoImageSlider,GlideUtil.CENTER_CROP,R.drawable.ic_baseline_image_not_supported)
            }
        }
    }


    class ViewHolder(view: View) : SliderViewAdapter.ViewHolder(view) {
        val binding = ImageLayoutSliderItemBinding.bind(view)
    }

    interface OnItemClickCallback {
        fun onItemClick(item: SliderResult)
    }

    override fun getCount() = _items.size

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder = ViewHolder(
        LayoutInflater.from(parent?.context)
            .inflate(R.layout.image_layout_slider_item, parent, false)
    )


}
