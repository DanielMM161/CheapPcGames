package com.dmm.cheappcgames.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.adapters.OffersAdapter
import com.dmm.cheappcgames.data.Offer
import com.dmm.cheappcgames.resource.Resource
import com.dmm.cheappcgames.ui.DealsActivity
import com.dmm.cheappcgames.ui.OffersViewModel
import com.dmm.cheappcgames.utils.Constants
import com.dmm.cheappcgames.utils.Utils
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class DealsBaseFragment<VB: ViewBinding>(
    bindingInflater: (inflater: LayoutInflater) -> VB
) : BaseFragment<VB>(
    bindingInflater
) {

    lateinit var viewModel: OffersViewModel
    protected abstract val action: Int
    private lateinit var offersAdapter: OffersAdapter

    protected abstract fun hiddenProgressBar()
    protected abstract fun showProgressBar()

    override fun onViewCreated() {
        viewModel = (activity as DealsActivity).viewModel

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    subscribeObservableDeals()
                }
                launch {
                    subscribeObservableDealById()
                }
            }
        }
    }

    protected suspend fun subscribeObservableDeals()  {
        viewModel.deals.collect {
            when(it) {
                is Resource.Success -> {
                    it.data?.let { offers ->
                        offersAdapter.differ.submitList(offers)
                        //Temporal Solution
                        offersAdapter.notifyItemRangeChanged(0, offers.size)

                        val totalPages = offers.size / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.dealsPage == totalPages

                        hiddenProgressBar()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    hiddenProgressBar()
                    it.message.let { message ->
                        Utils.showToast(requireContext(), "An error occured: $message")
                    }
                }
                is Resource.ErrorCaught -> {
                    hiddenProgressBar()
                    val message = it.asString(requireContext())
                    Utils.showToast(requireContext(), "$message")
                }
            }
        }
    }

    private suspend fun subscribeObservableDealById() {
        viewModel.gameId.collect {
            when(it) {
                is Resource.Success -> {
                    it.data?.let { game ->
                        hiddenProgressBar()
                        val bundle = Bundle().apply {
                            putSerializable("game", game)
                        }
                        findNavController().navigate(action, bundle)
                        hiddenProgressBar()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    hiddenProgressBar()
                    it.message.let { message ->
                        Utils.showToast(requireContext(), "An error occured: $message")
                    }
                }
                is Resource.ErrorCaught -> {
                    hiddenProgressBar()
                    val message = it.asString(requireContext())
                    Utils.showToast(requireContext(), "$message")
                }
            }
        }
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstElementVisible = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItem = offersAdapter.differ.currentList.size

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isLastItem = firstElementVisible + visibleItemCount >= totalItem
            val isNotAtBegining = visibleItemCount >= 0
            val isTotalMoreThanVisible = totalItem >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isLastItem && isNotAtBegining && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.dealsHandler()
                isScrolling = false
            } else {
                recyclerView.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    protected fun setUpRecyclerView(recyclerView: RecyclerView) {
        recyclerView.apply {
            offersAdapter = OffersAdapter()
            adapter = offersAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(this@DealsBaseFragment.scrollListener)
        }
        itemClickListener()
    }

    private fun itemClickListener() = offersAdapter.setOnItemClickListener {
        val gameId = it.gameID.toInt()
        val storeId = it.storeID
        viewModel.getGameById(gameId, storeId)
    }

}