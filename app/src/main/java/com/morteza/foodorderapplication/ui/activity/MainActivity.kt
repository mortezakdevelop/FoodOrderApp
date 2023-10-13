package com.morteza.foodorderapplication.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.morteza.foodorderapplication.R
import com.morteza.foodorderapplication.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHost: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainBottomNav.background = null

        navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        binding.mainBottomNav.setupWithNavController(navHost.navController)

    }

    override fun onNavigateUp(): Boolean {
        return navHost.navController.navigateUp() || super.onNavigateUp()
    }
}