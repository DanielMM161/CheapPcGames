package com.dmm.cheappcgames.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dmm.cheappcgames.MainActivity
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.adapters.DistributorAdapter
import com.dmm.cheappcgames.databinding.FragmentFilterBinding
import com.dmm.cheappcgames.ui.OffersViewModel

class FragmentFilter : Fragment() {

    lateinit var _binding: FragmentFilterBinding
    private val binding get() = _binding

    lateinit var viewModel: OffersViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentFilterBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
        setGrid()

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.getOffers()
                    findNavController().navigate(R.id.action_fragmentFilter_to_fragmentsOffers)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        viewModel.resetResponseOffers()
        viewModel.resetGameById()
    }

    private fun setGrid() = binding.grid.apply {
        val gamesDistributor = viewModel.gamesDistributor.value?.data
        val activeDistributors = gamesDistributor?.filter { item -> item.isActive == 1 }!!

        adapter = DistributorAdapter(activeDistributors, viewModel)
    }

    override fun onAttach(context: Context) {
        (activity as MainActivity).onNextClicked = {
            viewModel.getOffers()
        }
        super.onAttach(context)
    }


}