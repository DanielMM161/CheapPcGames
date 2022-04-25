package com.dmm.cheappcgames.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmm.cheappcgames.MainActivity
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.adapters.OffersAdapter
import com.dmm.cheappcgames.databinding.FragmentSearchBinding
import com.dmm.cheappcgames.resource.Resource
import com.dmm.cheappcgames.ui.OffersViewModel
import com.dmm.cheappcgames.utils.Constants
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FragmentSearch : Fragment() {

    private lateinit var _binding: FragmentSearchBinding
    private val binding get() = _binding

    lateinit var viewModel: OffersViewModel
    private lateinit var searchDealsAdapter: OffersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                subscribeObservableDealsSearch()
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
            val totalItem = searchDealsAdapter.differ.currentList.size

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isLastItem = firstElementVisible + visibleItemCount >= totalItem
            val isNotAtBegining = visibleItemCount >= 0
            val isTotalMoreThanVisible = totalItem >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isLastItem && isNotAtBegining && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.getDealsByTitle()
                isScrolling = false
            } else {
                binding.rvSearchDeals.setPadding(0, 0, 0, 0)
            }

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private suspend fun subscribeObservableDealsSearch() {
        viewModel.dealsGamesSearch.collect {
            when(it) {
                is Resource.Success -> {
                    it.data?.let { offers ->
                        searchDealsAdapter.differ.submitList(offers)
                        //Temporal Solution
                        searchDealsAdapter.notifyItemRangeChanged(0, offers.size)
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
                }
            }
        }
    }

    private fun setupRecyclerView() = binding.rvSearchDeals.apply {
        searchDealsAdapter = OffersAdapter()
        adapter = searchDealsAdapter
        layoutManager = LinearLayoutManager(requireContext())
        addOnScrollListener(this@FragmentSearch.scrollListener)
    }

    private fun hiddenProgressBar() {
        binding.paginationProgressbar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressbar.visibility = View.VISIBLE
        isLoading = true
    }

}