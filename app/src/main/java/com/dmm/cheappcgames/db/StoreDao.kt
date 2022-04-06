package com.dmm.cheappcgames.db

import androidx.room.*
import com.dmm.cheappcgames.data.StoreItem

@Dao
interface StoreDao {

    @Query("Select * FROM stores")
    fun getStores(): List<StoreItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(stores: List<StoreItem>)

}