package com.dmm.cheappcgames.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.dmm.cheappcgames.MainActivity
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.adapters.GameDealersAdapter
import com.dmm.cheappcgames.data.GameItem
import com.dmm.cheappcgames.databinding.FragmentShowOfferDialogBinding
import com.dmm.cheappcgames.ui.OffersViewModel

class FragmentShowOfferDialog() : DialogFragment() {

    private lateinit var _binding : FragmentShowOfferDialogBinding
    private val binding get() = _binding

    private lateinit var gameItem: GameItem

    lateinit var viewModel: OffersViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_offer_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentShowOfferDialogBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
        gameItem = viewModel.gameId.value?.data!!
        binding.game = gameItem
        setUpListView()
    }

    fun setUpListView() = binding.distributorList.apply {
        adapter = GameDealersAdapter(requireContext(),gameItem.deals, viewModel)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

}