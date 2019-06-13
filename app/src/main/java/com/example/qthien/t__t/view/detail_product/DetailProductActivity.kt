package com.example.qthien.t__t.view.detail_product

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.EvaluationAdapter
import com.example.qthien.t__t.adapter.ViewPagerAdapter
import com.example.qthien.t__t.model.Evaluation
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.model.ResponseEvaluation
import com.example.qthien.t__t.model.VoteQuantity
import com.example.qthien.t__t.presenter.detail_product.PreDetailProduct
import com.example.qthien.t__t.presenter.pre_product_favorite.PreProductFavoriteActi
import com.example.qthien.t__t.view.cart.AddToCartActivity
import com.example.qthien.t__t.view.cart.CartActivity
import com.example.qthien.t__t.view.ealuation.CreateEvaluationActivity
import com.example.qthien.t__t.view.ealuation.EvaluationActivity
import com.example.qthien.t__t.view.main.MainActivity
import com.example.qthien.t__t.view.product_favorite.IViewProductFavoriteActi
import com.example.qthien.t__t.view.search_product.SearchProductActivity
import kotlinx.android.synthetic.main.activity_detail_poduct.*
import java.text.DecimalFormat



class DetailProductActivity : AppCompatActivity() , IDetailProduct ,
        EvaluationAdapter.EvaluationAdapterCall ,View.OnClickListener , IViewProductFavoriteActi{

    override fun resultGetProductFavorite(arrResult: ArrayList<Product>?) {}

    override fun favoriteProduct(resultCode: String) {
        if(resultCode.equals("ok")){
            checkFavorite = !checkFavorite
            addOrRemoveFavorite()
        }
        else
            Toast.makeText(this , R.string.fail_again , Toast.LENGTH_LONG).show()
    }

    override fun addTks(idEvluated: Int) {
        idEvaluteSelected = idEvluated
        PreDetailProduct(this).addTks(idEvluated , MainActivity.customer!!.idCustomer)
    }

    override fun failureFavorite(message: String) {
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
    }

    override fun failureCallDetail(message: String) {
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
    }

    override fun successAddTks(status: String?) {
        if(status != null && status.equals("Success")) {
            addTksToSharePreference(idEvaluteSelected.toString())
            arrEvaluation.find { it.idEvaluation == idEvaluteSelected }!!.quantityTks += 1
            adapterEvaluation?.notifyDataSetChanged()
        }
        else
            Toast.makeText(this , R.string.fail_again , Toast.LENGTH_LONG).show()
    }

    override fun successCallDetail(response: ResponseEvaluation) {
        Log.d("setttttttt" , response.data.listIdEvaluation.toString())
        initSharePreferenceTksCusomer(response.data.listIdEvaluation.toSet())

        arrEvaluation.addAll(response.data.listEvaluttion)
        arrImage.addAll(response.data.listUrlProduct)
        txtPage.setText("1/${arrImage.size}")
        adapterEvaluation?.notifyDataSetChanged()
        adapterViewPager?.notifyDataSetChanged()
        voteQuantity = response.data.quantityVote
        if(response.data.quantityVote?.total != 0)
            setUpQuantityVote(response.data.quantityVote!!)

        lnEvaluation.visibility = View.VISIBLE
        progressLoader.visibility = View.GONE
        if(arrEvaluation.size == 0){
            txtNotEvaluation.visibility = View.VISIBLE
            btnReadMoreEvaluation.visibility = View.GONE
            btnWriteEvaluation.visibility = View.GONE
            lnInfoEvaluation.visibility = View.GONE
        }
    }

    lateinit var arrImage : ArrayList<String>

    private var optionMenu : Menu? = null
    private var adapterEvaluation : EvaluationAdapter? = null
    private lateinit var arrEvaluation  : ArrayList<Evaluation>
    private var adapterViewPager : ViewPagerAdapter? = null
    private var txtCount : TextView? = null
    private var mCartItemCount = 5
    private var product : Product? = null
    private var checkFavorite = false
    private var checkFavoriteOld = false
    private var voteQuantity : VoteQuantity? = null
    private var idEvaluteSelected = 0
    private val REQUEST_CODE_ORDER_NOW  = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_detail_poduct)

        product = intent.extras?.getParcelable("product")

        arrImage = ArrayList()
        arrEvaluation = ArrayList()

        setSupportActionBar(toolbarDetailProduct)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(com.example.qthien.t__t.R.drawable.ic_arrow_back_white_24dp)

        btnWriteEvaluation.setOnClickListener(this)
        ibtnAddEvaluation.setOnClickListener(this)

        setUpExpandTextView()
        setUpEvaluation()
        setUpInfoProduct()
        setUpViewPager()
        checkFavorite()
        PreDetailProduct(this).callDetailOrEvaluation(product!!.idProduct , MainActivity.customer!!.idCustomer , null , null , null)

        btnOrderNow.setOnClickListener({
            val i = Intent(this , AddToCartActivity::class.java)
            i.putExtra("product" , product)
            startActivityForResult(i , REQUEST_CODE_ORDER_NOW)
        })
    }


    override fun onClick(v: View?) {
        if(v?.id == R.id.btnWriteEvaluation || v?.id == R.id.ibtnAddEvaluation){
            val i = Intent(this ,  CreateEvaluationActivity::class.java)
            i.putExtra("url" , product?.imageProduct)
            i.putExtra("id" , product?.idProduct)
            i.putExtra("name" , product?.nameProduct)
            startActivity(i)
        }
    }

    fun addTksToSharePreference(idEvalute : String){
        val share =  getSharedPreferences("EvaluateTks" , Context.MODE_PRIVATE)
        val setTks = share.getStringSet("setTks" , null)
        Log.d("setttttttt" , setTks?.toString())
        if(setTks != null){
            setTks.add(idEvalute)
            share.edit().putStringSet("setTks" , setTks).apply()
        }
    }

    override fun onBackPressed() {
        createSharePre()
        super.onBackPressed()
    }

    fun createSharePre(){
        if(checkFavorite != checkFavoriteOld)
            getSharedPreferences("Favorite" , Context.MODE_PRIVATE).edit()
                    .putBoolean( "changeFavorite" , true).apply()
    }

    fun initSharePreferenceTksCusomer(arrTks : Set<String>){
        getSharedPreferences("EvaluateTks" , Context.MODE_PRIVATE).edit().
                putStringSet("setTks" , arrTks).apply()
        Log.d("setttttttt" , arrTks.toString())
    }

    @SuppressLint("SetTextI18n")
    fun setUpQuantityVote(quantityVote : VoteQuantity){
        txtTotalEvaluation.setText("${quantityVote.total} ${getString(R.string.vote)}")
        val five = (quantityVote.totalFive * 100) / quantityVote.total
        val four = (quantityVote.totalFour * 100) / quantityVote.total
        val three = (quantityVote.totalThree * 100) / quantityVote.total
        val two = (quantityVote.totalTwo * 100) / quantityVote.total
        val one = (quantityVote.totalOne * 100) / quantityVote.total

        progressFive.progress = five
        progressFour.progress = four
        progressThree.progress = three
        progressTwo.progress = two
        progressOne.progress = one

        txtPercentFive.setText("$five%")
        txtPercentFour.setText("$four%")
        txtPercentThree.setText("$three%")
        txtPercentTwo.setText("$two%")
        txtPercentOne.setText("$one%")

        val average = ( (quantityVote.totalFive.times(5)) + (quantityVote.totalFour.times(4)) +
                (quantityVote.totalThree.times(3)) + (quantityVote.totalTwo.times(2)) + (quantityVote.totalOne.times(1)) ).toDouble() / quantityVote.total

    }

    @SuppressLint("SetTextI18n")
    private fun setUpInfoProduct(){
        txtNameProduct.setText(product?.nameProduct)

        val format = DecimalFormat("###,###,###")

        txtPriceSProduct.setText(format.format(product?.priceProduct) + " đ")
        txtPriceMProduct.setText(format.format(product?.priceMProduct) + " đ")
        txtPriceLProduct.setText(format.format(product?.priceLProduct) + " đ")

        if(product?.priceProduct == 0L) {
            lnPrice.visibility = View.GONE
            line1.visibility = View.GONE
        }

        if(product?.priceMProduct == 0L) {
            lnPriceM.visibility = View.GONE
            line2.visibility = View.GONE
        }

        if(product?.priceLProduct == 0L) {
            lnPriceL.visibility = View.GONE
            line3.visibility = View.GONE
        }

        txtDeriptionProduct.setText(product?.decriptionProduct)

        arrImage.add(product?.imageProduct!!)
    }

    fun addOrRemoveFavorite(){
        val stringFavorite = getSharedPreferences("Favorite", Context.MODE_PRIVATE).getString("arrFavorite", null)
        if (stringFavorite != null) {
            val arrFavorite = ArrayList<String>(
                    stringFavorite.replace("[", "").replace("]", "").split(","))
            if(checkFavorite){
                arrFavorite.add(product!!.idProduct.toString())
            }
            else{
                arrFavorite.remove(product!!.idProduct.toString())
            }
            getSharedPreferences("Favorite" , Context.MODE_PRIVATE).edit()
                    .putString( "arrFavorite" , arrFavorite.toString()).apply()
        }
        setFavorite()
    }

    fun checkFavorite(){
        val stringFavorite = getSharedPreferences("Favorite", Context.MODE_PRIVATE).getString("arrFavorite", null)
        if (stringFavorite != null) {
            val arrFavorite = stringFavorite.replace("[", "").replace("]", "").split(",")
            if (arrFavorite.find { it.trim().equals(product?.idProduct.toString()) } != null) {
                checkFavorite = true
            } else
                checkFavorite = false
            checkFavoriteOld = checkFavorite
            setFavorite()
        }
    }

    fun setFavorite() {
        val menuFavorite = optionMenu?.findItem(com.example.qthien.t__t.R.id.menu_like)
        if (checkFavorite) {
            menuFavorite?.setIcon(com.example.qthien.t__t.R.drawable.ic_favorite_white_24dp)
        } else {
            menuFavorite?.setIcon(com.example.qthien.t__t.R.drawable.ic_favorite_border_white_24dp)
        }
        invalidateOptionsMenu()
    }

    private fun setUpEvaluation() {
        adapterEvaluation = EvaluationAdapter(this , arrEvaluation)
        recyclerEvaluation.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recyclerEvaluation.adapter = adapterEvaluation
        val dividerItemDecoration = DividerItemDecoration(recyclerEvaluation.getContext(),
                LinearLayoutManager.VERTICAL)
        recyclerEvaluation.addItemDecoration(dividerItemDecoration)

        btnReadMoreEvaluation.setOnClickListener({
            val i = Intent(this , EvaluationActivity::class.java)
            i.putExtra("vote_quantity" , voteQuantity)
            i.putExtra("idProduct" , product?.idProduct)
            startActivity(i)
        })
    }

    override fun onStart() {
        super.onStart()
        setupBadge()
        invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.example.qthien.t__t.R.menu.menu_detail_product , menu)
        Log.d("checkkkkkkkkkkkF" , "2")

        val menuFavorite = menu?.findItem(com.example.qthien.t__t.R.id.menu_like)
        if (checkFavorite) {
            menuFavorite?.setIcon(com.example.qthien.t__t.R.drawable.ic_favorite_white_30dp)
        } else {
            menuFavorite?.setIcon(com.example.qthien.t__t.R.drawable.ic_favorite_border_white_30dp)
        }


        val menuItem = menu?.findItem(com.example.qthien.t__t.R.id.menu_cartss)
        val actionView = menuItem?.getActionView()
        txtCount = actionView?.findViewById(com.example.qthien.t__t.R.id.cart_badge)
        val imgCart = actionView?.findViewById<ImageView>(com.example.qthien.t__t.R.id.ic_Cart)

        setupBadge()

        imgCart?.setOnClickListener {
            startActivity(Intent(this , CartActivity::class.java))
        }
        optionMenu = menu

        return super.onCreateOptionsMenu(menu)
    }

    private fun setupBadge() {
        val count = getSharedPreferences("QuantityPrice" , Context.MODE_PRIVATE)
                .getInt("quantity" , 0)
        mCartItemCount = count
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
                startActivity(Intent(this , SearchProductActivity::class.java))
            }
            com.example.qthien.t__t.R.id.menu_like ->{
                PreProductFavoriteActi(this).favoriteProduct(product!!.idProduct , MainActivity.customer!!.idCustomer ,
                        if(checkFavorite) 0 else 1)
            }
            com.example.qthien.t__t.R.id.menu_share -> {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
                i.putExtra(Intent.EXTRA_TEXT, "https://www.thecoffeehouse.com/products/caramel-macchiato")
                startActivity(Intent.createChooser(i, "Share URL"))
            }
            com.example.qthien.t__t.R.id.menu_cartss -> {
            }
            android.R.id.home ->{
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    fun setUpViewPager(){
        txtPage.setText("1/${arrImage.size}")
        adapterViewPager = ViewPagerAdapter(this , arrImage)

        viewpagerImgProducts.adapter = adapterViewPager
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
