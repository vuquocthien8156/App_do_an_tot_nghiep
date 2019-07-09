package com.example.qthien.t__t.mvp.view.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.Customer
import com.example.qthien.t__t.model.Discount
import com.example.qthien.t__t.mvp.view.NetworkCheckingActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() , NewsFragment.FragmentNewsCommnunicationMain
, AccountFragment.FragmentAccountCommnunicationMain , OrderFragment.OrderFragmentCallMain {

    override fun clearSharePreAndLocation() {
        getSharedPreferences("address", Activity.MODE_PRIVATE).edit().clear().apply()
        getSharedPreferences("Favorite" , Context.MODE_PRIVATE).edit().clear().apply()
        getSharedPreferences("QuantityPrice" , Context.MODE_PRIVATE).edit().clear().apply()
        getSharedPreferences("EvaluateTks" , Context.MODE_PRIVATE).edit().clear().apply()

        fragmentOrder?.checkGPSFirst = false
        fragmentOrder?.hasAddress = false
    }

    override fun visibleNavigation(boolean: Boolean) {
        if(boolean) {
            navigationView.visibility = View.VISIBLE
        }else {
            navigationView.visibility = View.GONE
        }
    }

    override fun checkSelectedFragmentOrder() {
        navigationView.selectedItemId = com.example.qthien.t__t.R.id.nav_order
    }

    override fun checkSelectedFragmentNews() {
        navigationView.selectedItemId = com.example.qthien.t__t.R.id.nav_news
    }

    override fun checkedFragmentOrder(discount : Discount?) {
        Log.d("checkedFragmentOrder" , "1")
        navigationView.selectedItemId = com.example.qthien.t__t.R.id.nav_order
        fragmentOrder?.setDiscount(discount)
    }

    var newsFragment : NewsFragment? = null
    var accountFragment: AccountFragment ?= null
    var fragmentOrder : OrderFragment ?= null
    var fragmentActi : Fragment ?= null

    val REQUEST_CODE_NETWORK = 1

    companion object {
        var customer : Customer? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(com.example.qthien.t__t.R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_main)

//        Thread.setDefaultUncaughtExceptionHandler(MyExceptionHandler(this))

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        newsFragment = NewsFragment.newInstance()
        fragmentActi = newsFragment

        supportFragmentManager.beginTransaction()
            .add(com.example.qthien.t__t.R.id.frmContainer, newsFragment!! , "news").commit()


    }

    override fun onBackPressed() {
        if(!newsFragment!!.isHidden)
            super.onBackPressed()
        else {
            hideAndShowFragment(fragmentActi!!, newsFragment!!)
            navigationView.selectedItemId = com.example.qthien.t__t.R.id.nav_news
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            com.example.qthien.t__t.R.id.nav_news -> {
                hideAndShowFragment(fragmentActi!! , newsFragment!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_order -> {
                if(customer == null) {
                    newsFragment?.questionLoginOrder()
                    return@OnNavigationItemSelectedListener false
                }
                else
                    if(fragmentOrder == null) {
                        fragmentOrder = OrderFragment.newInstance()
                        addFragment(fragmentOrder!!)
                    }
                hideAndShowFragment(fragmentActi!! , fragmentOrder!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_account -> {
                if(accountFragment == null) {
                    accountFragment = AccountFragment.newInstance()
                    addFragment(accountFragment!!)
                }
                else
                    hideAndShowFragment(fragmentActi!! , accountFragment!!)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun addFragment(fr : Fragment){
        supportFragmentManager.beginTransaction().add(R.id.frmContainer , fr).hide(fragmentActi!!).show(fr).commit()
        fragmentActi = fr
    }

    fun hideAndShowFragment(fragHide : Fragment , fragShow : Fragment){
        supportFragmentManager.beginTransaction().hide(fragHide).show(fragShow).commit()
        fragmentActi = fragShow
    }

    override fun onResume() {
        super.onResume()
        Log.d("checkedFragmentOrder" , "1" + (fragmentOrder != null).toString())
        if(fragmentOrder != null)
            fragmentOrder?.setUpQuantityAndPrice()

        if(!checkConection()){
            val i = Intent(this , NetworkCheckingActivity::class.java)
            startActivityForResult(i , REQUEST_CODE_NETWORK)
        }
    }

    fun checkConection() : Boolean{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true
        return isConnected
    }

    override fun onRestart() {
        super.onRestart()

        val change = getSharedPreferences("Favorite" , Context.MODE_PRIVATE).getBoolean("changeFavorite" , false)
        Log.d("changeeeeFavo" , change.toString())
        if(change) {
            fragmentOrder?.updateViewPager()
            getSharedPreferences("Favorite" , Context.MODE_PRIVATE)?.edit()
                    ?.remove("changeFavorite")?.apply()
        }
    }
}
