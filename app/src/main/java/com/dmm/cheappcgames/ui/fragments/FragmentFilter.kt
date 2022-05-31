package com.dmm.cheappcgames.ui.fragments

import android.content.Context
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.adapters.DistributorAdapter
import com.dmm.cheappcgames.databinding.FragmentFilterBinding
import com.dmm.cheappcgames.ui.DealsActivity
import com.dmm.cheappcgames.ui.OffersViewModel

class FragmentFilter : BaseFragment<FragmentFilterBinding>(FragmentFilterBinding::inflate) {

    private lateinit var viewModel: OffersViewModel

    override fun onViewCreated() {
        viewModel = (activity as DealsActivity).viewModel
        setGrid()

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.resetResponse()
                    viewModel.dealsHandler()
                    findNavController().navigate(R.id.action_fragmentFilter_to_fragmentsOffers)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun setGrid() = binding.grid.apply {
        viewModel.gamesStores.let {
            val activeDistributors = it.filter { item -> item.isActive == 1 }
            adapter = DistributorAdapter(activeDistributors, viewModel)
        }
    }

    override fun onAttach(context: Context) {
        (activity as DealsActivity).onNextClicked = {
            viewModel.resetResponse()
            viewModel.dealsHandler()
        }
        super.onAttach(context)
    }
}