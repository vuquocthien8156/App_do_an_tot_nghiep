package com.example.qthien.t__t.view.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.NewsAdapter
import com.example.qthien.t__t.adapter.ViewPagerAdapter
import com.example.qthien.t__t.model.Customer
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.presenter.pre_login.PreLogin
import com.example.qthien.t__t.presenter.pre_product_favorite.PreProductFavoriteActi
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import com.example.qthien.t__t.view.BarcodeActivity
import com.example.qthien.t__t.view.branch.BranchActivity
import com.example.qthien.t__t.view.customer.CustomerActivity
import com.example.qthien.t__t.view.delivery_address.SearchDeliverryAddressActivity
import com.example.qthien.t__t.view.product_favorite.IViewProductFavoriteActi
import com.example.qthien.t__t.view.view_login.ILogin
import com.example.qthien.t__t.view.view_login.LoginActivity
import com.example.qthien.week3_ryder.GlideApp
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.HttpMethod
import com.facebook.accountkit.AccountKit
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : Fragment() , ILogin , IViewProductFavoriteActi {

    override fun favoriteProduct(resultCode: String) {}
    override fun failureFavorite(message: String) {}
    override fun resultExistAccount(email : String? , id_fb : String? , phone : String?) {}
    override fun resultRegisterAccount(idUser: Int?) {}

    override fun resultGetProductFavorite(arrResult: ArrayList<Product>?) {
        if(arrResult != null){
            val arrIdFavorite = arrResult.map { it.idProduct }
            context?.getSharedPreferences("Favorite" , Context.MODE_PRIVATE)?.edit()
                ?.putString( "arrFavorite" , arrIdFavorite.toString())?.apply()

            val r = arrIdFavorite.toString()
            Log.d("arrrFavorite" , r)
        }
    }

    override fun failure(message: String) {
        Toast.makeText(context , message , Toast.LENGTH_LONG).show()
        lnLoader.visibility = View.GONE
    }

    override fun resultLoginAccount(customer: Customer?) {
        MainActivity.customer = customer
        if(customer != null)
            setLogin(customer)
        else
            setLogout()
        lnLoader.visibility = View.GONE
        Log.d("emaillll" , "News" + customer.toString())
    }

    override fun resultLoginPhone(customer: Customer?) {
        MainActivity.customer = customer
        if(customer != null)
            setLogin(customer)
        else
            setLogout()
        lnLoader.visibility = View.GONE
    }

    override fun resultLoginFacebook(customer: Customer?) {
        Log.d("customerFB" , MainActivity.customer.toString())
        if(customer != null) {
            MainActivity.customer = customer
            setLogin(MainActivity.customer!!)
        }else
            setLogout()
        lnLoader.visibility = View.GONE
    }

    val arrPoster = arrayListOf("http://quangcaotantheky.com/wp-content/uploads/2017/10/c-mau-bien-quang-cao-tra-sua-e1513221415389.jpg"
        ,"data:image/jpeg;base64,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"
        , "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR023Lpt_0X3fTUYB8OgUnTQczTkncYg02NUl0dR22rdcZwe0xt"
        , "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTXVI2i-QS91Vrn3YSMUN66km6rpQFc_2SXLyBPUaUtQPQ2qLT4_Q")

    companion object {
        fun newInstance() : NewsFragment = NewsFragment()
    }

    interface FragmentNewsCommnunicationMain{
        fun checkedFragmentOrder()
    }

    var communicationToMain : FragmentNewsCommnunicationMain? = null

    val REQUEST_CODE_LOGIN = 1
    var tagg = ""

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

        viewpagerMain.setAdapter(ViewPagerAdapter(context!! , arrPoster))

        viewpagerMain.setScrollFactor(5.0)
        viewpagerMain.setOffscreenPageLimit(4)
        viewpagerMain.startAutoScroll(4000)
        indicator.setViewPager(viewpagerMain)

        recylerMain.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)
        recylerMain.adapter = NewsAdapter(context!! , arrayListOf(""))

        btnToOrder.setOnClickListener({
            communicationToMain?.checkedFragmentOrder()
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
            startActivity(Intent(context , SearchDeliverryAddressActivity::class.java))
        })

        btnLoginNowNews.setOnClickListener({
            startActivityForResult(Intent(context , LoginActivity::class.java) , REQUEST_CODE_LOGIN)
        })

        checkLogin()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Toast.makeText(context , hidden.toString() , Toast.LENGTH_LONG).show()
        if(!hidden)
            if(MainActivity.customer != null) {
                setLogin(MainActivity.customer!!)
            }else
                setLogout()
    }

    override fun onStart() {
        super.onStart()
        Toast.makeText(context , "onStart" , Toast.LENGTH_LONG).show()
        if(MainActivity.customer == null && MainActivity.customerFB == null)
            setLogout()
        else
            if(MainActivity.customer != null)
                setLogin(MainActivity.customer!!)
    }


    fun checkLogin()
    {
        val email = context!!.getSharedPreferences("Login" , Context.MODE_PRIVATE).getString("email" , null)
        val phone = context!!.getSharedPreferences("Login" , Context.MODE_PRIVATE).getString("phone" , null)

        if (AccountKit.getCurrentAccessToken() != null && phone != null) {
            PreLogin(this).loginPhoneUser(phone)
        }

        if(com.facebook.AccessToken.getCurrentAccessToken() != null){
            getInfoUserFacebook()
        }

        if(email != null) {
            PreLogin(this).getInfoByEmail(email)
            Log.d("emaillll" , "Main")
        }
        else
            lnLoader.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK && data != null)
        {
            MainActivity.customer = data.extras!!.getParcelable("customer") as Customer?
            val c = MainActivity.customer
            if(c != null)
                setLogin(c)
        }
    }

    fun setLogin(c : Customer){
        txtNameCustomerNews.visibility = View.VISIBLE
        lnPoint.visibility = View.VISIBLE
        btnLoginNowNews.visibility = View.GONE

        Log.d("urrllll" , "${RetrofitInstance.baseUrl}/${c.avatar}")
        if(c.avatar != null)
            GlideApp.with(context!!).load("${RetrofitInstance.baseUrl}/${c.avatar}")
            .into(imgAvataNews)
        else
            if(MainActivity.customerFB != null)
                GlideApp.with(context!!).load(MainActivity.customerFB!!.avatar)
                    .into(imgAvataNews)
            else
                GlideApp.with(context!!).load("${RetrofitInstance.baseUrl}/images/logo1.png")
                    .into(imgAvataNews)

        if(c.point == null)
            c.point = 0
        txtPointUser.setText(c.point.toString())
        txtNameCustomerNews.setText(c.nameCustomer)

        toolbarNews.setOnClickListener({
            startActivity(Intent(context , CustomerActivity::class.java))
        })

        PreProductFavoriteActi(this).getProductFavorite(MainActivity.customer!!.idCustomer)
    }

    fun setLogout(){
        txtNameCustomerNews.visibility = View.GONE
        lnPoint.visibility = View.GONE
        btnLoginNowNews.visibility = View.VISIBLE
        Log.d("cccccccc" ,"DO nè")
        GlideApp.with(context!!).load("${RetrofitInstance.baseUrl}/images/logo1.png")
            .into(imgAvataNews)
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
                        MainActivity.customerFB = Customer(
                            0 ,
                            id.toString() ,
                            name.toString(),
                            null,
                            null,
                            null,
                            null,
                            email.toString(),
                            null,
                            url.toString(),
                            null
                        )
                        PreLogin(this@NewsFragment).loginFacebook(id.toString(), email.toString() , name.toString())
                    }
                    else
                        Toast.makeText(context , "Null" , Toast.LENGTH_SHORT).show()
                }

            }
        ).executeAsync()
    }
}