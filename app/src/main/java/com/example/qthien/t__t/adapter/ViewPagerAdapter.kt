package com.example.qthien.t__t.adapter

import android.content.Context
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.qthien.t__t.GlideApp
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import com.example.qthien.t__t.mvp.view.detail_product.DetailProductActivity
import com.example.qthien.t__t.mvp.view.detail_product.ViewFullScreenImageActivity
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
        val view = layoutInflater.inflate(com.example.qthien.t__t.R.layout.item_viewpager, container, false)

        GlideApp.with(context)
            .load("${RetrofitInstance.baseUrl}/${listImg.get(position)}")
            .into(view.imageView)

        if(context is ViewFullScreenImageActivity) {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            params.setMargins(12, 12, 12, 12)
            view.imageView.setLayoutParams(params)
        }

        if(context is DetailProductActivity) {
            view.layoutViewPager.setOnClickListener({
                val i = Intent(context, ViewFullScreenImageActivity::class.java)
                i.putExtra("position", position)
                i.putExtra("arrImg", listImg)
                Log.d("ádasdfsaf", "aaa")
                context.startActivity(i)
            })
        }

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
