package com.dmm.cheappcgames.network

import com.dmm.cheappcgames.data.GameItem
import com.dmm.cheappcgames.data.Offer
import com.dmm.cheappcgames.data.StoreItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OffersApi {

    @GET("deals")
    suspend fun getDeals(
        @Query("pageNumber")
        pageNumber: Int = 0
    ) : Response<List<Offer>>

    @GET("deals")
    suspend fun getFilteredDeals(
        @Query("pageNumber")
        pageNumber: Int = 0,
        @Query("storeID")
        storesId: String
    ) : Response<List<Offer>>

    @GET("deals")
    suspend fun getDealsByTitle(
        @Query("pageNumber")
        pageNumber: Int = 0,
        @Query("title")
        title: String ?= null
    ) : Response<List<Offer>>

    @GET("games")
    suspend fun getGameById(
        @Query("id")
        id: Int = 1
    ) : Response<GameItem>

    @GET("stores")
    suspend fun getStores() : Response<List<StoreItem>>
}