package com.bintang.quexp.ui.fragment.visited

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bintang.quexp.adapter.VisitedAdapter
import com.bintang.quexp.data.visited.VisitedItem
import com.bintang.quexp.databinding.FragmentVisitedBinding
import com.bintang.quexp.util.viewmodel.ViewModelFactory

class VisitedFragment : Fragment() {

    private var _binding: FragmentVisitedBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val visitedViewModel: VisitedViewModel by viewModels { viewModel }
    private lateinit var visitedAdapter: VisitedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVisitedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelFactory.getInstance(requireContext())

        visitedViewModel.apply {
            visitedResponse.observe(viewLifecycleOwner) {
                setVisited(it)
            }
            message.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }
        visitedViewModel.visited()
    }

    private fun setVisited(it: List<VisitedItem>) {
        visitedAdapter = VisitedAdapter(it)
        binding.rvVisited.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = visitedAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}