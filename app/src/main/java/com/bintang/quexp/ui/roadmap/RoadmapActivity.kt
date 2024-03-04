package com.bintang.quexp.ui.roadmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintang.quexp.R
import com.bintang.quexp.adapter.RoadmapAdapter
import com.bintang.quexp.data.roadmap.RoadmapItem
import com.bintang.quexp.databinding.ActivityRoadmapBinding
import com.bintang.quexp.util.createAlertDialog
import com.bintang.quexp.util.viewmodel.ViewModelFactory

class RoadmapActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private var _binding: ActivityRoadmapBinding? = null
    val binding get() = _binding!!

    private lateinit var viewModel: ViewModelFactory
    private val roadmapViewModel: RoadmapViewModel by viewModels { viewModel }
    private lateinit var roadmapAdapter: RoadmapAdapter
    private lateinit var idCategory: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        _binding = ActivityRoadmapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelFactory.getInstance(this)
        idCategory = getDataIntent("id")

        binding.apply {
            btnBack.setOnClickListener {
                onBackPressedCallback.handleOnBackPressed()
            }
            if (idCategory.equals("1")) {
                imgTopRoadmap.setImageResource(R.drawable.ic_top_nature)
                imgBottomRoadmap.setImageResource(R.drawable.ic_bottom_nature)
            } else if (idCategory.equals("2")) {
                imgTopRoadmap.setImageResource(R.drawable.ic_top_food)
                imgBottomRoadmap.setImageResource(R.drawable.ic_bottom_food)
            } else {
                imgTopRoadmap.setImageResource(R.drawable.ic_top_history)
                imgBottomRoadmap.setImageResource(R.drawable.ic_bottom_history)
            }
        }

        roadmapViewModel.apply {
            roadmapResponse.observe(this@RoadmapActivity) {
                setRoadmap(it)
            }
            isLoading.observe(this@RoadmapActivity) {
                showLoading(it)
            }
            message.observe(this@RoadmapActivity) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(this@RoadmapActivity, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        roadmapViewModel.roadmap(idCategory)
    }

    private fun setRoadmap(it: List<RoadmapItem>) {
        roadmapAdapter = RoadmapAdapter(it)
        binding.rvRoadmap.apply {
            layoutManager = LinearLayoutManager(
                this@RoadmapActivity, LinearLayoutManager
                    .HORIZONTAL, false
            )
            adapter = roadmapAdapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loading.root.visibility = View.VISIBLE
        } else {
            binding.loading.root.visibility = View.GONE
        }
    }

    fun getDataIntent(string: String): String {
        return intent.getStringExtra(string).toString()
    }

}