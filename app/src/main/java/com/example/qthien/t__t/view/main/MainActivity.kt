package com.example.qthien.t__t.view.main

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.Customer
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() , NewsFragment.FragmentNewsCommnunicationMain
, AccountFragment.FragmentAccountCommnunicationMain {
    override fun visibleLoader(visible : Int) {
        frmLoader.visibility = visible
    }

    override fun checkSelectedFragmentOrder() {
        navigationView.selectedItemId = R.id.nav_order
    }

    override fun checkSelectedFragmentNews() {
        navigationView.selectedItemId = R.id.nav_news
    }

    override fun checkedFragmentOrder() {
        navigationView.selectedItemId = R.id.nav_order
    }

    var newsFragment : NewsFragment? = null
    var accountFragment: AccountFragment ?= null
    var fragmentOrder : OrderFragment ?= null
    var fragmentActi : Fragment ?= null

    companion object {
        var customer : Customer? = null
        var customerFB : Customer? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Thread.setDefaultUncaughtExceptionHandler(MyExceptionHandler(this))

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        newsFragment = NewsFragment.newInstance()
        fragmentActi = newsFragment

        supportFragmentManager.beginTransaction()
            .add(R.id.frmContainer, newsFragment!! , "news").commit()


    }
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_news -> {
                hideAndShowFragment(fragmentActi!! , newsFragment!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_order -> {
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

    override fun onRestart() {
        super.onRestart()
        val change = getSharedPreferences("Favorite" , Context.MODE_PRIVATE).getBoolean("changeFavorite" , false)
        if(change) {
            fragmentOrder?.updateViewPager()
            getSharedPreferences("Favorite" , Context.MODE_PRIVATE)?.edit()
                    ?.remove("changeFavorite")?.apply()
        }
    }
}
