package com.dmm.cheappcgames.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.cheappcgames.DealsApplication
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.data.GameItem
import com.dmm.cheappcgames.data.Offer
import com.dmm.cheappcgames.data.StoreItem
import com.dmm.cheappcgames.repository.OffersRepository
import com.dmm.cheappcgames.resource.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response

class OffersViewModel(
    app: Application,
    private val repository: OffersRepository
) : AndroidViewModel(app) {

    lateinit var deals: StateFlow<Resource<List<Offer>>>

    private var _dealsGames = MutableStateFlow<Resource<List<Offer>>>(Resource.Loading())
    val dealsGames = _dealsGames.asStateFlow()
    var dealsPage = 0
    private var dealsGamesResponse: MutableList<Offer>? = null

    private var _dealsGamesSearch = MutableStateFlow<Resource<List<Offer>>>(Resource.Pause())
    val dealsGamesSearch = _dealsGamesSearch.asStateFlow()
    private var dealsPageSearch = 0
    private var dealsGamesResponseSearch: MutableList<Offer>? = null

    private var _favoriteDeals = MutableStateFlow<Resource<List<Offer>>>(Resource.Loading())
    val favoriteDeals = _favoriteDeals.asStateFlow()

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
        if(hasInternetConnection()) {
            val response = repository.getStores()
            _gamesStores = handleGamesStores(response)
            if(_gamesStores.isNotEmpty()) {
                getDeals()
            }
        } else {
            _dealsGames.value = Resource.ErrorCaught(resId = R.string.offline)
        }
    }

    fun getDealsByTitle() = viewModelScope.launch {
        if(hasInternetConnection()) {
            _dealsGamesSearch.value = Resource.Loading()
            val response = repository.getDealsByTitle(dealsPageSearch, searchText)
            _dealsGamesSearch.value = handleDealsGamesSearchResponse(response)
        } else {
            _dealsGamesSearch.value = Resource.ErrorCaught(resId = R.string.offline)
        }
    }

    fun handleDealsByTitle(query: String) {
        if(hasInternetConnection()) {
            resetSearchResponse()
            searchText = query
            getDealsByTitle()
        } else {
            _dealsGamesSearch.value = Resource.ErrorCaught(resId = R.string.offline)
        }
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
        if(hasInternetConnection()) {
            val response = repository.getGameById(gameId)
            _gameId.value = handleGameById(response, storeId)
            _gameId.value = Resource.Pause()
        } else {
            _gameId.value = Resource.ErrorCaught(resId = R.string.offline)
        }

    }

    fun dealsHandler() {
        if(hasInternetConnection()) {
            if(storesSelectedList.size > 0) {
                getFilteredDeals()
            } else {
                getDeals()
            }
        } else {
            _dealsGames.value = Resource.ErrorCaught(resId = R.string.offline)
        }

    }

    fun saveGame(game: Offer) = viewModelScope.launch {
        repository.insertGame(game)
    }

    fun deleteGame(game: Offer) = viewModelScope.launch {
        repository.deleteGame(game)
    }

    fun getFavoritesDeals() = repository.getFavoritesOffers()

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

    fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<DealsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> return true
                else -> return false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> return true
                    TYPE_MOBILE -> return true
                    TYPE_ETHERNET -> return true
                    else -> return false
                }
            }
        }
        return false
    }
}