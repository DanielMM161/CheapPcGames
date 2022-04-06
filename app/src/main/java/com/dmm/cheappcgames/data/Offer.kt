package com.dmm.cheappcgames.data

data class Offer(
    val dealID: String,
    val dealRating: String,
    val gameID: String,
    val internalName: String,
    val isOnSale: String,
    val lastChange: Int,
    val metacriticLink: String,
    val metacriticScore: String,
    val normalPrice: String,
    val releaseDate: Int,
    val salePrice: String,
    val savings: String,
    val steamAppID: Any,
    val steamRatingCount: String,
    val steamRatingPercent: String,
    val steamRatingText: Any,
    val storeID: String,
    val thumb: String,
    val title: String
)