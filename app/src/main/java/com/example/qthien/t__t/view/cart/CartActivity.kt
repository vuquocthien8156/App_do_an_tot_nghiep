package com.example.qthien.t__t.view.cart

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.RootCartAdapter
import com.example.qthien.t__t.model.MainProductCart
import com.example.qthien.t__t.presenter.pre_cart.PreCart
import com.example.qthien.t__t.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_cart.*

class CartActivity : AppCompatActivity() , IViewCart , RootCartAdapter.AdapterCartCallActivity{

    override fun createEditCart(position: Int) {
        val i = Intent(this , AddToCartActivity::class.java)
        i.putExtra("arrCart" , arrCart)
        i.putExtra("position" , position)
        startActivityForResult(i , REQUEST_EDIT_CART)
    }

    override fun getCartOfUser(arrCart: ArrayList<MainProductCart>?) {
        if(arrCart != null) {
            this.arrCart.addAll(arrCart)
            adapter.notifyDataSetChanged()
        }
        else
            Toast.makeText(this , R.string.fail_again , Toast.LENGTH_LONG).show()
    }

    override fun failure(message: String) {
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
    }

    lateinit var adapter : RootCartAdapter
    lateinit var arrCart : ArrayList<MainProductCart>
    val REQUEST_EDIT_CART = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        setSupportActionBar(toolbarCart)
        toolbarCart.setTitleTextColor(Color.WHITE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(com.example.qthien.t__t.R.drawable.ic_arrow_back_white_24dp)
        supportActionBar?.setTitle(R.string.your_cart)

        arrCart = ArrayList()

        adapter = RootCartAdapter(this , arrCart)
        recyCart.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recyCart.adapter = adapter
        recyCart.isNestedScrollingEnabled = false

        PreCart(this).getAllCartUser(MainActivity.customer!!.idCustomer)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_EDIT_CART && resultCode == Activity.RESULT_OK && data != null)
        {
            val arrUpdate :  ArrayList<MainProductCart>? = data.extras?.getParcelableArrayList("arrCartNews")
            if(arrUpdate != null){
                arrCart.clear()
                arrCart.addAll(arrUpdate)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
