package com.example.qthien.t__t.mvp.view.cart

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.RootCartAdapter
import com.example.qthien.t__t.model.Discount
import com.example.qthien.t__t.model.MainProductCart
import com.example.qthien.t__t.mvp.presenter.pre_cart.PreCart
import com.example.qthien.t__t.mvp.view.main.MainActivity
import com.example.qthien.t__t.mvp.view.order.OrderActivity
import kotlinx.android.synthetic.main.activity_cart.*
import java.text.DecimalFormat

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
            relaToDepay.isEnabled = true
        }
        else
            Toast.makeText(this , R.string.fail_again , Toast.LENGTH_LONG).show()
        progressLoader.visibility = View.GONE
    }

    override fun failure(message: String) {
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
    }

    lateinit var adapter : RootCartAdapter
    lateinit var arrCart : ArrayList<MainProductCart>
    val REQUEST_EDIT_CART = 1
    val REQUEST_ORDER_CART = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        relaToDepay.isEnabled = false

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

        relaToDepay.setOnClickListener({
            val i = Intent(this , OrderActivity::class.java)
            i.putExtra("Cart" , arrCart)
            i.putExtra("discount" , intent.extras?.getParcelable<Discount?>("discount"))
            startActivityForResult(i , REQUEST_ORDER_CART)
        })

        setUpQuantityAndPrice()
    }

    @SuppressLint("SetTextI18n")
    fun setUpQuantityAndPrice(){
        val share = getSharedPreferences("QuantityPrice" , Activity.MODE_PRIVATE)
        val quantity = share.getInt("quantity" , 0)
        val price = share.getInt("price" , 0)

        if(quantity != 0){
            relaToDepay.visibility = View.VISIBLE
            txtQuantity.setText(quantity.toString())
            txtSumPriceCart.setText(DecimalFormat("###,###,###").format(price)+ " đ")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_EDIT_CART && resultCode == Activity.RESULT_OK && data != null)
        {
            val arrUpdate :  ArrayList<MainProductCart>? = data.extras?.getParcelableArrayList("arrCartNews")
            if(arrUpdate != null && arrUpdate.size > 0){
                arrCart.clear()
                arrCart.addAll(arrUpdate)
                adapter.notifyDataSetChanged()
                setUpQuantityAndPrice()
            }
            else
                finish()
        }

        if(requestCode == REQUEST_ORDER_CART && resultCode == Activity.RESULT_OK && data != null){
            arrCart.clear()
            getSharedPreferences("QuantityPrice" , Activity.MODE_PRIVATE).edit().clear().apply()
            getSharedPreferences("addressOrder", Activity.MODE_PRIVATE).edit().clear().apply()
            setResult(Activity.RESULT_OK , Intent())
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
