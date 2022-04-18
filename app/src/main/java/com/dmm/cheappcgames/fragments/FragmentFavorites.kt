package com.dmm.cheappcgames.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmm.cheappcgames.MainActivity
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.adapters.OffersAdapter
import com.dmm.cheappcgames.databinding.FragmentFavoritesBinding
import com.dmm.cheappcgames.ui.OffersViewModel

class FragmentFavorites() : Fragment() {

    private lateinit var _binding: FragmentFavoritesBinding
    private val binding get() = _binding

    lateinit var viewModel: OffersViewModel
    lateinit var offersAdapter: OffersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoritesBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        viewModel.getFavoritesGames().observe(viewLifecycleOwner) { it ->
            offersAdapter.submitList(it)
        }
        viewModel.resetGameById()
    }

    private fun setupRecyclerView() {
        val gameDistributor = viewModel.gamesDistributor.value?.data!!
        offersAdapter = OffersAdapter(gameDistributor)
        binding.rvFavorites.apply {
            adapter = offersAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}