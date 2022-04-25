package com.dmm.cheappcgames.data

data class Deal(
    val dealID: String,
    val price: String,
    val retailPrice: String,
    val savings: String,
    val storeID: String,
    var storeItem: StoreItem
)