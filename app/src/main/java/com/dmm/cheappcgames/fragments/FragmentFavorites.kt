package com.dmm.cheappcgames.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmm.cheappcgames.MainActivity
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.adapters.OffersAdapter
import com.dmm.cheappcgames.databinding.FragmentFavoritesBinding
import com.dmm.cheappcgames.ui.OffersViewModel
import com.google.android.material.snackbar.Snackbar

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
        val bottomview = (activity as MainActivity).bottomNavigation
        setupRecyclerView()

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val game = offersAdapter.differ.currentList[position]
                viewModel.deleteGame(game)
                Snackbar.make(view, getString(R.string.delete_game, game.title), Snackbar.LENGTH_LONG).apply {
                    setAction(getString(R.string.undo)) {
                        viewModel.saveGame(game)
                    }
                    setAnchorView(bottomview)
                    show()
                }

            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvFavorites)
        }

        viewModel.getFavoritesGames().observe(viewLifecycleOwner) { it ->
            offersAdapter.differ.submitList(it)
            // Show or Hidde empty Layout
            binding.emptyListLayout.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupRecyclerView() = binding.rvFavorites.apply {
        offersAdapter = OffersAdapter()
        adapter = offersAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }
}