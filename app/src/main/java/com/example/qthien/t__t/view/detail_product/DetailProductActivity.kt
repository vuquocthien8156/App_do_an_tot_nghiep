package com.example.qthien.t__t.view.detail_product

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.EvaluationAdapter
import com.example.qthien.t__t.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_detail_poduct.*

class DetailProductActivity : AppCompatActivity() {

    val arrImage = arrayListOf("https://product.hstatic.net/1000075078/product/caramel_macchiato_b6f694f469794e12b04a91845f5fce2d_master.jpg"
    ,"https://product.hstatic.net/1000075078/product/caramel_macchiato_b6f694f469794e12b04a91845f5fce2d_master.jpg"
    ,"https://product.hstatic.net/1000075078/product/caramel_macchiato_b6f694f469794e12b04a91845f5fce2d_master.jpg"
    ,"https://product.hstatic.net/1000075078/product/caramel_macchiato_b6f694f469794e12b04a91845f5fce2d_master.jpg"
    ,"https://product.hstatic.net/1000075078/product/caramel_macchiato_b6f694f469794e12b04a91845f5fce2d_master.jpg")

    var optionMenu : Menu? = null
    var adapterEvaluation : EvaluationAdapter? = null
    var txtCount : TextView? = null
    var mCartItemCount = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_detail_poduct)

        setSupportActionBar(toolbarDetailProduct)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(com.example.qthien.t__t.R.drawable.ic_arrow_back_white_24dp)

        setUpViewPager()

        setUpExpandTextView()

        setUpRecyclerEvaluation()
    }

    private fun setUpRecyclerEvaluation() {
        adapterEvaluation = EvaluationAdapter(this , arrayListOf())
        recyclerEvaluation.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recyclerEvaluation.adapter = adapterEvaluation
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.example.qthien.t__t.R.menu.menu_detail_product , menu)

        val menuItem = menu?.findItem(R.id.menu_carts)

        val actionView = menuItem?.getActionView()
        txtCount = actionView?.findViewById(R.id.cart_badge)

        setupBadge()

        actionView?.setOnClickListener { onOptionsItemSelected(menuItem) }

        optionMenu = menu
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupBadge() {

        if (txtCount != null) {
            if (mCartItemCount == 0) {
                if (txtCount?.getVisibility() != View.GONE) {
                    txtCount?.setVisibility(View.GONE)
                }
            } else {
                txtCount?.setText((Math.min(mCartItemCount, 99)).toString())
                if (txtCount?.visibility != View.VISIBLE) {
                    txtCount?.setVisibility(View.VISIBLE)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            com.example.qthien.t__t.R.id.menu_search ->{

            }
            com.example.qthien.t__t.R.id.menu_like ->{

            }
            com.example.qthien.t__t.R.id.menu_share -> {

            }
            com.example.qthien.t__t.R.id.menu_carts -> {

            }
            android.R.id.home ->{
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    fun setUpViewPager(){
        txtPage.setText("1/${arrImage.size}")

        viewpagerImgProducts.adapter = ViewPagerAdapter(this , arrImage)

        viewpagerImgProducts.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                txtPage.setText("${p0 +1}/${arrImage.size}")
            }

        })
    }

    fun setUpExpandTextView(){
        // but, you can also do the checks yourself
        btnReadMore.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View) {
                if (txtDeriptionProduct.isExpanded()) {
                    txtDeriptionProduct.collapse()
                    btnReadMore.setText(com.example.qthien.t__t.R.string.read_more)
                } else {
                    txtDeriptionProduct.expand()
                    btnReadMore.setText(com.example.qthien.t__t.R.string.collapse)
                }
            }
        })
    }
}
