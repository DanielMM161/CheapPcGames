package com.dmm.cheappcgames.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.adapters.OffersAdapter
import com.dmm.cheappcgames.databinding.FragmentFavoritesBinding
import com.dmm.cheappcgames.resource.Resource
import com.dmm.cheappcgames.ui.DealsActivity
import com.dmm.cheappcgames.ui.OffersViewModel
import com.dmm.cheappcgames.utils.Utils
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
        viewModel = (activity as DealsActivity).viewModel
        val bottomview = (activity as DealsActivity).bottomNavigation
        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    subscribeObservableGameId()
                }
            }
        }

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

    private suspend fun subscribeObservableGameId() {
        viewModel.gameId.collect {
            when(it) {
                is Resource.Success -> {
                    it.data?.let { game ->
                        hiddenProgressBar()
                        val bundle = Bundle().apply {
                            putSerializable("game", game)
                        }
                        findNavController().navigate(R.id.action_fragmentFavorites_to_fragmentShowGame, bundle)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    hiddenProgressBar()
                    it.message.let { message ->
                        Utils.showToast(requireContext(), "An error occured: $message")
                    }
                }
                is Resource.ErrorCaught -> {
                    hiddenProgressBar()
                    val message = it.asString(requireContext())
                    Utils.showToast(requireContext(), "$message")
                }
            }
        }
    }
}