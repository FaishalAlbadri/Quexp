package com.bintang.quexp.ui.fragment.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintang.quexp.adapter.NewsAdapter
import com.bintang.quexp.adapter.BannerAdapter
import com.bintang.quexp.adapter.CategoryRoadmapAdapter
import com.bintang.quexp.adapter.PlacesAdapter
import com.bintang.quexp.data.banner.BannerItem
import com.bintang.quexp.data.category.CategoryItem
import com.bintang.quexp.data.news.NewsItem
import com.bintang.quexp.data.places.PlacesItem
import com.bintang.quexp.databinding.FragmentHomeBinding
import com.bintang.quexp.ui.notification.NotificationActivity
import com.bintang.quexp.util.viewmodel.ViewModelFactory
import com.smarteist.autoimageslider.SliderView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val homeViewModel: HomeViewModel by viewModels { viewModel }
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var categoryRoadmapAdapter: CategoryRoadmapAdapter
    private lateinit var placesAdapter: PlacesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelFactory.getInstance(requireContext())

        binding.apply {
            btnNotification.setOnClickListener {
                startActivity(Intent(requireActivity(), NotificationActivity::class.java))
            }
        }

        homeViewModel.apply {
            getSessionUser().observe(viewLifecycleOwner) {
                binding.txtUsername.text = it.user_name
            }

            bannerResponse.observe(viewLifecycleOwner) {
                setBanner(it)
            }

            categoryResponse.observe(viewLifecycleOwner) {
                setCategoryRoadmap(it)
            }

            placesResponse.observe(viewLifecycleOwner) {
                setPlaces(it)
            }

            newsResponse.observe(viewLifecycleOwner) {
                setNews(it)
            }

            isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
            message.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
            home()
        }
    }

    private fun setNews(it: List<NewsItem>) {
        newsAdapter = NewsAdapter(it)
        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager
                    .HORIZONTAL, false
            )
            adapter = newsAdapter
        }
    }

    private fun setPlaces(it: List<PlacesItem>) {
        placesAdapter = PlacesAdapter(it)
        binding.rvPlaces.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = placesAdapter
        }
    }

    private fun setCategoryRoadmap(it: List<CategoryItem>) {
        categoryRoadmapAdapter = CategoryRoadmapAdapter(it)
        binding.rvRoadmap.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = categoryRoadmapAdapter
        }
    }

    private fun setBanner(it: List<BannerItem>) {
        bannerAdapter = BannerAdapter(it)
        binding.apply {
            sliderBanner.apply {
                autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
                setSliderAdapter(bannerAdapter)
                scrollTimeInSec = 2
                isAutoCycle = true
                startAutoCycle()
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.apply {
                loading.root.visibility = View.VISIBLE
                scrollHome.visibility = View.GONE
            }
        } else {
            binding.apply {
                loading.root.visibility = View.GONE
                scrollHome.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}