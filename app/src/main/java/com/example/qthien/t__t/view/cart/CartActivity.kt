package com.example.qthien.t__t.view.cart

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.RootCartAdapter
import com.example.qthien.t__t.model.MainProductCart
import com.example.qthien.t__t.model.ToppingProductCart
import com.example.qthien.t__t.presenter.pre_cart.PreCart
import com.example.qthien.t__t.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_cart.*

class CartActivity : AppCompatActivity() , IViewCart , RootCartAdapter.AdapterCartCallActivity{

//    ,  ,
//    DialogEditProductCart.DialogEditCartCommunication ,
//    DialogSheetTopping.CompleteTopping

    override fun createEditCart(position: Int) {
        val i = Intent(this , AddToCartActivity::class.java)
        i.putExtra("cart" , arrCart[position])
        startActivity(i)
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
    var positionTopping = -1
    var positionEdit = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        setSupportActionBar(toolbarCart)
        toolbarCart.setTitleTextColor(Color.WHITE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(com.example.qthien.t__t.R.drawable.ic_arrow_back_white_24dp)
        supportActionBar?.setTitle(R.string.your_cart)

        val arrTopping = ArrayList<ToppingProductCart>()
//        arrTopping.add(ToppingProductCart(1 , 1 , "Trân châu đen" , 10000 , 1))
//        arrTopping.add(ToppingProductCart(2 , 2 , "Trân châu trắng" , 10000 , 1))
//        arrTopping.add(ToppingProductCart(3 , 3 , "Thạch trái cây" , 6000 , 1))

        arrCart = ArrayList()
//        arrCart.add(MainProductCart(1 , 1,
//            "Trà sữa chân trâu" , 50000 , 56000 , 72000 , 2,
//            "http://store.bobapop.com.vn/resource/uploads/2016/09/04-tran-chau-600x600.jpg" ,
//            1 , "S" , "cccccccccc" ,arrTopping))
//
//        arrCart.add(MainProductCart(2 , 2,
//            "Sữa tươi chân trâu đường đen" , 45000 , 51000 , 57000 , 2,
//            "http://product.hstatic.net/1000304357/product/foody-1990-tea-335-636607537720912175_1024x1024.jpg" ,
//            2 , "S" , "bbbbbbbbbbbbb" , arrTopping))
//
//        arrCart.add(MainProductCart(3 , 3 ,
//            "Hồng trà chân châu trắng" , 38000 , 44000 , 50000 , 2,
//            "https://tea-4.lozi.vn/v1/images/resized/hong-tra-tran-chau-173012-1466605784?w=480&type=o" ,
//            1 , "S" , "aaaaaaaaaaaaa" ,arrTopping))

        adapter = RootCartAdapter(this , arrCart)
        recyCart.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recyCart.adapter = adapter
        recyCart.isNestedScrollingEnabled = false

        PreCart(this).getAllCartUser(MainActivity.customer!!.idCustomer)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

//    override fun createDialogEditCart(position: Int) {
//        val dialogEdit = DialogEditProductCart()
//        val bundle = Bundle()
//        bundle.putParcelable("cart" , arrCart[position])
//        this.positionEdit = position
//        dialogEdit.arguments = bundle
//        dialogEdit.show(supportFragmentManager , "dialog")
//    }
//
//    override fun recyclerToppingClick(position: Int) {
//        val selectedTopping = DialogSheetTopping()
//        val bundle = Bundle()
//        bundle.putParcelableArrayList("topping" , arrCart[position].arrTopping)
//        this.positionTopping = position
//        selectedTopping.arguments = bundle
//        selectedTopping.show(supportFragmentManager , "dialog")
//    }
//
//    override fun update(cart: MainProductCart) {
//        if(arrCart.find { it.equals(cart) } != null){
//            arrCart.find { it.equals(cart) }!!.quantity += 1
//            arrCart.removeAt(positionEdit)
//        }
//        else{
//            for(i in 0 until arrCart.size)
//                if(arrCart[i].idProductCart == cart.idProductCart)
//                    arrCart[i] = cart
//        }
//
//
//        adapter.notifyDataSetChanged()
//    }
//
//    override fun completeEditTopping(arrToppingEdit: ArrayList<ToppingProductCart>) {
//        arrCart[positionTopping].arrTopping?.clear()
//        arrCart[positionTopping].arrTopping?.addAll(arrToppingEdit)
//
//        val tmp = arrCart[positionTopping]
//        arrCart.removeAt(positionTopping)
//
//        if(arrCart.find { it.equals(tmp) } != null){
//            arrCart.find { it.equals(tmp) }!!.quantity += 1
//        }
//        else{
//            arrCart.add(positionTopping , tmp)
//        }
//
//        adapter.notifyDataSetChanged()
//    }
//
//    override fun remove(cart: MainProductCart) {
//        arrCart.remove(arrCart.find { it.idProductCart == cart.idProductCart })
//        adapter.notifyDataSetChanged()
//    }
}
