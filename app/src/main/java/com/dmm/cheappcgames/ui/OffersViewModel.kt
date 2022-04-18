package com.dmm.cheappcgames.ui

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.cheappcgames.data.GameItem
import com.dmm.cheappcgames.data.Offer
import com.dmm.cheappcgames.data.StoreItem
import com.dmm.cheappcgames.resource.Resource
import kotlinx.coroutines.launch
import retrofit2.Response


class OffersViewModel(
    private val repository: OffersRepository
) : ViewModel() {

    val offersGame: MutableLiveData<Resource<List<Offer>>?> = MutableLiveData()
    var offersPage = 1
    var offersGameResponse: MutableList<Offer>? = null

    val searchOffers: MutableLiveData<Resource<List<Offer>>> = MutableLiveData()
    var searchPage = 0
    var searchOffersResponse: MutableList<Offer>? = null

    val gamesDistributor: MutableLiveData<Resource<List<StoreItem>>> = MutableLiveData()

    var gameId: MutableLiveData<Resource<GameItem>> = MutableLiveData()

    var storesSelectedList: MutableList<String> = ArrayList()

    var searchText: String = ""

    init {
        getStores()
    }

    fun getOffers() = viewModelScope.launch {
        offersGame.postValue(Resource.Loading())
        if(isStoresSelected()) {
            var storesSelected = ""
            storesSelectedList.forEachIndexed { index, element -> if(index == storesSelectedList.size - 1) storesSelected = storesSelected+element else storesSelected = "${storesSelected+element}," }
            val response = repository.getOffers(offersPage, storesSelected)
            offersGame.postValue(handleOffersResponse(response))
        } else {
            val response = repository.getOffers(offersPage)
            offersGame.postValue(handleOffersResponse(response))
        }
    }

    fun getSearchOffers(title: String) = viewModelScope.launch {
        searchOffers.postValue(Resource.Loading())
        if(isStoresSelected()) {
            var storesSelected = ""
            storesSelectedList.forEachIndexed { index, element -> if(index == storesSelectedList.size - 1) storesSelected = storesSelected+element else storesSelected = "${storesSelected+element}," }
            val response = repository.getSearchOffers(searchPage, storesSelected, title)
            searchOffers.postValue(handleSearchResponse(response))
        } else {
            val response = repository.getSearchOffers(searchPage, title = title)
            searchOffers.postValue(handleSearchResponse(response))
        }
    }

    fun getStores() = viewModelScope.launch {
        gamesDistributor.postValue(Resource.Loading())
        val response = repository.getSotres()
        gamesDistributor.postValue(handleGamesDistributor(response))
    }

    fun getGameById(id: Int) = viewModelScope.launch {
        gameId.postValue(Resource.Loading())
        val response = repository.getGameById(id)
        gameId.postValue(handleGameById(response, id.toString()))
    }

    fun saveGame(game: Offer) = viewModelScope.launch {
        repository.insertGame(game)
    }

    fun getFavoritesGames() = repository.getFavoritesOffers()

    private fun handleOffersResponse(response: Response<List<Offer>>) : Resource<List<Offer>> {
        if(response.isSuccessful) {
            response.body()?.let { result ->
                offersPage++
                if(offersGameResponse == null) {
                    offersGameResponse = result.toMutableList()
                } else {
                    val oldGames = offersGameResponse
                    val newGames = result
                    oldGames?.addAll(newGames)
                }
                return Resource.Success(offersGameResponse ?: result)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchResponse(response: Response<List<Offer>>) : Resource<List<Offer>> {
        if(response.isSuccessful) {
            response.body()?.let { result ->
                searchPage++
                if(searchOffersResponse == null) {
                    searchOffersResponse = result.toMutableList()
                } else {
                    val oldGames = searchOffersResponse
                    val newGames = result
                    oldGames?.addAll(newGames)
                }
                return Resource.Success(searchOffersResponse ?: result)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleGamesDistributor(response: Response<List<StoreItem>>) : Resource<List<StoreItem>> {
        if(response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result.toMutableList())
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleGameById(response: Response<GameItem>, id: String) : Resource<GameItem> {
        if(response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result.copy(gameId = id))
            }
        }
        return Resource.Error(response.message())
    }

    private fun isStoresSelected(): Boolean {
        return storesSelectedList.size != 0
    }

    fun resetResponseOffers() {
        offersGameResponse = null
        offersPage = 1
    }

    fun resetGameById() {
        gameId =  MutableLiveData()
    }

    fun resetResponseSearch() {
        searchOffersResponse = null
        searchPage = 0
    }

    fun handleOnClickMaterial(view: View, storeItem: StoreItem) {
        view.isSelected = !view.isSelected
        if(view.isSelected) storesSelectedList.add(storeItem.storeID) else storesSelectedList.remove(storeItem.storeID)
    }
}