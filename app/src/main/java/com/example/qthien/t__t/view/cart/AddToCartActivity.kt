package com.example.qthien.t__t.view.cart

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.qthien.t__t.GlideApp
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

        if(btnAddCart.getTag() != R.drawable.ic_add_shopping_cart_24dp){
            var i = 0
            val arrOld = arrCartOld?.get(position)?.arrTopping
            for(topping in arrToppingEdit){
                val t = arrOld?.find { it.equals(topping) }
                if(t != null)
                    i += 1
            }

            if(arrOld?.size ==  arrToppingEdit.size) {
                if (arrOld.size == i) {
                    checkChange(false)
                    arrToppingSelected.addAll(arrOld)
                } else {
                    checkChange(true)
                    arrToppingSelected.addAll(arrToppingEdit)
                }
            }
            else {
                checkChange(true)
                arrToppingSelected.addAll(arrToppingEdit)
            }
        }
        else {
            arrToppingSelected.addAll(arrToppingEdit)
            Toast.makeText(this, arrToppingEdit.size.toString() , Toast.LENGTH_LONG).show()
        }

        Log.d("btnnnn" , cart.toString())

        adapterOrder.notifyDataSetChanged()

        if(arrToppingSelected.size > 0){
            txtAddtopping.setText(R.string.update_topping)
            txtAddtopping.setTextColor(ContextCompat.getColor(this , R.color.colorPrimaryText))
            relaTopping.setBackgroundColor(Color.parseColor("#ffffff"))
        }
        else {
            txtAddtopping.setText(R.string.add_topping)
            txtAddtopping.setTextColor(ContextCompat.getColor(this , R.color.colorIconsText))
            relaTopping.setBackgroundColor( ContextCompat.getColor(this , R.color.colorSecondaryText))
        }

        caculationPriceTmp()
    }

    override fun failureFavorite(message: String) {
        frmLoader.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun failureAddCart(message: String) {
        frmLoader.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun favoriteProduct(resultCode: String) {
        if (resultCode.equals("ok")) {
            change = true
            checkFavorite = !checkFavorite
            if(checkFavorite){
                arrFavoriteString?.add(product?.idProduct.toString())
            }
            else{
                arrFavoriteString?.remove(product?.idProduct.toString())
            }

            getSharedPreferences("Favorite" , Context.MODE_PRIVATE).edit()
                .putString( "arrFavorite" , arrFavoriteString.toString()).apply()

            setFavorite()
        }
    }

    override fun successAddCart(status: String?) {
        if(status != null && status.equals("Success")){
//            val i = Intent(this , MainActivity::class.java)
//            i.putExtra("change" , change)
//            setResult(Activity.RESULT_OK , i)
            createSharePre()
            finish()
        }
        else {
            Toast.makeText(this, "Add cart fail", Toast.LENGTH_LONG).show()
            frmLoader.visibility = View.GONE
        }
    }

    override fun successUpdateCart(message: String?) {
        if(message != null && message.equals("Success")){
            successUpdateCart()
        }
        else {
            Toast.makeText(this, "Update cart fail", Toast.LENGTH_LONG).show()
            frmLoader.visibility = View.GONE
        }
    }

    override fun successDeleteCart(message: String?) {
        if(message != null && message.equals("Success")){
            if(cart == null){
                Log.d("quantityyyy" , arrCartOld!!.get(checkExist).quantity.toString())
                arrCartOld!!.get(checkExist).quantity += edtQuantity.text.toString().toInt()
                Log.d("quantityyyy" , arrCartOld!!.get(checkExist).quantity.toString())
                PreAddCart(this).updateCart(arrCartOld!!.get(checkExist))
                arrCartOld!!.removeAt(position)
            }
            else
                successUpdateCart()
        }
        else {
            Toast.makeText(this, "Delete cart fail", Toast.LENGTH_LONG).show()
            frmLoader.visibility = View.GONE
        }
    }

    fun successUpdateCart(){
        val i = Intent(this , CartActivity::class.java)
        i.putParcelableArrayListExtra("arrCartNews" , arrCartOld)
        setResult(Activity.RESULT_OK , i)
        finish()
    }

    override fun resultGetProductFavorite(arrResult: ArrayList<Product>?) {}

        private var product: Product? = null

        private var checkFavorite = false

        private var priceNow = 0L

        private var formatPrice = DecimalFormat("###,###,###")

        private lateinit var adapterOrder: ToppingSelectedAdapter
        private lateinit var arrToppingSelected : ArrayList<ToppingProductCart>

        private var cart : MainProductCart? = null
        private var arrCartOld : ArrayList<MainProductCart>? = null
        private var position : Int = -1

        private var arrFavoriteString : ArrayList<String>? = null

        private var checkExist = -1
        private var change = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_add_to_cart)

        product = intent.extras!!.get("product") as Product?

        arrCartOld = intent.extras!!.getParcelableArrayList("arrCart")

        btnAddCart.setTag(R.drawable.ic_add_shopping_cart_24dp)

        arrToppingSelected = ArrayList()

        if(arrCartOld != null) {
            position = intent.extras!!.getInt("position")
            cart = arrCartOld?.get(position)?.copy()
            loadCart()
        }
        else
            cart = MainProductCart(0 , product!!.idProduct , "" , 0L, 0L , 0L,
                0 , "" , 1 , "" , "" , arrToppingSelected)

        imgClose.setOnClickListener({
//            val i = Intent(this , MainActivity::class.java)
//            i.putExtra("change" , change)
//            setResult(Activity.RESULT_CANCELED , i)
            createSharePre()
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

    fun checkChange(b : Boolean){
        if(b){
            btnAddCart.setTag(R.drawable.ic_check_white_22dp)
            btnAddCart.setImageDrawable(getDrawable(R.drawable.ic_check_white_22dp))
            btnAddCart.setBackgroundResource(R.drawable.shape_btn_cart_update)
        }
        else{
            btnAddCart.setTag(R.drawable.ic_arrow_back_white_24dp)
            btnAddCart.setBackgroundResource(R.drawable.shape_btn_cart_remove)
            btnAddCart.setImageDrawable(getDrawable(R.drawable.ic_arrow_back_white_24dp))
        }
    }

    fun checkFavorite() {
        val stringFavorite = getSharedPreferences("Favorite", Context.MODE_PRIVATE).getString("arrFavorite", null)
        if (stringFavorite != null) {
            val arrFavorite = stringFavorite.replace("[", "").replace("]", "").split(",")
            arrFavoriteString = ArrayList()
            arrFavoriteString?.addAll(arrFavorite)
            if (arrFavorite.find { it.trim().equals(product?.idProduct.toString()) } != null) {
                checkFavorite = true
            } else
                checkFavorite = false
            setFavorite()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadCart() {
        product = Product(cart!!.idProduct,
            cart!!.nameProduct, cart!!.priceProduct , cart!!.priceMProduct , cart!!.priceLProduct ,
            "" , 0 , cart!!.imageUrl , "" , 0 , cart!!.mainCatalogy)

        if(cart!!.arrTopping.size > 0) {
            arrToppingSelected.addAll(cart!!.arrTopping)
            cart!!.arrTopping = arrToppingSelected
            txtAddtopping.setTextColor(ContextCompat.getColor(this , R.color.colorPrimaryText))
            relaTopping.setBackgroundColor(Color.parseColor("#ffffff"))
            txtAddtopping.setText(R.string.update_topping)
        }

        edtQuantity.setText(cart!!.quantity.toString())

        if(cart != null)
            edtNote.setText(cart!!.note)

        edtNote.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(edtNote.text?.equals(cart!!.note) ?: false)
                    checkChange(false)
                else
                    checkChange(true)
            }
        })

        btnAddCart.setBackgroundResource(R.drawable.shape_btn_cart_remove)
        btnAddCart.setImageDrawable(getDrawable(R.drawable.ic_arrow_back_white_24dp))
        btnAddCart.setTag(R.drawable.ic_arrow_back_white_24dp)
    }

    fun eventAddCart(){
        btnAddCart.setOnClickListener({
            when(btnAddCart.getTag() as Int){
                R.drawable.ic_add_shopping_cart_24dp ->{
                    val cartPlus = CartPlus(MainActivity.customer!!.idCustomer , cart!!)
                    PreAddCart(this).addCart(cartPlus)
                    frmLoader.visibility = View.VISIBLE
                }
                R.drawable.ic_check_white_22dp -> {
                    checkExist = -1
                    val idProduct = cart?.idProduct
                    val size = cart?.size
                    val note = cart?.note
                    val topping = cart?.arrTopping ?: ArrayList()

                    for(i in 0 until arrCartOld!!.size){
                        if(i != position){
                            val idProductOld = arrCartOld!![i].idProduct
                            val sizeOld = arrCartOld!![i].size
                            val noteOld = arrCartOld!![i].note
                            val toppingOld = arrCartOld!![i].arrTopping

                            if(idProductOld == idProduct
                                    && sizeOld.equals(size)
                                    && noteOld.equals(note)){
                                var a = 0
                                for(top in topping){
                                    val t = toppingOld.find { it.equals(top) }
                                    if(t != null)
                                        a += 1
                                }

                                if(arrCartOld!![i].arrTopping.size == a)
                                    checkExist = i
                            }
                        }
                    }
                    Log.d("checkExist", checkExist.toString())

                    frmLoader.visibility = View.VISIBLE

                    if(checkExist != -1){
                        PreAddCart(this).deleteCart(cart!!.idProductCart)
                        cart = null
                    }
                    else{
                        if(cart != null) {
                            arrCartOld!![position] = cart!!
                            PreAddCart(this).updateCart(cart!!)
                        }
                    }
                }
                R.drawable.ic_remove_shopping_cart_white_24dp ->{
                    if(cart != null) {
                        Log.d("DeleteCarrtt" , cart!!.idProductCart.toString())
                        arrCartOld?.removeAt(position)
                        PreAddCart(this).deleteCart(cart!!.idProductCart)
                        frmLoader.visibility = View.VISIBLE
                    }
                }

                R.drawable.ic_arrow_back_white_24dp -> {
                    finish()
                }
            }
        })
    }

    fun createSharePre(){
        getSharedPreferences("Favorite" , Context.MODE_PRIVATE).edit()
                .putBoolean( "changeFavorite" , change).apply()
    }

    override fun onBackPressed() {
        createSharePre()
        super.onBackPressed()
    }

    fun setFavorite() {
        if (checkFavorite) {
            btnFavorite.setImageDrawable(getDrawable(com.example.qthien.t__t.R.drawable.ic_favorite_white_24dp))
            btnFavorite.setBackgroundResource(com.example.qthien.t__t.R.drawable.shape_btn_unn_favorite)
        } else {
            btnFavorite.setImageDrawable(getDrawable(com.example.qthien.t__t.R.drawable.ic_favorite_red_24dp))
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
                if (arrCartOld == null) {
                    cart?.size = "S"
                    setActiBtnAndUnActiBtn(btnS, btnL, btnM, "S")
                }
            }
            product!!.priceMProduct != 0.toLong() -> {
                priceNow = product!!.priceMProduct
                setActiBtnAndUnActiBtn(btnM, btnL, btnS, "M")
                val name = txtNameProduct.text.toString() + " (M)"
                if (arrCartOld == null ) {
                    cart?.size = "M"
                    txtNameProduct.setText(name)
                }
            }
            product!!.priceLProduct != 0.toLong() -> {
                priceNow = product!!.priceLProduct
                setActiBtnAndUnActiBtn(btnL, btnS, btnM, "L")
                val name = txtNameProduct.text.toString() + " (L)"
                if (arrCartOld == null ){
                    cart?.size = "L"
                    txtNameProduct.setText(name)
                }
            }
        }

        Log.d("btnnnn" , cart!!.size)
        if(arrCartOld != null){
            when(cart!!.size){
                "S" -> { btnS.performClick() }
                "M" -> { btnM.performClick() }
                "L" -> { btnL.performClick() }
            }
        }

        txtPriceProduct.text = formatPrice.format(priceNow) + " đ"
    }

    fun checkChangeSize(size : String){
        if(btnAddCart.getTag() != R.drawable.ic_add_shopping_cart_24dp) {
            if (arrCartOld?.get(position)?.size?.equals(size) ?: false)
                checkChange(false)
            else {
                checkChange(true)
            }
            cart?.size = size
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when (v?.id) {
            com.example.qthien.t__t.R.id.btnS -> {
                priceNow = product!!.priceProduct
                setActiBtnAndUnActiBtn(btnS, btnL, btnM , "S")
            }
            com.example.qthien.t__t.R.id.btnM -> {
                priceNow = product!!.priceMProduct
                setActiBtnAndUnActiBtn(btnM, btnL, btnS , "M")
            }
            com.example.qthien.t__t.R.id.btnL -> {
                priceNow = product!!.priceLProduct
                setActiBtnAndUnActiBtn(btnL, btnS, btnM , "L")
            }
            com.example.qthien.t__t.R.id.ibtnPlus -> {
                val quantity_now = edtQuantity.text.toString()
                val quantity_plus = quantity_now.toInt() + 1
                edtQuantity.setText(quantity_plus.toString())
                ibtnMinus.isEnabled = true
                cart?.quantity = quantity_plus
                checkQuantity(quantity_plus)

                caculationPriceTmp()
            }
            com.example.qthien.t__t.R.id.ibtnMinus -> {
                val quantity_now = edtQuantity.text.toString()
                val quantity_minus = quantity_now.toInt() - 1

                if (quantity_minus > 0) {
                    checkQuantity(quantity_minus)
                    edtQuantity.setText(quantity_minus.toString())
                    cart?.quantity = quantity_minus
                } else {
                    if(arrCartOld == null)
                        ibtnMinus.isEnabled = false
                    else{
                        edtQuantity.setText(quantity_now)
                        cart?.quantity = quantity_now.toInt()

                        btnAddCart.setTag(R.drawable.ic_arrow_back_white_24dp)
                        btnAddCart.setBackgroundResource(R.drawable.shape_btn_cart_remove)
                        btnAddCart.setImageDrawable(getDrawable(R.drawable.ic_arrow_back_white_24dp))
                    }
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

    fun checkQuantity(quantity : Int){
        if(arrCartOld != null){
            if(arrCartOld!!.get(position).quantity == quantity)
                checkChange(false)
            else
                checkChange(true)
        }
    }

    @SuppressLint("SetTextI18n")
    fun caculationPriceTmp(){
        var price = priceNow * edtQuantity.text.toString().toInt()
        price += arrToppingSelected.sumBy { it.priceProduct.toInt() * it.quantity }
        txtSumPrice.setText(formatPrice.format(price) + " đ")
    }

    fun setActiBtnAndUnActiBtn(
        btnActi: Button,
        btnUnActi1: Button,
        btnUnActi2: Button,
        s: String
    ) {
        btnActi.background = getDrawable(com.example.qthien.t__t.R.drawable.shap_btn_size_acti)
        btnActi.setTextColor(Color.WHITE)
        btnUnActi1.background = getDrawable(com.example.qthien.t__t.R.drawable.shape_btn_size_nomal)
        btnUnActi1.setTextColor(Color.BLACK)
        btnUnActi2.background = getDrawable(com.example.qthien.t__t.R.drawable.shape_btn_size_nomal)
        btnUnActi2.setTextColor(Color.BLACK)

        caculationPriceTmp()
        cart?.size = s
        checkChangeSize(s)
        txtNameProduct.text = product?.nameProduct + " ($s)"
        txtPriceProduct.setText("${formatPrice.format(priceNow)} đ")
    }

    override fun transmissionNote(note: String) {
        if (note.equals(""))
            edtNote.setHint(com.example.qthien.t__t.R.string.question_your_note)
        else
            edtNote.setHint(com.example.qthien.t__t.R.string.your_note)
        edtNote.setText(note)
    }
}
