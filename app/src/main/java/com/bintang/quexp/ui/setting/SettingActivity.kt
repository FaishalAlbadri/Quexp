package com.bintang.quexp.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.databinding.ActivitySettingBinding
import com.bintang.quexp.ui.intro.IntroActivity
import com.bintang.quexp.ui.setting.change.password.ChangePasswordActivity
import com.bintang.quexp.util.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide


class SettingActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private var _binding: ActivitySettingBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val settingViewModel: SettingViewModel by viewModels { viewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelFactory.getInstance(this)

        setContentLayout()
    }

    private fun setContentLayout() {
        binding.apply {
            btnBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }

            Glide.with(this@SettingActivity)
                .load(APIConfig.URL_IMG_PROFILE + getDataIntent("user_img"))
                .circleCrop()
                .into(imgProfile)
            txtUsername.text = getDataIntent("user_name")

            itemChangePassword.apply {
                this.root.setOnClickListener {
                    startActivity(Intent(applicationContext, ChangePasswordActivity::class.java))
                }
                this.txtTitle.text = "Ganti Password"
            }

            itemTentangKami.apply {
                this.root.setOnClickListener {
                    Toast.makeText(
                        this@SettingActivity,
                        "Kami adalah penjelajah",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                this.txtTitle.text = "Tentang Kami"
            }

            itemKebijakanPrivasi.apply {
                this.root.setOnClickListener {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.privacypolicyonline.com/live.php?token=rUyHs8XE6NQKN6KVcjiA2a3Brwjmt1jn")
                        )
                    )
                }
                this.txtTitle.text = "Kebijakan Privasi"
            }

            itemSyaratKetentuan.apply {
                this.root.setOnClickListener {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.termsandconditionsgenerator.com/live.php?token=NEU1DjtgBB3C2KHEAkrQ6KdotBBasFmR")
                        )
                    )
                }
                this.txtTitle.text = "Syarat dan Ketentuan"
            }

            btnLogout.setOnClickListener {
                settingViewModel.logout()
                startActivity(
                    Intent(this@SettingActivity, IntroActivity::class.java)
                        .addFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                        )
                )
                finish()
            }
        }
    }

    fun getDataIntent(string: String): String {
        return intent.getStringExtra(string).toString()
    }

}