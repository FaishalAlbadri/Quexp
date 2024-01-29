package com.bintang.quexp.ui.fragment.profile

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.viewModels
import com.bintang.quexp.R
import com.bintang.quexp.adapter.ViewPagerAdapter
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.databinding.FragmentProfileBinding
import com.bintang.quexp.util.createAlertDialog
import com.bintang.quexp.util.createBitmapWithBorder
import com.bintang.quexp.util.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.google.android.material.tabs.TabLayoutMediator

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val profileViewModel: ProfileViewModel by viewModels { viewModel }
    val pageContent = arrayOf(
        "Lencana",
        "Dikunjungi"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelFactory.getInstance(requireContext())

        binding.apply {
            btnEditProfile.setOnClickListener {
                Toast.makeText(requireContext(), "Under Development", Toast.LENGTH_SHORT).show()
            }
            btnSetting.setOnClickListener {
                Toast.makeText(requireContext(), "Under Development", Toast.LENGTH_SHORT).show()
            }
        }

        profileViewModel.apply {
            userResponse.observe(viewLifecycleOwner) {
                it.apply {
                    val welcome = "Hello, ${it.userName.split(" ")[0]}"
                    val location = "${it.userCity}, Indonesia"

                    binding.apply {
                        Glide.with(requireContext())
                            .load(R.drawable.bg_profile)
                            .transform(CenterCrop(), RoundedCorners(4))
                            .into(imgBgProfile)

                        txtUsername.text = it.userName
                        txtWelcome.text = welcome
                        txtLocation.text = location
                        imgProfile.loadCircularImage(it.userImg, 8F)

                        vpProfile.adapter =
                            ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
                        TabLayoutMediator(tabProfile, vpProfile) { tab, position ->
                            tab.text = pageContent[position]
                        }.attach()
                    }

                }
            }
            isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
            message.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }
        profileViewModel.profile()
    }

    fun <T> ImageView.loadCircularImage(
        model: T,
        borderSize: Float = 0F,
        borderColor: Int = Color.WHITE
    ) {
        Glide.with(context)
            .asBitmap()
            .thumbnail(0.1f)
            .load(APIConfig.URL_IMG_PROFILE + model)
            .apply(RequestOptions.circleCropTransform())
            .into(object : BitmapImageViewTarget(this) {
                override fun setResource(resource: Bitmap?) {
                    setImageDrawable(
                        resource?.run {
                            RoundedBitmapDrawableFactory.create(
                                resources,
                                if (borderSize > 0) {
                                    createBitmapWithBorder(borderSize, borderColor)
                                } else {
                                    this
                                }
                            ).apply {
                                isCircular = true
                            }
                        }
                    )
                }
            })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loading.root.visibility = View.VISIBLE
        } else {
            binding.loading.root.visibility = View.GONE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}