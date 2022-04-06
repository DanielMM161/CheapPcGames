package com.dmm.cheappcgames

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: OffersViewModel
    private lateinit var navController: NavController

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

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.fragmentsOffers))

        binding.materialToolbar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}