package com.dmm.cheappcgames.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.adapters.GameDealersAdapter
import com.dmm.cheappcgames.data.Deal
import com.dmm.cheappcgames.data.GameItem
import com.dmm.cheappcgames.databinding.FragmentShowGameBinding
import com.dmm.cheappcgames.ui.DealsActivity
import com.dmm.cheappcgames.ui.OffersViewModel
import com.dmm.cheappcgames.utils.Utils
import com.google.android.material.snackbar.Snackbar

class FragmentShowGame : BaseFragment<FragmentShowGameBinding>(
    FragmentShowGameBinding::inflate
) {

    private lateinit var viewModel: OffersViewModel
    private lateinit var gameDealersAdapter: GameDealersAdapter
    private lateinit var game: GameItem
    private lateinit var mainDealer: Deal

    private val args: FragmentShowGameArgs by navArgs()

    override fun onViewCreated() {
        viewModel = (activity as DealsActivity).viewModel
        game = args.game
        mainDealer = game.deals.find { deal -> deal.storeID == game.storeId }!!
        setupListView()

        binding.infoGame = game.info
        binding.mainDealer = mainDealer
        binding.mainStoreItem = mainDealer?.storeItem

        binding.favorite.setOnClickListener {
            val offerGame = viewModel.dealsGames.value?.data?.filter { item -> item.dealID.equals(mainDealer.dealID) }
            if(offerGame?.size!! > 0) {
                viewModel.saveGame(offerGame[0])
                Snackbar.make(binding.root, "The game saved successfully", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.mainDealerLayout.setOnClickListener {
            goFragmentDealWebview(mainDealer.dealID)
        }
    }

    private fun setupListView() {
        binding.dealers.apply {
            val othersDealers = game.deals.filter { deal -> deal.storeID != mainDealer.storeID }
            gameDealersAdapter = GameDealersAdapter(requireContext(), othersDealers)
            adapter = gameDealersAdapter
        }
        itemClickListener()
    }

    private fun itemClickListener() = gameDealersAdapter.setOnItemClickListener {
        goFragmentDealWebview(it)
    }

    private fun goFragmentDealWebview(dealId: String) {
        if(viewModel.hasInternetConnection()) {
            val bundle = Bundle().apply {
                putString("dealId", dealId)
            }
            findNavController().navigate(R.id.action_fragmentShowGame_to_fragmentDealWebview, bundle)
        } else {
            Utils.showToast(requireContext(), "You're offline")
        }
    }



}