package com.dmm.cheappcgames.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmm.cheappcgames.MainActivity
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.adapters.OffersAdapter
import com.dmm.cheappcgames.databinding.FragmentOffersBinding
import com.dmm.cheappcgames.resource.Resource
import com.dmm.cheappcgames.ui.OffersViewModel
import com.dmm.cheappcgames.utils.Constants.Companion.QUERY_PAGE_SIZE

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
        setUpRecyclerView()

        viewModel = (activity as MainActivity).viewModel

        viewModel.offersGame.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    response.data?.let { offers ->
                        offersAdapter.submitList(offers.toList())
                        val totalPages = offers.size / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.offersPage == totalPages
                    }
                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {

                }
            }
        })

        binding.filter.setOnClickListener {
            showFilter()
        }
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstElementVisible = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItem = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isLastItem = firstElementVisible + visibleItemCount >= totalItem
            val isNotAtBegining = visibleItemCount >= 0
            val isTotalMoreThanVisible = totalItem >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isLastItem && isNotAtBegining && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.getOffers()
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

    fun setUpRecyclerView() = binding.rvOffers.apply {
        offersAdapter = OffersAdapter()
        adapter = offersAdapter
        layoutManager = LinearLayoutManager(requireContext())
        addOnScrollListener(this@FragmentsOffers.scrollListener)
    }

    fun showFilter() {
        findNavController().navigate(R.id.action_fragmentsOffers_to_fragmentFilter)
    }

    fun showDialog() {
        var dialog = FragmentShowOfferDialog()
        dialog.show(parentFragmentManager, "filterDialog")
    }
}