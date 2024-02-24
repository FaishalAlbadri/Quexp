package com.bintang.quexp.ui.intro

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.bintang.quexp.R
import com.bintang.quexp.adapter.IntroAdapter
import com.bintang.quexp.data.local.IntroData
import com.bintang.quexp.databinding.ActivityIntroBinding
import com.bintang.quexp.ui.location.LocationActivity
import com.bintang.quexp.ui.login.LoginActivity

class IntroActivity : AppCompatActivity() {

    private var _binding: ActivityIntroBinding? = null
    val binding get() = _binding!!
    private lateinit var introAdapter: IntroAdapter
    private val introList: List<IntroData> = listOf(
        IntroData(
            "Temukan Tempat Wisata Baru",
            "memandu Anda untuk mengeksplorasi tempat-tempat wisata baru dan unik",
            R.drawable.intro1
        ),
        IntroData(
            "Bermain Dengan AR",
            "menghadirkan augmented reality (AR) yang seru dan interaktif langsung di tangan Anda",
            R.drawable.intro2
        ),
        IntroData(
            "Raih Pencapaian",
            "Mengejar pencapaian destinasi yang menghibur",
            R.drawable.intro3
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        introAdapter = IntroAdapter(introList)

        binding.apply {

            vpIntro.adapter = introAdapter
            vpIntro.addOnPageChangeListener(viewListener)

            btnStart.setOnClickListener {
                if (vpIntro.currentItem == 2) {
                    if (ContextCompat.checkSelfPermission(this@IntroActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this@IntroActivity, LocationActivity::class.java))
                        finish()
                    }
                } else {
                    vpIntro.currentItem = vpIntro.currentItem + 1
                }
            }
        }
    }

    var viewListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            binding.apply {
                if (position == 0) {
                    btnStart.text = getString(R.string.intro_button_start)
                    dotOne.setImageResource(R.drawable.dot_on)
                    dotTwo.setImageResource(R.drawable.dot_off)
                    dotThree.setImageResource(R.drawable.dot_off)
                } else if (position == 1) {
                    btnStart.text = getString(R.string.intro_button_start)
                    dotOne.setImageResource(R.drawable.dot_off)
                    dotTwo.setImageResource(R.drawable.dot_on)
                    dotThree.setImageResource(R.drawable.dot_off)
                } else {
                    btnStart.text = getString(R.string.intro_button_discover)
                    dotOne.setImageResource(R.drawable.dot_off)
                    dotTwo.setImageResource(R.drawable.dot_off)
                    dotThree.setImageResource(R.drawable.dot_on)
                }
            }
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}