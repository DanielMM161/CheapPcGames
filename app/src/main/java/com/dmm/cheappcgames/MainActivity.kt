package com.dmm.cheappcgames

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.dmm.cheappcgames.databinding.ActivityMainBinding
import com.dmm.cheappcgames.db.CheapPcDataBase
import com.dmm.cheappcgames.ui.OffersRepository
import com.dmm.cheappcgames.ui.OffersViewModel
import com.dmm.cheappcgames.ui.ViewModelFactory
import com.dmm.cheappcgames.utils.Constants
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: OffersViewModel
    lateinit var navController: NavController
    private lateinit var searchView: SearchView
    private lateinit var menuItem: MenuItem
    private lateinit var materiaToolbar: MaterialToolbar
    var onNextClicked: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val offersRepository = OffersRepository(CheapPcDataBase(this))
        val viewModelProviderFactory = ViewModelFactory(offersRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(OffersViewModel::class.java)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.fragmentsOffers, R.id.fragmentFavorites))

        binding.materialToolbar.setupWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)


        materiaToolbar = binding.materialToolbar
        menuItem = materiaToolbar.menu.findItem(R.id.app_bar_search)
        menuItem.setVisible(true)
        searchView = menuItem.actionView as SearchView

        destinationChangedListener()
        searchViewEvents()
    }

    fun destinationChangedListener() {
        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when(destination.id) {
                R.id.fragmentFilter -> {
                    setTitleMateriaToolbar(R.string.fragment_filter)
                    menuItem.setVisible(false)
                }
                R.id.fragmentsOffers -> {
                    setTitleMateriaToolbar(R.string.fragment_offers)
                    onNextClicked.invoke()
                    menuItem.setVisible(true)
                }
                R.id.fragmentFavorites -> {
                    setTitleMateriaToolbar(R.string.fragment_favorites)
                }
                else -> menuItem.setVisible(true)
            }
        }
    }

    private fun setTitleMateriaToolbar(resId: Int) {
        materiaToolbar.title = getString(resId)
    }

    fun searchViewEvents() {
        var job: Job? = null
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                job?.cancel()
                job = MainScope().launch {
                    delay(Constants.SEARCH_TIME_DELAY)
                    newText?.let {
                        if(newText.isNotEmpty()) {
                            viewModel.resetResponseSearch()
                            viewModel.searchText = newText
                            viewModel.getSearchOffers(newText.toString())
                        } else {
                            viewModel.searchText = ""
                            viewModel.resetResponseOffers()
                            viewModel.getOffers()
                        }
                    }
                }
                return false
            }
        })

        searchView.setOnCloseListener (object: SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                return false
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}