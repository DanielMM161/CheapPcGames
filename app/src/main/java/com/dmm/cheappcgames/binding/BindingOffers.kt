package com.dmm.cheappcgames.binding

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import coil.load
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.data.StoreItem
import com.dmm.cheappcgames.ui.OffersViewModel
import com.google.android.material.button.MaterialButton

object BindingOffers {

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            imgView.load(imgUri) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_baseline_broken_image_24)
            }
        }
    }

}