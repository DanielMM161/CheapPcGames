package com.dmm.cheappcgames.db

import androidx.room.TypeConverter
import com.dmm.cheappcgames.data.Images
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ImagesConverter {

    @TypeConverter
    fun fromImage(image: Images): String {
        return Gson().toJson(image)
    }

    @TypeConverter
    fun stringtoImage(imageStr: String): Images {
        val notesType = object : TypeToken<Images>() {}.type
        return Gson().fromJson<Images>(imageStr, notesType)
    }
}