package com.dmm.cheappcgames.data

data class GameItem(
    val cheapestPriceEver: CheapestPriceEver,
    val deals: List<Deal>,
    val info: Info
)