package com.dmm.cheappcgames

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Job

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: OffersViewModel
    lateinit var navController: NavController
    private lateinit var menuItem: MenuItem
    private lateinit var searchView: SearchView
    lateinit var bottomNavigation: BottomNavigationView
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

        bottomNavigation = binding.bottomNavigation
        menuItem = binding.materialToolbar.menu.findItem(R.id.app_bar_search)
        searchView = menuItem.actionView as SearchView

        destinationChangedListener()
        menuItemEvents()
        searchViewEvents()
    }

    fun destinationChangedListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            showBottomNavigation()
            when(destination.id) {
                R.id.fragmentFilter -> {
                    setTitleMateriaToolbar(R.string.fragment_filter)
                    setVisibilityMenuItem(false)
                    binding.materialToolbar.collapseActionView()
                }
                R.id.fragmentsOffers -> {
                    setTitleMateriaToolbar(R.string.fragment_offers)
                    onNextClicked.invoke()
                    setVisibilityMenuItem(true)
                }
                R.id.fragmentFavorites -> {
                    setTitleMateriaToolbar(R.string.fragment_favorites)
                    setVisibilityMenuItem(false)
                    binding.materialToolbar.collapseActionView()
                }
                R.id.fragmentSearch -> {
                    hiddeBottomNavigation()
                    viewModel.resetSearchResponse()
                }
            }
        }
    }

    private fun setTitleMateriaToolbar(resId: Int) {
        binding.materialToolbar.title = getString(resId)
    }

    private fun showBottomNavigation() {
        bottomNavigation.visibility = View.VISIBLE
    }

    private fun setVisibilityMenuItem(visibility: Boolean) {
        menuItem.isVisible = visibility
    }

    private fun hiddeBottomNavigation() {
        bottomNavigation.visibility = View.GONE
    }

    fun searchViewEvents() {
        var job: Job? = null
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if(it.isNotEmpty()) {
                        viewModel.resetSearchResponse()
                        viewModel.searchText = query
                        viewModel.getDealsByTitle()
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun menuItemEvents() {
        menuItem.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                navController.navigate(R.id.action_fragmentsOffers_to_fragmentSearch)
                return false
            }

        })

        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                navController.navigate(R.id.action_fragmentSearch_to_fragmentsOffers)
                return true
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}