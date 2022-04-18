package com.dmm.cheappcgames.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dmm.cheappcgames.data.Offer

@Dao
interface StoreDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGame(game: Offer)

    @Query("SELECT * FROM offers")
    fun getFavoritesOffers(): LiveData<List<Offer>>

}