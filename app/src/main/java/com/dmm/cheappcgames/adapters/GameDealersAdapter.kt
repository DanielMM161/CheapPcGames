package com.dmm.cheappcgames.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.data.Deal
import com.dmm.cheappcgames.databinding.ItemGameDealersBinding
import com.dmm.cheappcgames.ui.OffersViewModel

class GameDealersAdapter(private val cont: Context, val gameDealers: List<Deal>,  val viewModel: OffersViewModel) : ArrayAdapter<Deal>(cont, R.layout.item_game_dealers,gameDealers) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
       val binding = ItemGameDealersBinding.inflate(
           LayoutInflater.from(parent.context),
           parent,
           false
       )
        val deal = getItem(position)
        binding.deal = deal
        val stores = viewModel.gamesDistributor.value?.data
        val storesSelected = stores?.filter { storeItem -> storeItem.storeID.equals(deal?.storeID) }
        binding.store = storesSelected?.get(0)

        return binding.root
    }
}