package com.dmm.cheappcgames.utils

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.dmm.cheappcgames.resource.Resource
import com.dmm.cheappcgames.ui.OffersViewModel
import com.dmm.cheappcgames.ui.fragments.FragmentModalBottomSheet
import kotlinx.coroutines.flow.collect

class Utils {

    companion object {
        fun showToast(context: Context,message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        suspend fun subscribeObservableDealById(
            viewModel: OffersViewModel,
            context: Context,
            transaction: FragmentManager,
            hiddenProgressBar: () -> Unit,
            showProgressBar: () -> Unit
        )  {
            viewModel.gameId.collect {
                when(it) {
                    is Resource.Success -> {
                        it.data?.let { game ->
                            hiddenProgressBar
                            var dialogFragment = FragmentModalBottomSheet(game)
                            dialogFragment.show(transaction, "fragment_modal")
                        }
                        hiddenProgressBar
                    }
                    is Resource.Loading -> {
                        showProgressBar
                    }
                    is Resource.Error -> {
                        hiddenProgressBar
                        it.message.let { message ->
                            showToast(context, message)
                        }
                    }
                    is Resource.ErrorCaught -> {
                        hiddenProgressBar
                        val message = it.asString(context)
                        showToast(context, message)
                    }
                }
            }
        }
    }
}