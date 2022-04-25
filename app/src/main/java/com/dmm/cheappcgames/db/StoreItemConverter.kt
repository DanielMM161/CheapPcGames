package com.dmm.cheappcgames.db

import androidx.room.TypeConverter
import com.dmm.cheappcgames.data.StoreItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StoreItemConverter {

    @TypeConverter
    fun storeItemtoString(storeItem: StoreItem): String {
        return Gson().toJson(storeItem)
    }

    @TypeConverter
    fun stringtoStoreItem(storeItemStr: String): StoreItem {
        val notesType = object : TypeToken<StoreItem>() {}.type
        return Gson().fromJson<StoreItem>(storeItemStr, notesType)
    }
}