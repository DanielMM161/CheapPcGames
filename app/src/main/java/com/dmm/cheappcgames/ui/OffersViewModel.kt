package com.dmm.cheappcgames.ui

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmm.cheappcgames.data.Offer
import com.dmm.cheappcgames.data.StoreItem
import com.dmm.cheappcgames.resource.Resource
import kotlinx.coroutines.launch
import retrofit2.Response


class OffersViewModel(
    private val repository: OffersRepository
) : ViewModel() {

    val offersGame: MutableLiveData<Resource<List<Offer>>> = MutableLiveData()
    var offersPage = 1
    var offersGameResponse: MutableList<Offer>? = null

    var storesGame: List<StoreItem> = emptyList()
    var storesSelectedList: MutableList<String> = ArrayList()

    init {
        getStores()
        getOffers()
    }

    fun getOffers() = viewModelScope.launch {
        offersGame.postValue(Resource.Loading())
        if(areThereStoresSeleted()) {
            var storesSelected = ""
            storesSelectedList.forEachIndexed { index, element -> if(index == storesSelectedList.size - 1) storesSelected = storesSelected+element else storesSelected = "${storesSelected+element}," }
            val response = repository.getOffers(offersPage, storesSelected)
            offersGame.postValue(handleOffersResponse(response))
        } else {
            val response = repository.getOffers(offersPage)
            offersGame.postValue(handleOffersResponse(response))
        }
    }

    fun getStores() = viewModelScope.launch {
        val response = repository.getSotres()
        if(response.isSuccessful) {
            response.body()?.let { result ->
                storesGame = result
                repository.insertStores(result)
            }

        }
    }

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

    private fun handleStoreResponse(response: Response<List<StoreItem>>) : Resource<List<StoreItem>> {
        if(response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    private fun areThereStoresSeleted(): Boolean {
        return storesSelectedList.size != 0
    }

    fun resetResponse() {
        offersGameResponse = null
        offersPage = 1
    }

    fun handleOnClickMaterial(view: View, storeItem: StoreItem) {
        view.isSelected = !view.isSelected
        if(view.isSelected) storesSelectedList.add(storeItem.storeID) else storesSelectedList.remove(storeItem.storeID)
    }
}