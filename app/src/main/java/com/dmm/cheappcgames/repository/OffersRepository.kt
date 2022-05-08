package com.dmm.cheappcgames.repository

import com.dmm.cheappcgames.data.Offer
import com.dmm.cheappcgames.db.CheapPcDataBase
import com.dmm.cheappcgames.network.RetrofitInstance

class OffersRepository(
    val db: CheapPcDataBase
) {
    suspend fun getDeals(pageNumb: Int) =
        RetrofitInstance.api.getDeals(pageNumb)

    suspend fun getFilteredDeals(pageNumb: Int, storesId: String) =
        RetrofitInstance.api.getFilteredDeals(pageNumb, storesId)

    suspend fun getDealsByTitle(pageNumb: Int, title: String) =
        RetrofitInstance.api.getDealsByTitle(pageNumb, title)

    suspend fun getGameById(id: Int) =
        RetrofitInstance.api.getGameById(id)

    suspend fun getStores() =
        RetrofitInstance.api.getStores()

    suspend fun insertGame(game: Offer) = db.storeDao().insertGame(game)

    suspend fun deleteGame(game: Offer) = db.storeDao().deleteGame(game)

    fun getFavoritesOffers() = db.storeDao().getFavoritesOffers()
}