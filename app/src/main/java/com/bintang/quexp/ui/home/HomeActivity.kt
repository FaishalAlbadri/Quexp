package com.bintang.quexp.ui.home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.bintang.quexp.R
import com.bintang.quexp.databinding.ActivityHomeBinding
import com.bintang.quexp.util.PreferencesViewModel
import com.bintang.quexp.util.setupWithNavController
import com.bintang.quexp.util.viewmodel.ViewModelFactory
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence

class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val preferencesViewModel: PreferencesViewModel by viewModels { viewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelFactory.getInstance(this)
        setUpNavigation()

        preferencesViewModel.apply {
            getSessionUser().observe(this@HomeActivity) {
                if (!it.featureDiscovery) {
                    setUpTapTargetView()
                }
            }
        }
    }

    private fun setUpTapTargetView() {
        val tapTargetView = TapTargetSequence(this)
            .targets(
                TapTarget
                    .forView(
                        binding.bottomNav.findViewById(R.id.home),
                        "Menu Utama",
                        "Terdapat misi, berita, dan rekomendasi wisata."
                    )
                    .outerCircleColor(R.color.colorAccent)
                    .cancelable(false),
                TapTarget
                    .forView(
                        binding.bottomNav.findViewById(R.id.search),
                        "Mencari Wisata",
                        "Terdapat 3 jenis destinasi wisata. Cari dan lakukan perjalanan wisata."
                    )
                    .outerCircleColor(R.color.colorAccent)
                    .cancelable(false),
                TapTarget
                    .forView(
                        binding.bottomNav.findViewById(R.id.scan),
                        "Pindai Quexp",
                        "Cari qrcode Quexp dan dapatkan medali setelah menyelesaikan misi"
                    )
                    .outerCircleColor(R.color.colorAccent)
                    .cancelable(false),
                TapTarget
                    .forView(
                        binding.bottomNav.findViewById(R.id.ar),
                        "Augmented Reality",
                        "Mainkan dan lihat landmark ikonik."
                    )
                    .outerCircleColor(R.color.colorAccent)
                    .cancelable(false),
                TapTarget
                    .forView(
                        binding.bottomNav.findViewById(R.id.profile),
                        "Profil Kamu",
                        "Lihat destinasi yang telah dikunjungi dan medali apa saja yang sudah kamu raih."
                    )
                    .outerCircleColor(R.color.colorAccent)
                    .cancelable(false)

            ).listener(object : TapTargetSequence.Listener {
                override fun onSequenceFinish() {
                    preferencesViewModel.saveFeatureDiscovery()
                }

                override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {
                }

                override fun onSequenceCanceled(lastTarget: TapTarget?) {
                }

            })

        tapTargetView.start()
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
                    R.id.scan,
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