package com.example.qthien.t__t.mvp.view.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.qthien.t__t.GlideApp
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.NewsAdapter
import com.example.qthien.t__t.adapter.ViewPagerDiscountAdapter
import com.example.qthien.t__t.model.Customer
import com.example.qthien.t__t.model.Discount
import com.example.qthien.t__t.model.News
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.mvp.presenter.discount.PreDiscount
import com.example.qthien.t__t.mvp.presenter.pre_frag_news.PreFragNews
import com.example.qthien.t__t.mvp.presenter.pre_frag_news.PreQuantityAndPrice
import com.example.qthien.t__t.mvp.presenter.pre_login.PreLogin
import com.example.qthien.t__t.mvp.presenter.pre_product_favorite.PreProductFavoriteActi
import com.example.qthien.t__t.mvp.view.branch.BranchActivity
import com.example.qthien.t__t.mvp.view.customer.CustomerActivity
import com.example.qthien.t__t.mvp.view.discount.DiscountActivity
import com.example.qthien.t__t.mvp.view.discount.IDiscount
import com.example.qthien.t__t.mvp.view.point.BarcodeActivity
import com.example.qthien.t__t.mvp.view.point.HistoryPointActivity
import com.example.qthien.t__t.mvp.view.product_favorite.IViewProductFavoriteActi
import com.example.qthien.t__t.mvp.view.view_login.ILogin
import com.example.qthien.t__t.mvp.view.view_login.LoginActivity
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.HttpMethod
import com.facebook.accountkit.Account
import com.facebook.accountkit.AccountKit
import com.facebook.accountkit.AccountKitCallback
import com.facebook.accountkit.AccountKitError
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : Fragment() , IViewFragNews, ILogin ,
        IViewProductFavoriteActi ,
        IQuantityAndPrice ,
        IDiscount{

    override fun favoriteProduct(resultCode: String) {}
    override fun failureFavorite(message: String) {}
    override fun resultRegisterAccount(idUser: Int?) {}

    override fun failureNews(message: String) {
        Log.d("errorrNew" , message)
        Toast.makeText(context , message , Toast.LENGTH_LONG).show()
    }

    override fun failureQuantityAndPrice(message: String) {
        Log.d("errorrNew" , message)
        Toast.makeText(context , message , Toast.LENGTH_LONG).show()
    }

    override fun successGetDiscount(arrDiscount: ArrayList<Discount>) {
        arrPoster.addAll(arrDiscount)
        adapterSlider.notifyDataSetChanged()
        viewpagerMain.setScrollFactor(5.0)
        viewpagerMain.setOffscreenPageLimit(4)
        viewpagerMain.startAutoScroll(4000)
        indicator.setViewPager(viewpagerMain)

        relaNews.isEnabled = true
        frmLoader.visibility = View.GONE
        communicationToMain?.visibleNavigation(true)
    }

    override fun successQuantityAndPrice(quantity: Int, price: Int) {
        if(quantity != -1){
            val share = context!!.getSharedPreferences("QuantityPrice" , Activity.MODE_PRIVATE).edit()
            share.putInt("quantity" , quantity)
            share.putInt("price" , price)
            share.apply()
        }
    }

    override fun successGetNews(arrNews: ArrayList<News>) {
        if(arrNews.size > 0)
            this.arrNews.addAll(arrNews)
        adapterNew.notifyDataSetChanged()
    }

    override fun resultGetProductFavorite(arrResult: ArrayList<Product>?) {
        if(arrResult != null){
            val arrIdFavorite = arrResult.map { it.idProduct.toString().trim() }
            context?.getSharedPreferences("Favorite" , Context.MODE_PRIVATE)?.edit()
                ?.putString( "arrFavorite" , arrIdFavorite.toString())?.apply()

            val r = arrIdFavorite.toString()
            Log.d("arrrFavorite" , r)
        }
    }

    override fun failure(message: String) {
        Toast.makeText(context , message , Toast.LENGTH_LONG).show()
    }

    override fun resultLoginAccount(customer: Customer?) {
        MainActivity.customer = customer
        if(customer != null)
            setLogin(customer)
        else
            setLogout()
        Log.d("emaillll" , "News" + customer.toString())
    }

    override fun resultLoginPhone(customer: Customer?) {
        MainActivity.customer = customer
        if(customer != null)
            setLogin(customer)
        else
            setLogout()
    }

    override fun resultLoginFacebook(customer: Customer?) {
        Log.d("customerFB" , MainActivity.customer.toString())
        if(customer != null) {
            MainActivity.customer = customer
            setLogin(MainActivity.customer!!)
        }else
            setLogout()
    }


    companion object {
        fun newInstance() : NewsFragment = NewsFragment()
    }

    interface FragmentNewsCommnunicationMain{
        fun checkedFragmentOrder(discount : Discount? = null)
        fun visibleNavigation(boolean: Boolean)
        fun clearSharePreAndLocation()
    }

    var communicationToMain : FragmentNewsCommnunicationMain? = null
    lateinit var arrNews : ArrayList<News>
    lateinit var adapterNew : NewsAdapter
    val REQUEST_CODE_LOGIN = 1
    val REQUEST_CODE_DISCOUNT = 2
    var tagg = ""
    var loadDialog = false
    var scrollDowm = false

    lateinit var arrPoster : ArrayList<Discount>
    lateinit var adapterSlider : ViewPagerDiscountAdapter
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is FragmentNewsCommnunicationMain)
            communicationToMain = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_news , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        relaNews.isEnabled = false
        frmLoader.isEnabled = false

        PreDiscount(this).getDiscount(1)
        arrPoster = ArrayList()
        adapterSlider = ViewPagerDiscountAdapter(context!! , arrPoster)
        viewpagerMain.setAdapter(adapterSlider)

        arrNews = ArrayList()
        recylerMain.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)
        adapterNew = NewsAdapter(context!! , arrNews)
        recylerMain.adapter = adapterNew
        recylerMain.isNestedScrollingEnabled = false

        btnToOrder.setOnClickListener({
            if(MainActivity.customer != null)
                communicationToMain?.checkedFragmentOrder()
            else{
                questionLoginOrder()
            }
        })

        btnBranch.setOnClickListener {
            startActivity(Intent(context , BranchActivity::class.java))
        }
        btnBarcode.setOnClickListener({
            if(MainActivity.customer != null)
                startActivity(Intent(context , BarcodeActivity::class.java))
            else {
                startActivityForResult(Intent(context, LoginActivity::class.java), REQUEST_CODE_LOGIN)
                tagg = "barcode"
            }
        })


        btnDiscount.setOnClickListener({
            startActivityForResult(Intent(context , DiscountActivity::class.java) , REQUEST_CODE_DISCOUNT)
        })

        btnLoginNowNews.setOnClickListener({
            startActivityForResult(Intent(context , LoginActivity::class.java) , REQUEST_CODE_LOGIN)
        })

        txtReadAll.setOnClickListener({
            startActivity(Intent(context , NewsActivity::class.java))
        })

        txtPointUser.setOnClickListener({
                startActivity(Intent(context , HistoryPointActivity::class.java))
        })

        checkLogin()

        nestedNews.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
