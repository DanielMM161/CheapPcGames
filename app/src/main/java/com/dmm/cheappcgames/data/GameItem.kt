package com.dmm.cheappcgames.data

data class GameItem(
    val gameId: String,
    val cheapestPriceEver: CheapestPriceEver,
    val deals: List<Deal>,
    val info: Info
)