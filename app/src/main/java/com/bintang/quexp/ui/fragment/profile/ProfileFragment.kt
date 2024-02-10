package com.bintang.quexp.ui.fragment.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bintang.quexp.R
import com.bintang.quexp.adapter.ViewPagerAdapter
import com.bintang.quexp.databinding.FragmentProfileBinding
import com.bintang.quexp.ui.setting.change.profile.ChangeProfileActivity
import com.bintang.quexp.ui.setting.SettingActivity
import com.bintang.quexp.util.loadCircularImage
import com.bintang.quexp.util.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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

        profileViewModel.apply {
            userResponse.observe(viewLifecycleOwner) {
                it.apply {
                    val welcome = "Hello, ${userName.split(" ")[0]}"
                    val location = "${userCity}, Indonesia"

                    binding.apply {
                        Glide.with(requireContext())
                            .load(R.drawable.bg_profile)
                            .transform(CenterCrop(), RoundedCorners(4))
                            .into(imgBgProfile)

                        txtUsername.text = userName
                        txtWelcome.text = welcome
                        txtLocation.text = location
                        imgProfile.loadCircularImage(userImg, 8F)

                        vpProfile.adapter =
                            ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
                        TabLayoutMediator(tabProfile, vpProfile) { tab, position ->
                            tab.text = pageContent[position]
                        }.attach()

                        btnEditProfile.setOnClickListener {
                            startActivity(
                                Intent(
                                    requireContext(),
                                    ChangeProfileActivity::class.java
                                )
                                    .putExtra("user_img", userImg)
                                    .putExtra("user_name", userName)
                                    .putExtra("user_email", userEmail)
                                    .putExtra("user_phone", userPhone)
                                    .putExtra("user_city", userCity)
                            )
                        }
                        btnSetting.setOnClickListener {
                            startActivity(
                                Intent(
                                    requireContext(),
                                    SettingActivity::class.java
                                )
                                    .putExtra("user_name", userName)
                                    .putExtra("user_img", userImg)
                            )
                        }
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loading.root.visibility = View.VISIBLE
        } else {
            binding.loading.root.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.profile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}