//                Toast.makeText(context, "Scroll DOWN", Toast.LENGTH_LONG).show()
                communicationToMain?.visibleNavigation(false)
            }
            if (scrollY < oldScrollY) {
//                Toast.makeText(context, "Scroll UP", Toast.LENGTH_LONG).show()
                communicationToMain?.visibleNavigation(true)
            }
            if (scrollY == 0) {}
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {}
        })

        // call news
        PreFragNews(this).getNews(0)
    }

    fun questionLoginOrder(){
        val alert = AlertDialog.Builder(context!!)
        alert.setTitle(R.string.noti)
        alert.setMessage(R.string.login_for_order)
        alert.setPositiveButton(R.string.login_now , { dialog, which ->
            tagg = "btnToOrder"
            startActivityForResult(Intent(context, LoginActivity::class.java), REQUEST_CODE_LOGIN)
        })
        alert.setNegativeButton(R.string.noo , { dialog, which ->
            dialog.dismiss()
        })
        alert.show()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden)
            if(MainActivity.customer == null)
                setLogout()
        if(scrollDowm) communicationToMain?.visibleNavigation(false)
        else communicationToMain?.visibleNavigation(true)
    }

    override fun onStart() {
        super.onStart()
        Log.d("logggoutt" , "${MainActivity.customer?.phoneNumber} onStart")
//        if(MainActivity.customer == null)
//            setLogout()
//        else
            if(MainActivity.customer != null) {
                setLogin(MainActivity.customer!!)
            }
    }


    fun checkLogin()
    {
        val email = context!!.getSharedPreferences("Login" , Context.MODE_PRIVATE).getString("email" , null)

        if (AccountKit.getCurrentAccessToken() != null) {
            getPhoneAccountkit()
            return
        }

        Log.d("loginPhone" , (com.facebook.AccessToken.getCurrentAccessToken() != null).toString())
        if(com.facebook.AccessToken.getCurrentAccessToken() != null){
            getInfoUserFacebook()
            return
        }

        if(email != null) {
            PreLogin(this).getInfoByEmail(email)
        }
        else
            setLogout()
    }


    fun getPhoneAccountkit(){
        AccountKit.getCurrentAccount(object : AccountKitCallback<Account> {
            override fun onSuccess(p0: Account?) {
               val phone = p0?.phoneNumber.toString()
                PreLogin(this@NewsFragment).loginPhoneUser(phone)
            }

            override fun onError(p0: AccountKitError?) {
                Log.d("ErrorAcountKit" , p0.toString())

                Toast.makeText(context , R.string.fail_again , Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK && data != null) {
            MainActivity.customer = data.extras!!.getParcelable("customer") as Customer?
            if(tagg.equals("btnToOrder")) {
                if(MainActivity.customer != null)
                    communicationToMain?.checkedFragmentOrder()
            }
        }

        if(requestCode == REQUEST_CODE_DISCOUNT && resultCode == Activity.RESULT_OK && data != null){
            val discount = data.extras?.getParcelable<Discount>("discount")
            communicationToMain?.checkedFragmentOrder(discount)
        }
    }

    fun setLogin(c : Customer){
        tagg = ""
        txtNameCustomerNews.visibility = View.VISIBLE
        lnPoint.visibility = View.VISIBLE
        btnLoginNowNews.visibility = View.GONE

        if(c.phoneNumber != null)
            c.phoneNumber = c.phoneNumber?.replace("84" , "0")

        if(c.avatar != null )
            if(!c.avatar!!.contains("https://"))
                GlideApp.with(this).load(RetrofitInstance.baseUrl + "/" + c.avatar)
                        .into(imgAvataNews)
            else
                GlideApp.with(this).load(c.avatar).into(imgAvataNews)
        else
            GlideApp.with(context!!).load("${RetrofitInstance.baseUrl}/images/logo1.png")
                    .into(imgAvataNews)

        Log.d("points" , "news" + c.point.toString())
        if(c.point == null)
            c.point = 0
        txtPointUser.setText(c.point.toString())
        txtNameCustomerNews.setText(c.nameCustomer)

        toolbarNews.setOnClickListener({
            startActivity(Intent(context , CustomerActivity::class.java))
        })

        PreProductFavoriteActi(this).getProductFavorite(c.idCustomer)
        PreQuantityAndPrice(this).getQuantityAndPrice(c.idCustomer)

        Log.d("logggoutt" , "${MainActivity.customer?.phoneNumber} Login")
        Log.d("logggoutt" , "${loadDialog} Login")
        if(!loadDialog){
            loadDialog = true
            val dialogUpdatePhone = UpdateNumberPhone()
            dialogUpdatePhone.isCancelable = false
            MainActivity.customer?.phoneNumber ?: dialogUpdatePhone.show(childFragmentManager , "updatePhone")
        }
    }

    fun setLogout(){
        txtNameCustomerNews.visibility = View.GONE
        lnPoint.visibility = View.GONE
        btnLoginNowNews.visibility = View.VISIBLE
        Log.d("cccccccc" ,"DO nè")
        GlideApp.with(context!!).load("${RetrofitInstance.baseUrl}/images/logo1.png")
            .into(imgAvataNews)
        toolbarNews.setOnClickListener({})
        loadDialog = false
        communicationToMain?.clearSharePreAndLocation()
    }

    private fun getInfoUserFacebook() {
        GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me?fields=id,name,email,picture",
            null,
            HttpMethod.GET, object :
                GraphRequest.Callback {
                override fun onCompleted(response: GraphResponse) {
                    if(response.jsonObject != null){
                        val id = response.jsonObject.get("id")
                        val name = response.jsonObject.get("name")
                        val email = response.jsonObject.get("email")
                        val url = response.jsonObject.getJSONObject("picture")
                            .getJSONObject("data").get("url")

                        PreLogin(this@NewsFragment).loginFacebook(id.toString(), email.toString() , name.toString() , url.toString())
                    }
                    else
                        Toast.makeText(context , "Null" , Toast.LENGTH_SHORT).show()
                }

            }
        ).executeAsync()
    }
}