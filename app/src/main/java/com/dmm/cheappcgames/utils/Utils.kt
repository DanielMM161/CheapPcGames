package com.dmm.cheappcgames.utils

import android.content.Context
import android.widget.Toast

class Utils {

    companion object {
        fun showToast(context: Context,message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

    }

}