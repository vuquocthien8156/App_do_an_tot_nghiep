package com.example.qthien.t__t.mvp.view.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.GlideApp
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.Customer
import com.example.qthien.t__t.mvp.view.WebActivity
import com.example.qthien.t__t.mvp.view.customer.CustomerActivity
import com.example.qthien.t__t.mvp.view.delivery_address.DeliveryAddressActivity
import com.example.qthien.t__t.mvp.view.order.OrderHistoryActivity
import com.example.qthien.t__t.mvp.view.point.HistoryPointActivity
import com.example.qthien.t__t.mvp.view.product_favorite.ProductFavoriteActivity
import com.example.qthien.t__t.mvp.view.view_login.LoginActivity
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import com.facebook.accountkit.AccountKit
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment() {

    companion object {
        fun newInstance() : AccountFragment = AccountFragment()
    }

    interface FragmentAccountCommnunicationMain{
        fun checkSelectedFragmentNews()
        fun checkSelectedFragmentOrder()
        fun clearSharePreAndLocation()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_account , container , false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is FragmentAccountCommnunicationMain)
            communicationToMain = context
    }

    var communicationToMain : FragmentAccountCommnunicationMain? = null
    val REQUEST_CODE_LOGIN = 1
    val REQUEST_TO_ORDER = 2
    var call = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(MainActivity.customer != null){
            toolbarAccount.setOnClickListener({
                startActivity(Intent(context , CustomerActivity::class.java))
            })
        }
        else
            setLogout()

        txtFavorite.setOnClickListener({
            if(MainActivity.customer != null)
                startActivityForResult(Intent(context , ProductFavoriteActivity::class.java) , REQUEST_TO_ORDER)
            else {
                startActivityForResult(Intent(context, LoginActivity::class.java), REQUEST_CODE_LOGIN)
                call = "favorite"
            }
        })
        txtHistoryOrder.setOnClickListener({
            if(MainActivity.customer != null)
                startActivityForResult(Intent(context , OrderHistoryActivity::class.java) , REQUEST_TO_ORDER)
            else {
                startActivityForResult(Intent(context, LoginActivity::class.java), REQUEST_CODE_LOGIN)
                call = "history"
            }
        })
        txtLogout.setOnClickListener({
            logout()
        })
        btnLoginNowAccount.setOnClickListener({
            startActivityForResult(Intent(context , LoginActivity::class.java) , REQUEST_CODE_LOGIN)
        })

        txtAddressDelivery.setOnClickListener({
            if(MainActivity.customer != null)
                startActivity(Intent(context , DeliveryAddressActivity::class.java))
            else {
                startActivityForResult(Intent(context, LoginActivity::class.java), REQUEST_CODE_LOGIN)
                call = "address"
            }
        })

        txtPointManager.setOnClickListener({
            if(MainActivity.customer != null)
                startActivityForResult(Intent(context , HistoryPointActivity::class.java) , REQUEST_TO_ORDER)
            else {
                startActivityForResult(Intent(context, LoginActivity::class.java), REQUEST_CODE_LOGIN)
                call = "point"
            }
        })

        txtLaw.setOnClickListener({
            val i = Intent(context , WebActivity::class.java)
            i.putExtra("url" , "${RetrofitInstance.baseUrl}/rule")
            startActivity(i)
        })
    }

    override fun onStart() {
        super.onStart()
        Log.d("logggoutt" , "onStart")
        if(MainActivity.customer != null)
            setLogin(MainActivity.customer!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK && data != null)
        {
            MainActivity.customer = data.extras!!.getParcelable("customer") as Customer?
            val c = MainActivity.customer
            Log.d("cccccccc" ,"2" + c.toString())
            if(c != null) {
                if(call.equals(""))
                    communicationToMain?.checkSelectedFragmentNews()
                if(call.equals("favorite"))
                    startActivity(Intent(context , ProductFavoriteActivity::class.java))
                if(call.equals("history"))
                    startActivityForResult(Intent(context, OrderHistoryActivity::class.java), REQUEST_CODE_LOGIN)
                if(call.equals("address"))
                    startActivity(Intent(context , DeliveryAddressActivity::class.java))
                if(call.equals("point")){
                    startActivity(Intent(context , HistoryPointActivity::class.java))
                }
            }
        }

        if(requestCode == REQUEST_TO_ORDER){
            when(resultCode){
                Activity.RESULT_OK -> {
                    communicationToMain?.checkSelectedFragmentOrder()
                }
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden)
            if(MainActivity.customer != null)
                setLogin(MainActivity.customer!!)
    }

    private fun setLogin(c : Customer) {
        txtLogout.visibility = View.VISIBLE
        imgAvataAccount.visibility = View.VISIBLE
        txtNameCustomerAccount.visibility = View.VISIBLE
        txtPointUser.visibility = View.VISIBLE
        txtMember.visibility = View.VISIBLE

        Log.d("logggoutt" , "a")

        if(c.phoneNumber != null)
            c.phoneNumber = c.phoneNumber?.replace("84" , "0")

        if(c.avatar != null)
            if(!c.avatar!!.contains("https://"))
                GlideApp.with(this).load(RetrofitInstance.baseUrl + "/" + c.avatar)
                        .into(imgAvataAccount)
            else
                GlideApp.with(this).load(c.avatar).into(imgAvataAccount)
        else
            GlideApp.with(context!!).load("${RetrofitInstance.baseUrl}/images/logo1.png")
                    .into(imgAvataAccount)

        if(c.point == null)
            c.point = 0
        txtPointUser.setText(c.point.toString())

        txtNameCustomerAccount.setText(c.nameCustomer)

        btnLoginNowAccount.visibility = View.GONE
    }

    fun setLogout(){

        Log.d("logggoutt" , "setLogoutAccc")

        txtLogout.visibility = View.GONE
        txtNameCustomerAccount.visibility = View.GONE
        txtPointUser.visibility = View.GONE
        txtMember.visibility = View.GONE
        imgAvataAccount.visibility = View.GONE
        btnLoginNowAccount.visibility = View.VISIBLE
    }

    val callNews = fun() =  communicationToMain?.checkSelectedFragmentNews()

    fun logout(){

        if (AccountKit.getCurrentAccessToken() != null) {
            AccountKit.logOut()
        }

        if(com.facebook.AccessToken.getCurrentAccessToken() != null){
            LoginManager.getInstance().logOut()
        }
        context!!.getSharedPreferences("Login" , Context.MODE_PRIVATE).edit().clear().apply()
        MainActivity.customer = null
        setLogout()
        communicationToMain?.checkSelectedFragmentNews()
        communicationToMain?.clearSharePreAndLocation()
    }
}