package com.dmm.cheappcgames.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dmm.cheappcgames.repository.OffersRepository

class ViewModelFactory(
    val app: Application,
    private val repository: OffersRepository
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OffersViewModel(app, repository) as T
    }
}