package com.example.qthien.t__t.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import com.example.qthien.t__t.view.main.TabFragment

class ViewPagerTabAdapter(fm: FragmentManager ,internal var arrTitle : ArrayList<String>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        val fragment: Fragment
        Log.d("Positionnnn" , position.toString())
        fragment = when(position){
            0 -> TabFragment.newInstance(1)
            1 -> TabFragment.newInstance(1)
            2 -> TabFragment.newInstance(2)
            else -> TabFragment.newInstance(2)
        }
        return fragment
    }

    override fun getCount(): Int {
        return arrTitle.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return arrTitle[position]
    }
}
