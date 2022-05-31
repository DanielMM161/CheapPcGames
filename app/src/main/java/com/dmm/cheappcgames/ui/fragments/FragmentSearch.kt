package com.dmm.cheappcgames.ui.fragments

import android.view.View
import com.dmm.cheappcgames.databinding.FragmentSearchBinding

class FragmentSearch : DealsBaseFragment<FragmentSearchBinding>(
    FragmentSearchBinding::inflate
) {

    override fun onViewCreated() {
        super.onViewCreated()
        setUpRecyclerView(binding.rvSearchDeals)
    }

    override fun hiddenProgressBar() {
        binding.progressbar.visibility = View.GONE
        isLoading = false
    }

    override fun showProgressBar() {
        binding.progressbar.visibility = View.VISIBLE
        isLoading = true
    }
}