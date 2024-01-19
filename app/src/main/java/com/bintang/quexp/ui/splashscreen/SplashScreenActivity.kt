package com.bintang.quexp.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bintang.quexp.data.local.UserData
import com.bintang.quexp.databinding.ActivitySplashScreenBinding
import com.bintang.quexp.ui.home.HomeActivity
import com.bintang.quexp.ui.intro.IntroActivity
import com.bintang.quexp.util.viewmodel.ViewModelFactory
import java.util.Timer
import kotlin.concurrent.schedule

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private var _binding: ActivitySplashScreenBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val splashScreenViewModel: SplashScreenViewModel by viewModels { viewModel }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelFactory.getInstance(this)

        splashScreenViewModel.apply {
            getSessionUser().observe(this@SplashScreenActivity) {
                if (it.isLogin) {
                    profile()
                } else {
                    Timer("OpenIntro", false).schedule(3000) {
                        startActivity(Intent(this@SplashScreenActivity, IntroActivity::class.java))
                        finish()
                    }
                }
            }

            message.observe(this@SplashScreenActivity) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(this@SplashScreenActivity, it, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            userResponse.observe(this@SplashScreenActivity) {
                saveSession(UserData(it.idUser, it.userName, it.userToken, true))
                startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
                finish()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}