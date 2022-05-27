package com.dmm.cheappcgames.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dmm.cheappcgames.data.Deal
import com.dmm.cheappcgames.data.StoreItem
import com.dmm.cheappcgames.databinding.ItemGamesStoresBinding

class GamesStoresAdapter(val gameDealers: List<Deal>): RecyclerView.Adapter<GamesStoresAdapter.GamesStoresViewHolder>() {

    inner class GamesStoresViewHolder(private val binding: ItemGamesStoresBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(deal: Deal, storeItem: StoreItem) {
            binding.deal = deal
            binding.store = storeItem

            binding.relativeLayout.setOnClickListener {
                onItemClickListener?.let { deal.dealID.let { it1 -> it(it1) } }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesStoresViewHolder {
        val binding = ItemGamesStoresBinding.inflate(LayoutInflater.from(parent.context))
        return GamesStoresViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GamesStoresViewHolder, position: Int) {
        val deal: Deal = gameDealers.get(position)
        deal.storeItem.let {
            holder.bind(deal,it)
        }
    }

    override fun getItemCount(): Int { return gameDealers.size }

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }
}