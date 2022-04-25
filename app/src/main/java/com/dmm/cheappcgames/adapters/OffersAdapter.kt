package com.dmm.cheappcgames.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dmm.cheappcgames.data.Offer
import com.dmm.cheappcgames.databinding.ItemOffersBinding

class OffersAdapter() : RecyclerView.Adapter<OffersAdapter.OfferViewHolder>() {

    inner class OfferViewHolder(private val binding: ItemOffersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(offer: Offer) {
            binding.offer = offer
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val binding = ItemOffersBinding.inflate(LayoutInflater.from(parent.context))
        return OfferViewHolder(binding)
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Offer>() {
        override fun areContentsTheSame(oldItem: Offer, newItem: Offer): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Offer, newItem: Offer): Boolean {
            return oldItem.dealID == newItem.dealID
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Offer) -> Unit)? = null

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        Log.e("OFFERS ADAPTER -----> ", "${getItemCount()}")
        val item = differ.currentList[position]
        holder.itemView.apply {
           setOnClickListener {
               onItemClickListener?.let { it(item) }
           }
        }
        holder.bind(item)
    }

    fun setOnItemClickListener(listener: (Offer) -> Unit) {
        onItemClickListener = listener
    }

}