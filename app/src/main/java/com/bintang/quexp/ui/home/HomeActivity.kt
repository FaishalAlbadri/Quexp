package com.bintang.quexp.ui.home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.bintang.quexp.R
import com.bintang.quexp.databinding.ActivityHomeBinding
import com.bintang.quexp.util.setupWithNavController

class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNavigation()
    }

    private fun setUpNavigation() {
        val navHostFragment = this.supportFragmentManager
            .findFragmentById(R.id.nav_main_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (navController.currentDestination?.id) {
                    R.id.home -> finish()
                    R.id.search,
                    R.id.unusedScan,
                    R.id.ar,
                    R.id.profile -> {
                        binding.bottomNav.findViewById<View>(R.id.home)
                            .let {
                                it.requestFocus()
                                it.performClick()
                            }
                    }
                }
            }
        })
    }
}