package com.example.qthien.t__t.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R
import com.example.qthien.week3_ryder.GlideApp
import kotlinx.android.synthetic.main.item_viewpager.view.*
import java.util.*

class ViewPagerAdapter(
    internal var context: Context,
    internal var listImg: ArrayList<String>
) : PagerAdapter() {
    internal var layoutInflater: LayoutInflater

    init {
        this.layoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return listImg.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = layoutInflater.inflate(R.layout.item_viewpager, container, false)

        GlideApp.with(context)
            .load(listImg.get(position))
            .fitCenter()
            .into(view.imageView)

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
