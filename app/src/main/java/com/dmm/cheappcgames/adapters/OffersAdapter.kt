package com.dmm.cheappcgames.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dmm.cheappcgames.data.Offer
import com.dmm.cheappcgames.data.StoreItem
import com.dmm.cheappcgames.databinding.ItemOffersBinding
import com.dmm.cheappcgames.ui.OffersViewModel

class OffersAdapter(val gamesDistributor: List<StoreItem>) : ListAdapter<Offer, OffersAdapter.OfferViewHolder>(diffCallback) {

    class OfferViewHolder(private val binding: ItemOffersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(offer: Offer, gamesDistributor: List<StoreItem>) {
            binding.offer = offer
            binding.stores = gamesDistributor
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val binding = ItemOffersBinding.inflate(LayoutInflater.from(parent.context))
        return OfferViewHolder(binding)
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Offer>() {
            override fun areContentsTheSame(oldItem: Offer, newItem: Offer): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Offer, newItem: Offer): Boolean {
                return oldItem.dealID == newItem.dealID
            }
        }
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, gamesDistributor)
    }

}