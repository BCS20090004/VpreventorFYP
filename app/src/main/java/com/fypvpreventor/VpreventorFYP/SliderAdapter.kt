package com.fypvpreventor.VpreventorFYP

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(private val images: IntArray) :
    SliderViewAdapter<SliderAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.slider_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(viewHolder: Holder, position: Int) {
        viewHolder.imageView.setImageResource(images[position])
    }

    override fun getCount(): Int {
        return images.size
    }

    inner class Holder(itemView: View) :
        SliderViewAdapter.ViewHolder(itemView) {

        val imageView: ImageView = itemView.findViewById(R.id.image_view)
    }
}
