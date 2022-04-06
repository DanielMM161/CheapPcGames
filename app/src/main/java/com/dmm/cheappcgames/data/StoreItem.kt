package com.dmm.cheappcgames.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dmm.cheappcgames.db.ImagesConverter

@Entity( tableName = "stores")
data class StoreItem(
    @PrimaryKey
    @ColumnInfo(name = "store_id")
    val storeID: String,
    @ColumnInfo(name = "images")
    val images: Images,
    @ColumnInfo(name = "is_active")
    val isActive: Int,
    @ColumnInfo(name = "store_name")
    val storeName: String
)