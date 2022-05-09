package com.dmm.cheappcgames.data

import java.io.Serializable

data class GameItem(
    val storeId: String,
    val cheapestPriceEver: CheapestPriceEver,
    val deals: List<Deal>,
    val info: Info
) : Serializable