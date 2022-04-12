package com.dmm.cheappcgames.network

import com.dmm.cheappcgames.data.Offer
import com.dmm.cheappcgames.data.StoreItem
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OffersApi {

    @GET("deals")
    suspend fun getDeals(
        @Query("pageNumber")
        pageNumber: Int = 1,
        @Query("storeID")
        storeId: String ?= null
    ) : Response<List<Offer>>

    @GET("deals")
    suspend fun getSearchDeals(
        @Query("pageNumber")
        pageNumber: Int = 0,
        @Query("storeID")
        storeId: String ?= null,
        @Query("title")
        title: String ?= null
    ) : Response<List<Offer>>

    @GET("stores")
    suspend fun getSotres() : Response<List<StoreItem>>
}