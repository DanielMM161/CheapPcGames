package com.dmm.cheappcgames.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "offers")
data class Offer(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val dealID: String,
    @ColumnInfo(name = "deal_rating")
    val dealRating: String,
    @ColumnInfo(name = "game_id")
    val gameID: String,
    @ColumnInfo(name = "internal_name")
    val internalName: String,
    @ColumnInfo(name = "is_on_sale")
    val isOnSale: String,
    @ColumnInfo(name = "last_change")
    val lastChange: Int,
    @ColumnInfo(name = "metacitic_link")
    val metacriticLink: String? = "",
    @ColumnInfo(name = "metacritic_score")
    val metacriticScore: String? = "",
    @ColumnInfo(name = "normal_price")
    val normalPrice: String,
    @ColumnInfo(name = "release_date")
    val releaseDate: Int,
    @ColumnInfo(name = "saled_price")
    val salePrice: String,
    @ColumnInfo(name = "savings")
    val savings: String,
    @ColumnInfo(name = "steam_app_id")
    val steamAppID: String? = "",
    @ColumnInfo(name = "steam_rating_count")
    val steamRatingCount: String? = "",
    @ColumnInfo(name = "steam_rating_percent")
    val steamRatingPercent: String? = "",
    @ColumnInfo(name = "steam_rating_text")
    val steamRatingText: String? = "",
    @ColumnInfo(name = "store_id")
    val storeID: String,
    @ColumnInfo(name = "thumb")
    val thumb: String,
    @ColumnInfo(name = "title")
    val title: String
)