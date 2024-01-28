package com.bintang.quexp.ui.fragment.awards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintang.quexp.adapter.AwardsAdapter
import com.bintang.quexp.data.awards.AwardsItem
import com.bintang.quexp.databinding.FragmentAwardsBinding
import com.bintang.quexp.util.viewmodel.ViewModelFactory

class AwardsFragment : Fragment() {

    private var _binding: FragmentAwardsBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val awardsViewModel: AwardsViewModel by viewModels { viewModel }
    private lateinit var awardsAdapter: AwardsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAwardsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelFactory.getInstance(requireContext())

        awardsViewModel.apply {
            awardsResponse.observe(viewLifecycleOwner) {
                setAwards(it)
            }
            message.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }
        awardsViewModel.awards()
    }

    private fun setAwards(it: List<AwardsItem>) {
        awardsAdapter = AwardsAdapter(it)
        binding.rvAwards.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = awardsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}