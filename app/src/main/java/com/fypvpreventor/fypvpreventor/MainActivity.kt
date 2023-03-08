package com.fypvpreventor.VpreventorFYP

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.fypvpreventor.VpreventorFYP.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var  navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //disable dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        //setup nav controller with bottom navigation
        binding.bottomNavigationView.setupWithNavController(navController)

        //minor setting for bottom navigation
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2).isEnabled = false

        //move to another activity
        binding.fabBottom.setOnClickListener {
//            val intent = Intent(applicationContext,SensorActivity::class.java)
//            startActivity(intent)
            navHostFragment.findNavController().navigate(R.id.sensorFragment)
        }
    }


}