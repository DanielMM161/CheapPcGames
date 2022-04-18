package com.dmm.cheappcgames.ui

import com.dmm.cheappcgames.data.Offer
import com.dmm.cheappcgames.db.CheapPcDataBase
import com.dmm.cheappcgames.network.RetrofitInstance

class OffersRepository(
    val db: CheapPcDataBase
) {
    suspend fun getOffers(pageNumb: Int, storeId: String ?= null) =
        RetrofitInstance.api.getDeals(pageNumb, storeId)

    suspend fun getSearchOffers(pageNumb: Int, storeId: String ?= null, title: String ?= null) =
        RetrofitInstance.api.getSearchDeals(pageNumb, storeId, title)

    suspend fun getGameById(id: Int) =
        RetrofitInstance.api.getGameById(id)

    suspend fun getSotres() =
        RetrofitInstance.api.getSotres()

    suspend fun insertGame(game: Offer) = db.storeDao().insertGame(game)

    suspend fun deleteGame(game: Offer) = db.storeDao().deleteGame(game)

    fun getFavoritesOffers() = db.storeDao().getFavoritesOffers()
}