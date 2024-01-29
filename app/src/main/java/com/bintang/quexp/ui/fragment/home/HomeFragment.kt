package com.bintang.quexp.ui.fragment.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintang.quexp.adapter.NewsAdapter
import com.bintang.quexp.api.APIConfig
import com.bintang.quexp.data.awards.AwardsItem
import com.bintang.quexp.data.news.NewsItem
import com.bintang.quexp.databinding.FragmentHomeBinding
import com.bintang.quexp.ui.roadmap.RoadmapActivity
import com.bintang.quexp.util.createAlertDialog
import com.bintang.quexp.util.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val homeViewModel: HomeViewModel by viewModels { viewModel }
    private lateinit var newsAdapter: NewsAdapter

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
            btnRoadmap.setOnClickListener {
                startActivity(Intent(requireActivity(), RoadmapActivity::class.java))
            }
        }

        homeViewModel.apply {
            getSessionUser().observe(viewLifecycleOwner) {
                binding.txtUsername.text = it.user_name
            }

            awardsResponse.observe(viewLifecycleOwner) {
                setRoadmap(it)
            }

            newsResponse.observe(viewLifecycleOwner) {
                awards()
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
        }

        homeViewModel.news()
    }

    private fun setRoadmap(it: AwardsItem) {
        it.apply {
            binding.apply {
                txtRoadmapValue.text = awardsRuleDesc
                Glide.with(requireContext())
                    .load(APIConfig.URL_IMG_AWARDS_BANNER + awardsRuleImg)
                    .centerInside()
                    .into(imgAwards)
            }
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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.apply {
                loading.root.visibility = View.VISIBLE
                cvHome.visibility = View.GONE
            }
        } else {
            binding.apply {
                loading.root.visibility = View.GONE
                cvHome.visibility = View.VISIBLE
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}