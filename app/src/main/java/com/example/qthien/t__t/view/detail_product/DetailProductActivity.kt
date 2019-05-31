package com.example.qthien.t__t.view.detail_product

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.EvaluationAdapter
import com.example.qthien.t__t.adapter.ViewPagerAdapter
import com.example.qthien.t__t.model.Product
import kotlinx.android.synthetic.main.activity_detail_poduct.*
import java.text.DecimalFormat

class DetailProductActivity : AppCompatActivity() {

    val arrImage = arrayListOf("https://product.hstatic.net/1000075078/product/caramel_macchiato_b6f694f469794e12b04a91845f5fce2d_master.jpg"
    ,"https://product.hstatic.net/1000075078/product/caramel_macchiato_b6f694f469794e12b04a91845f5fce2d_master.jpg"
    ,"https://product.hstatic.net/1000075078/product/caramel_macchiato_b6f694f469794e12b04a91845f5fce2d_master.jpg"
    ,"https://product.hstatic.net/1000075078/product/caramel_macchiato_b6f694f469794e12b04a91845f5fce2d_master.jpg"
    ,"https://product.hstatic.net/1000075078/product/caramel_macchiato_b6f694f469794e12b04a91845f5fce2d_master.jpg")

    private var optionMenu : Menu? = null
    private var adapterEvaluation : EvaluationAdapter? = null
    private var txtCount : TextView? = null
    private var mCartItemCount = 5
    private var product : Product? = null
    private var checkFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_detail_poduct)

        product = intent.extras?.getParcelable("product")

        setSupportActionBar(toolbarDetailProduct)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(com.example.qthien.t__t.R.drawable.ic_arrow_back_white_24dp)

        setUpViewPager()
        setUpExpandTextView()
        setUpRecyclerEvaluation()
        setUpInfoProduct()
        checkFavorite()
    }

    @SuppressLint("SetTextI18n")
    private fun setUpInfoProduct(){
        txtNameProduct.setText(product?.nameProduct)

        val format = DecimalFormat("###,###,###")
        txtPriceSProduct.setText(format.format(product?.priceProduct) + " đ")
        txtPriceMProduct.setText(format.format(product?.priceMProduct) + " đ")
        txtPriceLProduct.setText(format.format(product?.priceLProduct) + " đ")

        txtDeriptionProduct.setText(product?.decriptionProduct)
    }

    fun addOrRemoveFavorite(id : Int , type : Boolean){
        val stringFavorite = getSharedPreferences("Favorite", Context.MODE_PRIVATE).getString("arrFavorite", null)
        if (stringFavorite != null) {
            val arrFavorite = ArrayList<String>(
                    stringFavorite.replace("[", "").replace("]", "").split(","))
            if(type){
                arrFavorite.add(id.toString())
            }
            else{
                arrFavorite.remove(id.toString())
            }
            getSharedPreferences("Favorite" , Context.MODE_PRIVATE).edit()
                    .putString( "arrFavorite" , arrFavorite.toString()).apply()
        }
    }

    fun checkFavorite(){
        val stringFavorite = getSharedPreferences("Favorite", Context.MODE_PRIVATE).getString("arrFavorite", null)
        if (stringFavorite != null) {
            val arrFavorite = stringFavorite.replace("[", "").replace("]", "").split(",")
            if (arrFavorite.find { it.trim().equals(product?.idProduct.toString()) } != null) {
                checkFavorite = true
            } else
                checkFavorite = false
            Log.d("checkkkkkkkkkkkF" , checkFavorite.toString())
            setFavorite()
        }
    }

    fun setFavorite() {
        val menuFavorite = optionMenu?.findItem(R.id.menu_like)
        if (checkFavorite) {
            menuFavorite?.setIcon(R.drawable.ic_favorite_white_24dp)
        } else {
            menuFavorite?.setIcon(R.drawable.ic_favorite_border_white_24dp)
        }
        invalidateOptionsMenu()
    }

    private fun setUpRecyclerEvaluation() {
        adapterEvaluation = EvaluationAdapter(this , arrayListOf())
        recyclerEvaluation.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recyclerEvaluation.adapter = adapterEvaluation
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.example.qthien.t__t.R.menu.menu_detail_product , menu)
        Log.d("checkkkkkkkkkkkF" , "2")

        val menuItem = menu?.findItem(R.id.menu_carts)

        val menuFavorite = menu?.findItem(R.id.menu_like)
        if (checkFavorite) {
            menuFavorite?.setIcon(R.drawable.ic_favorite_white_30dp)
        } else {
            menuFavorite?.setIcon(R.drawable.ic_favorite_border_white_30dp)
        }

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
