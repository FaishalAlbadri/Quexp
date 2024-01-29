package com.bintang.quexp.ui.places

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.NestedScrollView
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.databinding.ActivityPlacesDetailBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners

class PlacesDetailActivity : AppCompatActivity() {

    private var _binding: ActivityPlacesDetailBinding? = null
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
        _binding = ActivityPlacesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            toolbarPlaces.apply {
                viewBar.visibility = View.GONE
                txtTitleBar.text = getDataIntent("place_title")
                btnBack.setOnClickListener {
                    onBackPressedCallback.handleOnBackPressed()
                }
            }

            Glide.with(this@PlacesDetailActivity)
                .load(APIConfig.URL_IMG_PLACES + getDataIntent("place_img"))
                .transform(CenterCrop(), GranularRoundedCorners(20F, 20F, 20F, 20F))
                .into(imgPlaces)

            txtPlaces.text = getDataIntent("place_title")
            txtCity.text = getDataIntent("place_city")
            txtRating.text = getDataIntent("place_rating")
            txtDesc.text = getDataIntent("place_desc")

            btnMaps.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getDataIntent("place_gmaps"))))
            }

            nestedScrollPlaces.setOnScrollChangeListener(object :
                NestedScrollView.OnScrollChangeListener {
                override fun onScrollChange(
                    v: NestedScrollView,
                    scrollX: Int,
                    scrollY: Int,
                    oldScrollX: Int,
                    oldScrollY: Int
                ) {
                    if (scrollY > 0) {
                        btnMaps.visibility = View.GONE
                    } else {
                        btnMaps.visibility = View.VISIBLE
                    }
                }

            })
        }
    }

    fun getDataIntent(string: String): String {
        return intent.getStringExtra(string).toString()
    }
}