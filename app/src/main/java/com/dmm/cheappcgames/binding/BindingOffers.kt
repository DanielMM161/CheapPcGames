package com.dmm.cheappcgames.binding

import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import coil.load
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.utils.Constants.Companion.BASE_URL_LOGO
import kotlin.math.truncate

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

    @BindingAdapter("logoUrl")
    @JvmStatic
    fun bindLogo(imgView: ImageView, logoUrl: String?) {
        val fullLogoUrl = "${BASE_URL_LOGO}${logoUrl}"
        fullLogoUrl.let {
            val logoUri = it.toUri().buildUpon().scheme("https").build()
            imgView.load(logoUri) {
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_baseline_broken_image_24)
            }
        }
    }

    @BindingAdapter("textSaving")
    @JvmStatic
    fun bindSaving(textView: TextView, text: String) {
        val savings  = truncate(text.toDouble()).toInt()
        textView.text = "$savings%"
    }

    @BindingAdapter("textNormalPrice")
    @JvmStatic
    fun bindTextNormalPrice(textView: TextView, text: String) {
        val spannable = SpannableString("$${text}")
        spannable.setSpan(StrikethroughSpan(), 0, spannable.length, 0)
        textView.text = spannable
    }
}