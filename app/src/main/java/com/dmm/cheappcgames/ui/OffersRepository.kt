package com.dmm.cheappcgames.ui

import com.dmm.cheappcgames.data.StoreItem
import com.dmm.cheappcgames.db.CheapPcDataBase
import com.dmm.cheappcgames.network.OffersApi
import com.dmm.cheappcgames.network.RetrofitInstance

class OffersRepository(
    val db: CheapPcDataBase
) {
    suspend fun getOffers(pageNumb: Int, storeId: String ?= null) =
        RetrofitInstance.api.getDeals(pageNumb, storeId)

    suspend fun getSearchOffers(pageNumb: Int, storeId: String ?= null, title: String ?= null) =
        RetrofitInstance.api.getSearchDeals(pageNumb, storeId, title)

    suspend fun getSotres() =
        RetrofitInstance.api.getSotres()

    suspend fun insertStores(stores: List<StoreItem>) = db.storeDao().insertAll(stores)
}