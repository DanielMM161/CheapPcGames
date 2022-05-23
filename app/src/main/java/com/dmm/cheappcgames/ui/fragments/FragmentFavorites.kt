package com.dmm.cheappcgames.ui.fragments

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.adapters.OffersAdapter
import com.dmm.cheappcgames.databinding.FragmentFavoritesBinding
import com.dmm.cheappcgames.ui.DealsActivity
import com.dmm.cheappcgames.ui.OffersViewModel
import com.dmm.cheappcgames.utils.Utils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FragmentFavorites() : BaseFragment<FragmentFavoritesBinding>(
    FragmentFavoritesBinding::inflate
) {

    private lateinit var offersAdapter: OffersAdapter
    private lateinit var viewModel: OffersViewModel

    override fun onViewCreated() {
        val bottomView: BottomNavigationView = (activity as DealsActivity).bottomNavigation
        setupRecyclerView()
        viewModel = (activity as DealsActivity).viewModel

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.getFavoritesDeals().collect {
                        offersAdapter.differ.submitList(it)
                        // Show or Hidde empty Layout
                        binding.emptyListLayout.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
                    }
                }
                launch {
                    Utils.subscribeObservableDealById(
                        viewModel,
                        requireContext(),
                        parentFragmentManager,
                        { hiddenProgressBar() },
                        { showProgressBar() }
                    )
                }
            }
        }
        deleteItemSwipeLeftRight(bottomView)
    }

    private fun deleteItemSwipeLeftRight(bottomView: BottomNavigationView) {
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
                Snackbar.make(binding.root, getString(R.string.delete_game, game.title), Snackbar.LENGTH_LONG).apply {
                    setAction(getString(R.string.undo)) {
                        viewModel.saveGame(game)
                    }
                    setAnchorView(bottomView)
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvFavorites)
        }
    }

    private fun setupRecyclerView() {
        binding.rvFavorites.apply {
            offersAdapter = OffersAdapter()
            adapter = offersAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        itemClickListener()
    }

    private fun itemClickListener() = offersAdapter.setOnItemClickListener {
        val gameId = it.gameID.toInt()
        val storeId = it.storeID
        viewModel.getGameById(gameId, storeId)
    }

    private fun hiddenProgressBar() {
        binding.progressbar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressbar.visibility = View.VISIBLE
    }
}