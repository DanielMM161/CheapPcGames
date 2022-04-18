package com.dmm.cheappcgames.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.dmm.cheappcgames.data.StoreItem
import com.dmm.cheappcgames.databinding.ItemDistributorBinding
import com.dmm.cheappcgames.ui.OffersViewModel

class DistributorAdapter(val stores: List<StoreItem>, val viewModel: OffersViewModel) : BaseAdapter() {
    override fun getCount(): Int {
        return stores.size
    }

    override fun getItem(pos: Int): StoreItem {
        return stores[pos]
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemDistributorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        val item: StoreItem = getItem(position)
        binding.store = item
        binding.viewModel = viewModel

        if(viewModel.storesSelectedList.contains(item.storeID.toString())) {
            binding.button.isSelected = true
        }
        return binding.root
    }

}