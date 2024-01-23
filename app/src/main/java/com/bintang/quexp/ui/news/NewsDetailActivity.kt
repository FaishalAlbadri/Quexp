package com.bintang.quexp.ui.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.bintang.quexp.R
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.databinding.ActivityNewsDetailBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class NewsDetailActivity : AppCompatActivity() {

    private var _binding: ActivityNewsDetailBinding? = null
    val binding get() = _binding!!

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivityNewsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            Glide.with(this@NewsDetailActivity)
                .load(APIConfig.URL_IMG_NEWS + getDataIntent("img"))
                .transform(CenterCrop(), GranularRoundedCorners(0F, 0F, 64F, 64F))
                .into(imgNews)
            txtTitle.text = getDataIntent("title")
            txtDesc.text = getDataIntent("desc")

            toolbarNews.apply {
                txtTitleBar.text = "Berita"
                btnBack.setOnClickListener {
                    onBackPressedCallback.handleOnBackPressed()
                }
            }
        }

    }

    fun getDataIntent(string: String): String {
        return intent.getStringExtra(string).toString()
    }

}