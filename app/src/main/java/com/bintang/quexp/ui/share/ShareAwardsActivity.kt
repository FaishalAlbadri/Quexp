package com.bintang.quexp.ui.share

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.databinding.ActivityShareAwardsBinding
import com.bintang.quexp.util.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class ShareAwardsActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private var _binding: ActivityShareAwardsBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val shareViewModel: ShareViewModel by viewModels { viewModel }
    private lateinit var username: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivityShareAwardsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelFactory.getInstance(this)

        binding.apply {
            btnBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }
            btnShareFacebook.setOnClickListener { share() }
            btnShareInstagram.setOnClickListener { share() }
            btnShareWhatsapp.setOnClickListener { share() }
        }

        shareViewModel.apply {
            userResponse.observe(this@ShareAwardsActivity) {
                binding.apply {
                    username = it.userName
                    txtUsername.text = it.userName
                    Glide.with(this@ShareAwardsActivity)
                        .load(APIConfig.URL_IMG_PROFILE + it.userImg)
                        .transform(CenterCrop(), RoundedCorners(16))
                        .into(imgProfile)
                    Glide.with(this@ShareAwardsActivity)
                        .load(APIConfig.URL_IMG_AWARDS + getDataIntent("img"))
                        .centerInside()
                        .into(imgAwards)
                    txtAwards.text = getDataIntent("title")
                }
            }
            isLoading.observe(this@ShareAwardsActivity) {
                showLoading(it)
            }
            message.observe(this@ShareAwardsActivity) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(this@ShareAwardsActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
        shareViewModel.profile()
    }

    fun getDataIntent(string: String): String {
        return intent.getStringExtra(string).toString()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loading.root.visibility = View.VISIBLE
            binding.cvBadges.visibility = View.GONE
        } else {
            binding.loading.root.visibility = View.GONE
            binding.cvBadges.visibility = View.VISIBLE
        }
    }

    fun share() {
        val bitmap = Bitmap.createBitmap(
            binding.layoutAwards.width,
            binding.layoutAwards.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        binding.layoutAwards.draw(canvas)
        val uri =
            Uri.parse(MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null))
        val intent = Intent()
        intent.apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Hallo saya $username, telah mendapatkan penghargaan ${getDataIntent("title")}. Ayo download aplikasi Quexp sekarang!")
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/*"
        }
        startActivity(Intent.createChooser(intent, "Share to"))
    }
}