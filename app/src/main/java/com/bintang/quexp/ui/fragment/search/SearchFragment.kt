package com.bintang.quexp.ui.fragment.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintang.quexp.adapter.PlacesAdapter
import com.bintang.quexp.adapter.PlacesSearchAdapter
import com.bintang.quexp.data.places.PlacesItem
import com.bintang.quexp.databinding.FragmentSearchBinding
import com.bintang.quexp.util.createAlertDialog
import com.bintang.quexp.util.onTextChangedListener
import com.bintang.quexp.util.viewmodel.ViewModelFactory

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val searchViewModel: SearchViewModel by viewModels { viewModel }
    private lateinit var placesAdapter: PlacesAdapter
    private lateinit var placesSearchAdapter: PlacesSearchAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelFactory.getInstance(requireContext())

        searchViewModel.apply {
            placesResponse.observe(viewLifecycleOwner) {
                setPlaces(it.placesPopuler)
                setSearch(it.places)
            }

            searchResponse.observe(viewLifecycleOwner) {
                setSearch(it)
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
        searchViewModel.places()

        binding.edtSearch.apply {
            onTextChangedListener {
                if (text.toString().length > 0){
                    searchViewModel.search(this.text.toString())
                }
            }
        }
    }

    private fun setPlaces(it: List<PlacesItem>) {
        placesAdapter = PlacesAdapter(it)
        binding.rvPlaces.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = placesAdapter
        }
    }

    private fun setSearch(it: List<PlacesItem>) {
        placesSearchAdapter = PlacesSearchAdapter(it)
        binding.rvSearch.apply {
            layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager
                    .HORIZONTAL, false
            )
            adapter = placesSearchAdapter
        }
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