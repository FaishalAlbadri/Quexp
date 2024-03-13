package com.bintang.quexp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bintang.quexp.ui.fragment.awards.AwardsFragment
import com.bintang.quexp.ui.fragment.ranking.RankingFragment
import com.bintang.quexp.ui.fragment.visited.VisitedFragment

private const val NUM_TABS = 3

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return AwardsFragment()
            1 -> return VisitedFragment()
        }
        return RankingFragment()
    }
}