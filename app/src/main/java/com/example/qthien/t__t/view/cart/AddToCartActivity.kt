package com.example.qthien.t__t.view.cart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.ToppingSelectedAdapter
import com.example.qthien.t__t.model.CartPlus
import com.example.qthien.t__t.model.MainProductCart
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.model.ToppingProductCart
import com.example.qthien.t__t.presenter.pre_cart.PreAddCart
import com.example.qthien.t__t.presenter.pre_product_favorite.PreProductFavoriteActi
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import com.example.qthien.t__t.view.main.MainActivity
import com.example.qthien.t__t.view.product_favorite.IViewProductFavoriteActi
import com.example.qthien.week3_ryder.GlideApp
import kotlinx.android.synthetic.main.activity_add_to_cart.*
import java.text.DecimalFormat


class AddToCartActivity : AppCompatActivity(),
    View.OnClickListener,
    DialogEnterNoteFragment.TransmissionNoteInDialogToActivity,
    IViewProductFavoriteActi,
    IAddToCartActi,
    DialogSheetTopping.CompleteTopping {

    override fun completeEditTopping(arrToppingEdit: ArrayList<ToppingProductCart>) {
        arrToppingSelected.clear()
        arrToppingSelected.addAll(arrToppingEdit)
        adapterOrder.notifyDataSetChanged()
        if(arrToppingSelected.size > 0)
            txtAddtopping.setText(R.string.update_topping)
        else
            txtAddtopping.setText(R.string.add_topping)

        caculationPriceTmp()
    }

    override fun failureFavorite(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun failureAddCart(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun checkFavoriteProduct(check: Int?) {
//        if (check != null) {
//            checkFavorite = if (check == 1) true else false
//            setFavorite()
//        }
    }

    override fun favoriteProduct(resultCode: String) {
        Log.d("favoriteProduct", resultCode)
        if (resultCode.equals("ok")) {
            checkFavorite = !checkFavorite
            if(checkFavorite){
                arrFavoriteString?.add(product?.idProduct.toString())
            }
            else{
                arrFavoriteString?.remove(product?.idProduct.toString())
            }
            Log.d("arrrFavorite" , arrFavoriteString.toString())

            getSharedPreferences("Favorite" , Context.MODE_PRIVATE).edit()
                .putString( "arrFavorite" , arrFavoriteString.toString()).apply()

            setFavorite()
        }
    }

    override fun successAddCart(status: String?) {
        if(status != null && status.equals("Success")){
            Toast.makeText(this, "Add cart success", Toast.LENGTH_LONG).show()
        }
        else
            Toast.makeText(this, "Add cart fail", Toast.LENGTH_LONG).show()
    }

    override fun resultGetProductFavorite(arrResult: ArrayList<Product>?) {}

        private var product: Product? = null

        private var checkFavorite = false

        private var priceNow = 0L

        private var formatPrice = DecimalFormat("###,###,###")

        private lateinit var adapterOrder: ToppingSelectedAdapter
        private lateinit var arrToppingSelected : ArrayList<ToppingProductCart>

        private var cart : MainProductCart? = null

        private lateinit var cartPlus : CartPlus

        var arrFavoriteString : ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_add_to_cart)

        product = intent.extras!!.get("product") as Product?

        cart = intent.extras!!.get("cart") as MainProductCart?

        btnAddCart.setTag(R.drawable.ic_add_shopping_cart_24dp)

        arrToppingSelected = ArrayList()

        createObjectCartPlus()

        if(cart != null)
           loadCart()

        imgClose.setOnClickListener({
            finish()
        })

        btnS.setOnClickListener(this)
        btnM.setOnClickListener(this)
        btnL.setOnClickListener(this)
        ibtnPlus.setOnClickListener(this)
        ibtnMinus.setOnClickListener(this)
        btnFavorite.setOnClickListener(this)

        if (product!!.priceProduct == 0.toLong()) {
            btnS.visibility = View.GONE
        }
        if (product!!.priceMProduct == 0.toLong()) {
            btnM.visibility = View.GONE
        }
        if (product!!.priceLProduct == 0.toLong()) {
            btnL.visibility = View.GONE
        }

        setUpInfoProduct()
        caculationPriceTmp()

        val preAdd = PreAddCart(this)
        preAdd.checkFavoriteProduct(MainActivity.customer?.idCustomer!!, product!!.idProduct)

        Log.d("proddđ" , product.toString())
        if(product!!.mainCatalogy == 1) {
            relaTopping.visibility = View.GONE
            inputLayout.visibility = View.GONE
            line3.visibility = View.GONE
            txtAddtopping.visibility = View.GONE
        }
        else{
            adapterOrder = ToppingSelectedAdapter(this , arrToppingSelected)

            recylerToppingSelected.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recylerToppingSelected.adapter = adapterOrder
        }

        txtAddtopping.setOnClickListener({
            val selectedTopping = DialogSheetTopping()
            val bundle = Bundle()
            bundle.putParcelableArrayList("topping" , arrToppingSelected)
            selectedTopping.arguments = bundle
            selectedTopping.show(supportFragmentManager , "dialog")
        })

        eventAddCart()
        checkFavorite()
    }

    fun checkFavorite(){
        val stringFavorite = getSharedPreferences("Favorite" , Context.MODE_PRIVATE).getString("arrFavorite" , null)
        if(stringFavorite != null){
            val arrFavorite = stringFavorite.replace("[" , "").replace("]" , "").split(",")
            arrFavoriteString = ArrayList()
            arrFavoriteString?.addAll(arrFavorite)
            if(arrFavorite.find { it.trim().equals(product?.idProduct.toString()) } != null) {
                checkFavorite = true
            }else
                checkFavorite = false
            setFavorite()
        }
    }

    private fun createObjectCartPlus() {
        val cartAdd = MainProductCart(
            0,
            product?.idProduct ?: cart!!.idProduct,
            "" , 0L , 0L , 0L , 0 , "" ,
            0 , "" , "" , arrToppingSelected)

        cartPlus = CartPlus(MainActivity.customer!!.idCustomer, cartAdd)
    }

    @SuppressLint("SetTextI18n")
    private fun loadCart() {
        product = Product(cart!!.idProduct,
            cart!!.nameProduct, cart!!.priceProduct , cart!!.priceMProduct , cart!!.priceLProduct ,
            "" , 0 , cart!!.imageUrl , "" , 0 , cart!!.mainCatalogy)

        arrToppingSelected.addAll(cart!!.arrTopping)

        when(cart!!.size){
            "S" ->  {
                setActiBtnAndUnActiBtn(btnS, btnL, btnM)
                txtPriceProduct.setText(formatPrice.format(cart!!.priceProduct) + " đ")
                cartPlus.cart.size = "S"
            }
            "M" ->  {
                setActiBtnAndUnActiBtn(btnM, btnL, btnS)
                txtPriceProduct.setText(formatPrice.format(cart!!.priceProduct) + " đ")
                cartPlus.cart.size = "M"
            }
            "L" ->  {
                setActiBtnAndUnActiBtn(btnL, btnS, btnM)
                txtPriceProduct.setText(formatPrice.format(cart!!.priceProduct) + " đ")
                cartPlus.cart.size = "L"
            }
        }

        if(cart != null && cart!!.note != null)
            edtNote.setText(cart!!.note)

        btnAddCart.setBackgroundResource(R.drawable.shape_btn_cart_update)
        btnAddCart.setImageDrawable(getDrawable(R.drawable.ic_check_white_22dp))
        btnAddCart.setTag(R.drawable.ic_check_white_22dp)
    }

    fun eventAddCart(){
        btnAddCart.setOnClickListener({
            when(btnAddCart.getTag() as Int){
                R.drawable.ic_add_shopping_cart_24dp ->{
                    cartPlus.cart.quantity = edtQuantity.text.toString().toInt()
                    PreAddCart(this).addCart(cartPlus)
                }
                R.drawable.ic_check_white_22dp -> {

                }
                R.drawable.ic_remove_shopping_cart_white_24dp ->{

                }
            }
        })
    }

    fun setFavorite() {
        if (checkFavorite) {
            btnFavorite.setImageDrawable(getDrawable(com.example.qthien.t__t.R.drawable.ic_favorite_red_24dp))
            btnFavorite.setBackgroundResource(com.example.qthien.t__t.R.drawable.shape_btn_un_favorite)
        } else {
            btnFavorite.setImageDrawable(getDrawable(com.example.qthien.t__t.R.drawable.ic_favorite_white_24dp))
            btnFavorite.setBackgroundResource(com.example.qthien.t__t.R.drawable.shape_btn_favorite)
        }
    }

    @SuppressLint("SetTextI18n")
    fun setUpInfoProduct() {
        GlideApp.with(this)
            .load("${RetrofitInstance.baseUrl}/${product!!.imageProduct}")
            .into(imgProduct)

        txtNameProduct.setText(product!!.nameProduct)

        when {
            product!!.priceProduct != 0.toLong() -> {
                priceNow = product!!.priceProduct
                val name = txtNameProduct.text.toString() + " (S)"
                txtNameProduct.setText(name)
                setActiBtnAndUnActiBtn(btnS, btnL, btnM)
            }
            product!!.priceMProduct != 0.toLong() -> {
                priceNow = product!!.priceMProduct
                setActiBtnAndUnActiBtn(btnM, btnL, btnS)
                val name = txtNameProduct.text.toString() + " (M)"
                txtNameProduct.setText(name)
            }
            product!!.priceLProduct != 0.toLong() -> {
                priceNow = product!!.priceLProduct
                setActiBtnAndUnActiBtn(btnL, btnS, btnM)
                val name = txtNameProduct.text.toString() + " (L)"
                txtNameProduct.setText(name)
            }
        }
        txtPriceProduct.text = formatPrice.format(priceNow) + " đ"
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when (v?.id) {
            com.example.qthien.t__t.R.id.btnS -> {
                setActiBtnAndUnActiBtn(btnS, btnL, btnM)
                setPriceClick(product!!.priceProduct)
                cartPlus.cart.size = "S"
            }
            com.example.qthien.t__t.R.id.btnM -> {
                setActiBtnAndUnActiBtn(btnM, btnL, btnS)
                setPriceClick(product!!.priceMProduct)
                cartPlus.cart.size = "M"
            }
            com.example.qthien.t__t.R.id.btnL -> {
                setActiBtnAndUnActiBtn(btnL, btnS, btnM)
                setPriceClick(product!!.priceLProduct)
                cartPlus.cart.size = "L"
            }
            com.example.qthien.t__t.R.id.ibtnPlus -> {
                val quantity_now = edtQuantity.text.toString()
                val quantity_plus = quantity_now.toInt() + 1
                edtQuantity.setText(quantity_plus.toString())
                txtPriceProduct.setText("${formatPrice.format(quantity_plus * priceNow)} đ")
                ibtnMinus.isEnabled = true

                if(cart != null){
                    btnAddCart.setBackgroundResource(R.drawable.shape_btn_cart_update)
                    btnAddCart.setImageDrawable(getDrawable(R.drawable.ic_check_white_22dp))
                }

                caculationPriceTmp()
            }
            com.example.qthien.t__t.R.id.ibtnMinus -> {
                val quantity_now = edtQuantity.text.toString()
                val quantity_minus = quantity_now.toInt() - 1

                if (quantity_now.toInt() == 0) {
                    edtQuantity.setText(quantity_now)
                    txtPriceProduct.setText("$priceNow đ")
                    if(cart == null)
                        ibtnMinus.isEnabled = false
                } else {
                    if(quantity_minus == 0) {
                        ibtnMinus.isEnabled = false
                        btnAddCart.setBackgroundResource(R.drawable.shape_btn_cart_remove)
                        btnAddCart.setImageResource(R.drawable.ic_remove_shopping_cart_white_24dp)
                        btnAddCart.setTag(R.drawable.shape_btn_cart_remove)
                    }
                    edtQuantity.setText(quantity_minus.toString())
                    txtPriceProduct.setText("${formatPrice.format(quantity_minus * priceNow)} đ")
                }
                caculationPriceTmp()
            }
            com.example.qthien.t__t.R.id.btnFavorite -> {
                PreProductFavoriteActi(this).favoriteProduct(
                    product!!.idProduct,
                    MainActivity.customer?.idCustomer!!,
                    if (checkFavorite) 0 else 1
                )
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun caculationPriceTmp(){
        var price = priceNow * edtQuantity.text.toString().toInt()
//        for(i in arrToppingSelected)
            price += arrToppingSelected.sumBy { it.priceProduct.toInt() * it.quantity }
        txtSumPrice.setText(formatPrice.format(price) + " đ")
    }

    @SuppressLint("SetTextI18n")
    fun setPriceClick(price: Long) {
        priceNow = price
        val priceNoww = price * edtQuantity.text.toString().toInt()
        txtPriceProduct.setText(formatPrice.format(priceNoww) + " đ")
        caculationPriceTmp()
    }

    fun setActiBtnAndUnActiBtn(btnActi: Button, btnUnActi1: Button, btnUnActi2: Button) {
        btnActi.background = getDrawable(com.example.qthien.t__t.R.drawable.shap_btn_size_acti)
        btnActi.setTextColor(Color.WHITE)
        btnUnActi1.background = getDrawable(com.example.qthien.t__t.R.drawable.shape_btn_size_nomal)
        btnUnActi1.setTextColor(Color.BLACK)
        btnUnActi2.background = getDrawable(com.example.qthien.t__t.R.drawable.shape_btn_size_nomal)
        btnUnActi2.setTextColor(Color.BLACK)
    }

    override fun transmissionNote(note: String) {
        if (note.equals(""))
            edtNote.setHint(com.example.qthien.t__t.R.string.question_your_note)
        else
            edtNote.setHint(com.example.qthien.t__t.R.string.your_note)
        edtNote.setText(note)
    }
}
