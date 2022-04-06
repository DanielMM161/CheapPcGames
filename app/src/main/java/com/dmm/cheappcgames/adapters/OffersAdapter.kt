package com.dmm.cheappcgames.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dmm.cheappcgames.data.Offer
import com.dmm.cheappcgames.databinding.ItemOffersBinding

class OffersAdapter() : ListAdapter<Offer, OffersAdapter.OfferViewHolder>(diffCallback) {

    class OfferViewHolder(private val binding: ItemOffersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(offer: Offer) {
            binding.offer = offer
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
        holder.bind(item)
    }

}