package com.dmm.cheappcgames.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.dmm.cheappcgames.data.Offer
import com.dmm.cheappcgames.databinding.FragmentOffersBinding
import com.dmm.cheappcgames.databinding.FragmentSearchBinding
import com.dmm.cheappcgames.resource.Resource
import com.dmm.cheappcgames.ui.DealsActivity
import com.dmm.cheappcgames.ui.OffersViewModel
import com.dmm.cheappcgames.utils.Constants
import com.dmm.cheappcgames.utils.Utils
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FragmentSearch : DealsBaseFragment<FragmentSearchBinding>(
    FragmentSearchBinding::inflate
) {

    override val action: Int = R.id.action_fragmentSearch_to_fragmentShowGame

    override fun onViewCreated() {
        super.onViewCreated()
        setUpRecyclerView(binding.rvSearchDeals)
    }

    override fun hiddenProgressBar() {
        binding.paginationProgressbar.visibility = View.GONE
        isLoading = false
    }

    override fun showProgressBar() {
        binding.paginationProgressbar.visibility = View.VISIBLE
        isLoading = true
    }
}