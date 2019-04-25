package com.example.qthien.t__t.view.main

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.qthien.t__t.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var newsFragment : NewsFragment
    lateinit var accountFragment: AccountFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        newsFragment = NewsFragment.newInstance()
        openFragment(newsFragment)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_news -> {
                openFragment(newsFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_order -> {
                val fragmentOrder = OrderFragment.newInstance()
                openFragment(fragmentOrder)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_account -> {
                accountFragment = AccountFragment.newInstance()
                openFragment(accountFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frmContainer, fragment)
        transaction.commit()
    }

}
