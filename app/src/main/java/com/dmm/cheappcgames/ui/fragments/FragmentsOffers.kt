package com.dmm.cheappcgames.ui.fragments

import android.view.*
import androidx.navigation.fragment.findNavController
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.databinding.FragmentOffersBinding

class FragmentsOffers : DealsBaseFragment<FragmentOffersBinding>(
    FragmentOffersBinding::inflate
) {

    override fun onViewCreated() {
        super.onViewCreated()
        setUpRecyclerView(binding.rvOffers)
        showFilter()
    }

    private fun showFilter() {
        binding.filter.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentsOffers_to_fragmentFilter)
        }
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