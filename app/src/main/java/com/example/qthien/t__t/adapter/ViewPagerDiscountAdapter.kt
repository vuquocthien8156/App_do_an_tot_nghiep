package com.example.qthien.t__t.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.GlideApp
import com.example.qthien.t__t.model.Discount
import com.example.qthien.t__t.mvp.view.discount.DetailDiscountActivity
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import kotlinx.android.synthetic.main.item_viewpager.view.*
import java.util.*



class ViewPagerDiscountAdapter(
    internal var context: Context,
    internal var listDiscount: ArrayList<Discount>
) : PagerAdapter() {
    internal var layoutInflater: LayoutInflater

    init {
        this.layoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return listDiscount.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = layoutInflater.inflate(com.example.qthien.t__t.R.layout.item_viewpager_slider, container, false)

        val discount = listDiscount.get(position)
        GlideApp.with(context)
            .load("${RetrofitInstance.baseUrl}/${listDiscount.get(position).image}")
            .into(view.imageView)

        view.layoutViewPager.setOnClickListener({
            val i = Intent(context, DetailDiscountActivity::class.java)
            i.putExtra("discount", discount)
            context.startActivity(i)
        })

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
