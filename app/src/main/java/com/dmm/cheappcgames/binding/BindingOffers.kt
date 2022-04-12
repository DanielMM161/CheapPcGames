package com.dmm.cheappcgames.binding

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import coil.load
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.data.StoreItem
import com.dmm.cheappcgames.ui.OffersViewModel
import com.dmm.cheappcgames.utils.Constants.Companion.BASE_URL_LOGO

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

    @BindingAdapter("storeId", "stores")
    @JvmStatic
    fun bindLogo(imgView: ImageView, storeId: String, stores: List<StoreItem>) {
        val item: List<StoreItem> = stores.filter { storeItem ->  storeItem.storeID.equals(storeId)}
        if(item.isNotEmpty()) {
            val logoUrl = "${BASE_URL_LOGO}${item[0].images.logo}"
            logoUrl.let {
                val logoUri = logoUrl.toUri().buildUpon().scheme("https").build()
                imgView.load(logoUri) {
                    placeholder(R.drawable.loading_animation)
                    error(R.drawable.ic_baseline_broken_image_24)
                }
            }

        }
    }

}