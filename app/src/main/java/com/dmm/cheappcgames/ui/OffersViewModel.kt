package com.dmm.cheappcgames.ui

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.cheappcgames.data.Deal
import com.dmm.cheappcgames.data.GameItem
import com.dmm.cheappcgames.data.Offer
import com.dmm.cheappcgames.data.StoreItem
import com.dmm.cheappcgames.resource.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response

class OffersViewModel(
    private val repository: OffersRepository
) : ViewModel() {

    private var _dealsGames = MutableStateFlow<Resource<List<Offer>>>(Resource.Loading())
    val dealsGames = _dealsGames.asStateFlow()
    var dealsPage = 0
    private var dealsGamesResponse: MutableList<Offer>? = null

    private var _dealsGamesSearch = MutableStateFlow<Resource<List<Offer>>>(Resource.Pause())
    val dealsGamesSearch = _dealsGamesSearch.asStateFlow()
    private var dealsPageSearch = 0
    private var dealsGamesResponseSearch: MutableList<Offer>? = null

    private var _gamesStores: List<StoreItem> = emptyList()
    val gamesStores get() = _gamesStores

    private var _gameId = MutableStateFlow<Resource<GameItem>>(Resource.Pause())
    val gameId = _gameId.asStateFlow()

    var storesSelectedList: MutableList<String> = ArrayList()

    var searchText: String = ""

    init {
        getStores()
    }

    private fun getDeals() = viewModelScope.launch {
        _dealsGames.value = Resource.Loading()
        val response = repository.getDeals(dealsPage)
        _dealsGames.value = handleDealsGamesResponse(response)
    }

    private fun getStores() = viewModelScope.launch {
        val response = repository.getStores()
        _gamesStores = handleGamesStores(response)
        if(_gamesStores.isNotEmpty()) {
            getDeals()
        }
    }

    fun getDealsByTitle() = viewModelScope.launch {
        _dealsGamesSearch.value = Resource.Loading()
        val response = repository.getDealsByTitle(dealsPageSearch, searchText)
        _dealsGamesSearch.value = handleDealsGamesSearchResponse(response)
        _dealsGamesSearch.value = Resource.Pause()
    }

    private fun getFilteredDeals() = viewModelScope.launch {
        var storesId: String = ""
        storesSelectedList.forEachIndexed { index, element -> storesId =
            if(index == storesSelectedList.size - 1) storesId+element else "${storesId+element},"
        }

        _dealsGames.value = Resource.Loading()
        val response = repository.getFilteredDeals(dealsPage, storesId)
        _dealsGames.value = handleDealsGamesResponse(response)
    }

    fun getGameById(gameId: Int, storeId: String) = viewModelScope.launch {
        _gameId.value = Resource.Loading()
        val response = repository.getGameById(gameId)
        _gameId.value = handleGameById(response, storeId)
        _gameId.value = Resource.Pause()
    }

    fun dealsHandler() {
         if(storesSelectedList.size > 0) {
            getFilteredDeals()
        } else {
            getDeals()
        }
    }

    fun saveGame(game: Offer) = viewModelScope.launch {
        repository.insertGame(game)
    }

    fun deleteGame(game: Offer) = viewModelScope.launch {
        repository.deleteGame(game)
    }

    fun getFavoritesGames() = repository.getFavoritesOffers()

    private fun handleDealsGamesResponse(response: Response<List<Offer>>) : Resource<List<Offer>> {
        if(response.isSuccessful) {
            response.body()?.let { result ->
                dealsPage++
                if(dealsGamesResponse == null) {
                    dealsGamesResponse = addStoreItemtoDealsItem(result.toMutableList()).toMutableList()
                } else {
                    val oldGames = dealsGamesResponse
                    val newGames = addStoreItemtoDealsItem(result)
                    oldGames?.addAll(newGames)
                }
                return Resource.Success(dealsGamesResponse ?: result)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleDealsGamesSearchResponse(response: Response<List<Offer>>) : Resource<List<Offer>> {
        if(response.isSuccessful) {
            response.body()?.let { result ->
                dealsPageSearch++
                if(dealsGamesResponseSearch == null) {
                    dealsGamesResponseSearch = addStoreItemtoDealsItem(result.toMutableList()).toMutableList()
                } else {
                    val oldGames = dealsGamesResponseSearch
                    val newGames = addStoreItemtoDealsItem(result)
                    oldGames?.addAll(newGames)
                }
                return Resource.Success(dealsGamesResponseSearch ?: result)
            }
        }
        return Resource.Error(response.message())
    }

    private fun addStoreItemtoDealsItem(deals: List<Offer>) : List<Offer> {
       return deals.map { deal ->
            val storeId = deal.storeID
            val store = _gamesStores.find { store -> store.storeID.equals(storeId) }
            store?.let {
                deal.storeItem = it
            }
           deal
        }
    }

    private fun handleGamesStores(response: Response<List<StoreItem>>) : List<StoreItem> {
        var gamesStores: List<StoreItem> = emptyList()
        if(response.isSuccessful) {
            response.body()?.let { result ->
               gamesStores = result
            }
        }
        return gamesStores
    }

    private fun handleGameById(response: Response<GameItem>, storeId: String) : Resource<GameItem> {
        if(response.isSuccessful) {
            response.body()?.let { resResult ->
                resResult.deals.forEach { item ->
                    val store =  _gamesStores.find { store -> store.storeID == item.storeID }
                    store?.let {
                        item.storeItem = it
                    }
                }
                return Resource.Success(resResult.copy(storeId = storeId))
            }
        }
        return Resource.Error(response.message())
    }

    fun resetResponse() {
        dealsGamesResponse = null
        dealsPage = 0
    }

    fun resetSearchResponse() {
        dealsGamesResponseSearch = null
        dealsPageSearch = 0
    }

    fun handleOnClickMaterial(view: View, storeItem: StoreItem) {
        view.isSelected = !view.isSelected
        if(view.isSelected) storesSelectedList.add(storeItem.storeID) else storesSelectedList.remove(storeItem.storeID)
    }
}