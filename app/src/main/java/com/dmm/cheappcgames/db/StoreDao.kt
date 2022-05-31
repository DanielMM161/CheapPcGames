package com.dmm.cheappcgames.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dmm.cheappcgames.data.Offer
import com.dmm.cheappcgames.resource.Resource
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGame(game: Offer)

    @Delete
    suspend fun deleteGame(game: Offer)

    @Query("SELECT * FROM offers")
    fun getFavoritesOffers(): Flow<List<Offer>>

}