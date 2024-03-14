package com.bintang.quexp.ui.fragment.ranking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintang.quexp.R
import com.bintang.quexp.adapter.RankingAdapter
import com.bintang.quexp.data.ranking.RankingItem
import com.bintang.quexp.databinding.FragmentRankingBinding
import com.bintang.quexp.util.loadCircularImage
import com.bintang.quexp.util.viewmodel.ViewModelFactory

class RankingFragment : Fragment() {

    private var _binding: FragmentRankingBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ViewModelFactory
    private val rankingViewModel: RankingViewModel by viewModels { viewModel }
    private lateinit var rankingAdapter: RankingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelFactory.getInstance(requireContext())
        rankingViewModel.apply {
            rankingResponse.observe(viewLifecycleOwner) {
                setRanking(it)
            }
            message.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
            ranking()
        }
    }

    private fun setRanking(it: List<RankingItem>) {
        val newRank: MutableList<RankingItem> = arrayListOf()
        it.forEachIndexed { index, rankingItem ->
            if (index > 2) {
                newRank.add(rankingItem)
            } else {
                binding.apply {
                    if (index == 0) {
                        imgRanking1.loadCircularImage(rankingItem.userImg, 8F, ContextCompat.getColor(requireContext(), R.color.gold))
                        txtUsernameRanking1.text = rankingItem.userName
                        txtRanking1.text = "${rankingItem.count} Lencana"
                    } else if (index == 1) {
                        imgRanking2.loadCircularImage(rankingItem.userImg, 8F, ContextCompat.getColor(requireContext(), R.color.silver))
                        txtUsernameRanking2.text = rankingItem.userName
                        txtRanking2.text = "${rankingItem.count} Lencana"
                    } else if (index == 2) {
                        imgRanking3.loadCircularImage(rankingItem.userImg, 8F, ContextCompat.getColor(requireContext(), R.color.bronze))
                        txtUsernameRanking3.text = rankingItem.userName
                        txtRanking3.text = "${rankingItem.count} Lencana"
                    }
                }
            }
        }

        if (newRank.size > 0) {
            rankingAdapter = RankingAdapter(newRank)
            binding.rvRanking.apply {
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(requireContext())
                adapter = rankingAdapter
            }
        } else {
            binding.rvRanking.visibility = View.GONE
        }
    }
}