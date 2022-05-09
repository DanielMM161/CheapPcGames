package com.dmm.cheappcgames.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.adapters.OffersAdapter
import com.dmm.cheappcgames.databinding.FragmentOffersBinding
import com.dmm.cheappcgames.resource.Resource
import com.dmm.cheappcgames.ui.DealsActivity
import com.dmm.cheappcgames.ui.OffersViewModel
import com.dmm.cheappcgames.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.dmm.cheappcgames.utils.Utils
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FragmentsOffers() : Fragment() {

    private lateinit var _binding : FragmentOffersBinding
    private val binding get() = _binding

    lateinit var viewModel: OffersViewModel
    private lateinit var offersAdapter: OffersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_offers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOffersBinding.bind(view)
        viewModel = (activity as DealsActivity).viewModel
        setUpRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
               launch {
                   subscribeObservableDeals()
               }
                launch {
                    subscribeObservableGameId()
                }
            }
        }

        binding.filter.setOnClickListener {
            showFilter()
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
            val isTotalMoreThanVisible = totalItem >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isLastItem && isNotAtBegining && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.dealsHandler()
                isScrolling = false
            } else {
                binding.rvOffers.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rvOffers.apply {
            offersAdapter = OffersAdapter()
            adapter = offersAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(this@FragmentsOffers.scrollListener)
        }
        itemClickListener()
    }

    private fun showFilter() {
        findNavController().navigate(R.id.action_fragmentsOffers_to_fragmentFilter)
    }

    private fun hiddenProgressBar() {
        binding.paginationProgressbar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressbar.visibility = View.VISIBLE
        isLoading = true
    }

    private suspend fun subscribeObservableDeals() = viewModel.dealsGames.collect {
        showProgressBar()
        when(it) {
            is Resource.Success -> {
                it.data?.let { offers ->
                    offersAdapter.differ.submitList(offers)
                    //Temporal Solution
                    offersAdapter.notifyItemRangeChanged(0, offers.size)

                    val totalPages = offers.size / QUERY_PAGE_SIZE + 2
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



    private suspend fun subscribeObservableGameId() {
        viewModel.gameId.collect {
            when(it) {
                is Resource.Success -> {
                    it.data?.let { game ->
                        hiddenProgressBar()
                        val bundle = Bundle().apply {
                            putSerializable("game", game)
                        }
                        findNavController().navigate(R.id.action_fragmentsOffers_to_fragmentShowGame, bundle)
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

    private fun itemClickListener() = offersAdapter.setOnItemClickListener {
        val gameId = it.gameID.toInt()
        val storeId = it.storeID
        viewModel.getGameById(gameId, storeId)
    }

}