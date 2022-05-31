package com.dmm.cheappcgames.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.adapters.GamesStoresAdapter
import com.dmm.cheappcgames.data.Deal
import com.dmm.cheappcgames.data.GameItem
import com.dmm.cheappcgames.databinding.FragmentModalBottomSheetBinding
import com.dmm.cheappcgames.ui.DealsActivity
import com.dmm.cheappcgames.ui.OffersViewModel
import com.dmm.cheappcgames.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class FragmentModalBottomSheet(private val gameItem: GameItem) : BottomSheetDialogFragment() {

    private lateinit var _binding : FragmentModalBottomSheetBinding
    private val binding get() = _binding

    private lateinit var viewModel: OffersViewModel
    private lateinit var gameStoresAdapter: GamesStoresAdapter
    private lateinit var game: GameItem
    private lateinit var mainDealer: Deal

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_modal_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as DealsActivity).viewModel
        game = gameItem
        mainDealer = game.deals.find { deal -> deal.storeID == game.storeId }!!
        _binding = FragmentModalBottomSheetBinding.bind(view)
        setupListView()

        binding.infoGame = game.info
        binding.mainDealer = mainDealer
        binding.mainStoreItem = mainDealer.storeItem

        binding.favorite.setOnClickListener {
            val offerGame = viewModel.dealsGames.value.data?.filter { item -> item.dealID == mainDealer.dealID }
            if(offerGame?.size!! > 0) {
                viewModel.saveGame(offerGame[0])
                Snackbar.make(binding.root, getString(R.string.save_game), Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.close.setOnClickListener {
            dismiss()
        }

        binding.buttonStore.setOnClickListener {
            goToDealWebSite(Constants.REDIRECT_URL+mainDealer.dealID)
        }
    }

    private fun setupListView() {
        binding.rvStores.apply {
            val othersDealers = game.deals.filter { deal -> deal.storeID != mainDealer.storeID }
            gameStoresAdapter = GamesStoresAdapter(othersDealers)
            adapter = gameStoresAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        itemClickListener()
    }

    private fun itemClickListener() = gameStoresAdapter.setOnItemClickListener {
        goToDealWebSite(Constants.REDIRECT_URL+it)
    }

    private fun goToDealWebSite(uriString: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uriString)))
    }
}