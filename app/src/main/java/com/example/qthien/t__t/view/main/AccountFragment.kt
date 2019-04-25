package com.example.qthien.t__t.view.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R
import com.example.qthien.t__t.view.account.CustomerActivity
import com.example.qthien.week3_ryder.GlideApp
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment() {

    companion object {
        fun newInstance() : AccountFragment = AccountFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_account , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarAccount.setOnClickListener({
            startActivity(Intent(context , CustomerActivity::class.java))
        })

        GlideApp.with(context!!)
            .load("https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=1759083097570150&height=50&width=50&ext=1558512801&hash=AeTXe96P5aIPDi42")
            .into(imgAvataAccount)
    }
}