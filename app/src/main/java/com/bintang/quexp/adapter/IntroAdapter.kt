package com.bintang.quexp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.bintang.quexp.data.local.IntroData
import com.bintang.quexp.databinding.ItemIntroBinding

class IntroAdapter(
    val introList: List<IntroData>
) : PagerAdapter() {

    override fun getCount(): Int {
        return introList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    override fun instantiateItem(parent: ViewGroup, position: Int): Any {
        val binding = ItemIntroBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val introData: IntroData = introList.get(position)

        binding.apply {
            txtTitle.text = introData.title
            txtDesc.text = introData.desc
            imgIntro.setImageResource(introData.img)
        }

        parent.addView(binding.root)

        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }

}