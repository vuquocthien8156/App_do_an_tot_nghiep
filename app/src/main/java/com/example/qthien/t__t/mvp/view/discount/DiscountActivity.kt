package com.example.qthien.t__t.mvp.view.discount

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.DiscountAdapter
import com.example.qthien.t__t.model.Discount
import com.example.qthien.t__t.mvp.presenter.discount.PreDiscount
import kotlinx.android.synthetic.main.activity_discount.*

class DiscountActivity : AppCompatActivity() , IDiscount , DiscountAdapter.DiscountAdapterCommunicate {


    override fun startActivityDetail(discount: Discount) {
        val i = Intent(this, DetailDiscountActivity::class.java)
        i.putExtra("discount", discount)
        startActivityForResult(i , REQUEST_DETAIL)
    }

    override fun failure(message: String) {
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
    }

    override fun successGetDiscount(arrDiscount: ArrayList<Discount>) {
        this.arrDiscount.addAll(arrDiscount)
        adapter.notifyDataSetChanged()
        progressLoader.visibility = View.GONE
    }

    lateinit var adapter : DiscountAdapter
    lateinit var arrDiscount : ArrayList<Discount>
    private val REQUEST_DETAIL = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discount)

        setSupportActionBar(toolbarDiscount)
        toolbarDiscount.setTitleTextColor(Color.WHITE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        arrDiscount = ArrayList()
        adapter = DiscountAdapter(this , arrDiscount)
        val layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recyDiscount.layoutManager = layoutManager
        recyDiscount.adapter = adapter

        PreDiscount(this).getDiscount(0)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_DETAIL && resultCode == Activity.RESULT_OK && data != null){
            val i = Intent()
            val discount = data.extras?.getParcelable<Discount>("discount")
            i.putExtra("discount", discount)
            setResult(Activity.RESULT_OK, i)
            finish()
        }
    }
}
