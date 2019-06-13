package com.example.qthien.t__t.view.detail_product

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.ImageSmallAdapter
import com.example.qthien.t__t.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_view_full_screen_image.*

class ViewFullScreenImageActivity : AppCompatActivity() , ImageSmallAdapter.ImagesAdapterCommunication {

    override fun itemRecyclerSelected(position: Int) {
        viewpagerImg.setCurrentItem(position , true)
    }

    val arrUrl = ArrayList<String>()

    lateinit var adapterViewPager : ViewPagerAdapter
    lateinit var adapterImagesBottom : ImageSmallAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_full_screen_image)

        arrUrl.addAll(intent.extras?.getStringArrayList("arrImg")!!)

        adapterViewPager = ViewPagerAdapter(this , arrUrl)
        viewpagerImg.adapter = adapterViewPager

        adapterImagesBottom = ImageSmallAdapter(this , arrUrl)
        recyImagesBottom.layoutManager = LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false)
        recyImagesBottom.adapter = adapterImagesBottom

        val position = intent.extras?.getInt("position")

        adapterImagesBottom.setSelectedItem(position!!)
        viewpagerImg.setCurrentItem(position , true)
        recyImagesBottom.scrollToPosition(position)

        viewpagerImg.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(p0: Int) {
                recyImagesBottom.scrollToPosition(p0)
                adapterImagesBottom.setSelectedItem(p0)
            }
        })

        ibtnCloseFullScreen.setOnClickListener({
            finish()
        })
    }
}
