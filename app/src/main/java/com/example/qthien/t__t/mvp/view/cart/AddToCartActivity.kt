package com.example.qthien.t__t.mvp.view.cart

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.chip.Chip
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
import com.example.qthien.t__t.mvp.presenter.pre_cart.PreAddCart
import com.example.qthien.t__t.mvp.presenter.pre_product_favorite.PreProductFavoriteActi
import com.example.qthien.t__t.mvp.view.main.MainActivity
import com.example.qthien.t__t.mvp.view.product_favorite.IViewProductFavoriteActi
import com.example.qthien.t__t.retrofit2.RetrofitInstance
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

        if (btnAddCart.getTag() != R.drawable.ic_add_shopping_cart_24dp) {
            var i = 0
            val arrOld = arrCartOld?.get(position)?.arrTopping
            for (topping in arrToppingEdit) {
                val t = arrOld?.find { it.equals(topping) }
                if (t != null)
                    i += 1
            }

            if (arrOld?.size == arrToppingEdit.size) {
                if (arrOld.size == i) {
                    checkChange(false)
                    arrToppingSelected.addAll(arrOld)
                } else {
                    checkChange(true)
                    arrToppingSelected.addAll(arrToppingEdit)
                }
            } else {
                checkChange(true)
                arrToppingSelected.addAll(arrToppingEdit)
            }
        } else {
            arrToppingSelected.addAll(arrToppingEdit)
        }

        Log.d("btnnnn", arrToppingEdit.toString())

        adapterOrder.notifyDataSetChanged()

        if (arrToppingSelected.size > 0) {
            txtAddtopping.setText(R.string.update_topping)
            txtAddtopping.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryText))
            relaTopping.setBackgroundColor(Color.parseColor("#ffffff"))
        } else {
            txtAddtopping.setText(R.string.add_topping)
            txtAddtopping.setTextColor(ContextCompat.getColor(this, R.color.colorIconsText))
            relaTopping.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSecondaryText))
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
            checkFavorite = !checkFavorite
            Log.d("arrrFavoriteeeeeAdd" , checkFavorite.toString())
            Log.d("arrrFavoriteeeeeAdd" , product?.idProduct.toString())
            if (checkFavorite) {
                arrFavoriteString?.add(product?.idProduct.toString().trim())
            } else {
                for(i in 0 until arrFavoriteString!!.size){
                    Log.d("arrrFavoriteeeeeAdd" , arrFavoriteString!!.get(i))

                    if(arrFavoriteString!!.get(i).trim().equals(product?.idProduct.toString())) {
                        arrFavoriteString?.removeAt(i)
                        Log.d("arrrFavoriteeeeeAdd" , i.toString())
                        break
                    }
                }
            }
            Log.d("arrrFavoriteeeeeAdd" , arrFavoriteString.toString())
            getSharedPreferences("Favorite", Context.MODE_PRIVATE).edit()
                    .putString("arrFavorite", arrFavoriteString.toString()).apply()

            setFavorite()
        }
    }

    override fun successAddCart(status: String?) {
        if (status != null && status.equals("Success")) {
            createSharePre()
            updateQuantityAndPricePre()
            finish()
        } else {
            Toast.makeText(this, R.string.fail_again, Toast.LENGTH_LONG).show()
            frmLoader.visibility = View.GONE
        }
    }

    override fun successUpdateCart(message: String?) {
        if (message != null && message.equals("Success")) {
            updateQuantityAndPricePre()
            successUpdateCart()
        } else {
            Toast.makeText(this, R.string.fail_again, Toast.LENGTH_LONG).show()
            frmLoader.visibility = View.GONE
        }
    }

    override fun successDeleteCart(message: String?) {
        if (message != null && message.equals("Success")) {
            if (cart == null) {
                arrCartOld!!.get(checkExist).quantity += edtQuantity.text.toString().toInt()
                PreAddCart(this).updateCart(arrCartOld!!.get(checkExist))
                arrCartOld!!.removeAt(position)
            } else
                successUpdateCart()
        } else {
            Toast.makeText(this, R.string.fail_again, Toast.LENGTH_LONG).show()
            frmLoader.visibility = View.GONE
        }
    }

    fun successUpdateCart() {
        updateQuantityAndPricePre()
        val i = Intent(this, CartActivity::class.java)
        i.putParcelableArrayListExtra("arrCartNews", arrCartOld)
        setResult(Activity.RESULT_OK, i)
        finish()
    }

    override fun resultGetProductFavorite(arrResult: ArrayList<Product>?) {}

    private var product: Product? = null

    private var checkFavorite = false
    private var checkFavoriteOld = false

    private var priceNow = 0L
    private var priceSum = 0L

    private var formatPrice = DecimalFormat("###,###,###")

    private lateinit var adapterOrder: ToppingSelectedAdapter
    private lateinit var arrToppingSelected: ArrayList<ToppingProductCart>

    private var cart: MainProductCart? = null
    private var arrCartOld: ArrayList<MainProductCart>? = null
    private var position: Int = -1

    private var arrFavoriteString: ArrayList<String>? = null

    private var checkExist = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_add_to_cart)

        product = intent.extras!!.get("product") as Product?

        arrCartOld = intent.extras!!.getParcelableArrayList("arrCart")

        btnAddCart.setTag(R.drawable.ic_add_shopping_cart_24dp)

        arrToppingSelected = ArrayList()

        if (arrCartOld != null) {
            position = intent.extras!!.getInt("position")
            cart = arrCartOld?.get(position)?.copy()
            loadCart()
        } else
            cart = MainProductCart(0, product!!.idProduct, "", 0L, 0L, 0L,
                    0, "", 1, "", "", arrToppingSelected)

        imgClose.setOnClickListener({
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

        Log.d("proddđ", product.toString())
        if (product!!.mainCatalogy == 1) {
            relaTopping.visibility = View.GONE
            inputLayout.visibility = View.GONE
            line3.visibility = View.GONE
            txtAddtopping.visibility = View.GONE
            chipGroup.visibility = View.GONE
        } else {
            adapterOrder = ToppingSelectedAdapter(this, arrToppingSelected)

            recylerToppingSelected.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            recylerToppingSelected.adapter = adapterOrder
        }

        relaTopping.setOnClickListener({
            val selectedTopping = DialogSheetTopping()
            val bundle = Bundle()
            bundle.putParcelableArrayList("topping", ArrayList(arrToppingSelected))
            selectedTopping.arguments = bundle
            selectedTopping.show(supportFragmentManager, "dialog")
        })

        eventAddCart()
        checkFavorite()

        setUpChipGroupFoNote()

        edtNote.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                ibtnClearText.visibility = if(s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
        })

        ibtnClearText.setOnClickListener {
            edtNote.setText("")
        }
    }

    @SuppressLint("SetTextI18n")
    fun setUpChipGroupFoNote(){
        val arrNote = arrayListOf(getString(R.string.note_not_ice) ,
            getString(R.string.note_little_ice),
            getString(R.string.note_much_sweet) ,
            getString(R.string.note_less_sweet) ,
            getString(R.string.note_sweet))

        for(textChip in arrNote) {
            // Initialize a new chip instance
            val chip = Chip(chipGroup.context , null , R.attr.CustomChipChoiceStyle)
            chip.setText(textChip)

            // Make the chip clickable
            chip.isClickable = true
            chip.isCheckable = false

            // Set the chip click listener
            chip.setOnClickListener {
                val text = edtNote.text.toString()
                if(text.length > 0)
                    edtNote.setText("$text , $textChip")
                else
                    edtNote.setText(textChip)
                edtNote.setSelection(edtNote.text.toString().length)
            }

            // Finally, add the chip to chip group
            chipGroup.addView(chip)
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
                checkFavoriteOld = true
            } else {
                checkFavorite = false
                checkFavoriteOld = false
            }
            setFavorite()
        }
    }

    fun checkChange(b: Boolean) {
        if (b) {
            btnAddCart.setTag(R.drawable.ic_check_white_22dp)
            btnAddCart.setImageDrawable(getDrawable(R.drawable.ic_check_white_22dp))
            btnAddCart.setBackgroundResource(R.drawable.shape_btn_cart_update)
        } else {
            btnAddCart.setTag(R.drawable.ic_arrow_back_white_24dp)
            btnAddCart.setBackgroundResource(R.drawable.shape_btn_cart_remove)
            btnAddCart.setImageDrawable(getDrawable(R.drawable.ic_arrow_back_white_24dp))
        }
    }

    fun checkChangeInfo(){
        var b = false

        if (btnAddCart.getTag() != R.drawable.ic_add_shopping_cart_24dp) {
//            Log.d("changeeeeee" , "${arrCartOld?.get(position)?.size} -- ${cart!!.size} ")
//            Log.d("changeeeeee" , "${arrCartOld?.get(position)?.quantity} -- ${edtQuantity.text}")
//            Log.d("changeeeeee" , "${edtNote.text} -- ${arrCartOld?.get(position)?.note}")
//
//            Log.d("changeeeeee" , (!arrCartOld!!.get(position).size.equals(cart!!.size)).toString())
//            Log.d("changeeeeee" , (arrCartOld?.get(position)?.quantity != edtQuantity.text.toString().toInt()).toString())
//            Log.d("changeeeeee" , (!edtNote.text.toString().equals(arrCartOld?.get(position)?.note  ?: "")).toString())

            if (!arrCartOld!!.get(position).size.equals(cart!!.size))
                b = true

            if(arrCartOld?.get(position)?.quantity != edtQuantity.text.toString().toInt())
                b = true

            if (!edtNote.text.toString().equals(arrCartOld?.get(position)?.note ?: ""))
                b = true

            checkChange(b)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadCart() {
        product = Product(cart!!.idProduct,
                cart!!.nameProduct, cart!!.priceProduct, cart!!.priceMProduct, cart!!.priceLProduct,
                "",  cart!!.imageUrl, "", 0, cart!!.mainCatalogy)

        if (cart!!.arrTopping.size > 0) {
            arrToppingSelected.addAll(cart!!.arrTopping)
            txtAddtopping.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryText))
            relaTopping.setBackgroundColor(Color.parseColor("#ffffff"))
            txtAddtopping.setText(R.string.update_topping)
        }

        cart!!.arrTopping = arrToppingSelected
        edtQuantity.setText(cart!!.quantity.toString())

        if (!cart!!.note.equals(""))
            edtNote.setText(cart!!.note)

        edtNote.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                cart?.note = s.toString()
                checkChangeInfo()
            }
        })

        btnAddCart.setBackgroundResource(R.drawable.shape_btn_cart_remove)
        btnAddCart.setImageDrawable(getDrawable(R.drawable.ic_arrow_back_white_24dp))
        btnAddCart.setTag(R.drawable.ic_arrow_back_white_24dp)
    }

    fun updateQuantityAndPricePre() {
        val share = getSharedPreferences("QuantityPrice", Activity.MODE_PRIVATE)

        val edit = share.edit()

        if (arrCartOld != null) {
            if( arrCartOld!!.size > 0) {
                edit.putInt("quantity", arrCartOld!!.sumBy { it.quantity })
                edit.putInt("price", sumPrice())
                Log.d("halhfiuhdsflas" , sumPrice().toString())
            }
            else
                edit.clear()
        } else {
            val quantity = share.getInt("quantity", 0)
            val price = share.getInt("price", 0)
            edit.putInt("quantity", (quantity + cart!!.quantity))
            edit.putInt("price", (price + priceSum.toInt()))
        }

        edit.apply()
    }

    fun sumPrice(): Int {
        var priceSum = 0

        val priceTopping = arrCartOld!!.sumBy { it.arrTopping.sumBy { (it.quantity*it.priceProduct).toInt() } * it.quantity }
        priceSum += priceTopping
        for (c in arrCartOld!!) {
            when (c.size) {
                "S" -> priceSum += c.priceProduct.toInt() * c.quantity
                "M" -> priceSum += c.priceMProduct.toInt() * c.quantity
                "L" -> priceSum += c.priceLProduct.toInt() * c.quantity
            }
        }
        return priceSum
    }

    fun eventAddCart() {
        btnAddCart.setOnClickListener({
            when (btnAddCart.getTag() as Int) {
                R.drawable.ic_add_shopping_cart_24dp -> {
                    cart?.note = edtNote.text.toString()
                    val cartPlus = CartPlus(MainActivity.customer!!.idCustomer, cart!!)
                    PreAddCart(this).addCart(cartPlus)
                    frmLoader.visibility = View.VISIBLE
                }
                R.drawable.ic_check_white_22dp -> {
                    checkExist = -1
                    val idProduct = cart?.idProduct
                    val size = cart?.size
                    val note = cart?.note
                    val topping = cart?.arrTopping ?: ArrayList()

                    for (i in 0 until arrCartOld!!.size) {
                        if (i != position) {
                            val idProductOld = arrCartOld!![i].idProduct
                            val sizeOld = arrCartOld!![i].size
                            val noteOld = arrCartOld!![i].note
                            val toppingOld = arrCartOld!![i].arrTopping

                            Log.d("checkExist", noteOld.toString())
                            Log.d("checkExist", note.toString())
                            Log.d("checkExist", (noteOld.equals(note)).toString())
                            Log.d("checkExist", (noteOld?.trim().equals(note?.trim())).toString())

                            if (idProductOld == idProduct
                                    && sizeOld.equals(size)
                                    && noteOld.equals(note)) {
                                var a = 0
                                for (top in topping) {
                                    val t = toppingOld.find { it.equals(top) }
                                    if (t != null)
                                        a += 1
                                }

                                if (arrCartOld!![i].arrTopping.size == a)
                                    checkExist = i
                            }
                        }
                    }

                    frmLoader.visibility = View.VISIBLE

                    if (checkExist != -1) {
                        PreAddCart(this).deleteCart(cart!!.idProductCart)
                        cart = null
                    } else {
                        if (cart != null) {
                            arrCartOld!![position] = cart!!
                            PreAddCart(this).updateCart(cart!!)
                        }
                    }
                }
                R.drawable.ic_remove_shopping_cart_white_24dp -> {
                    if (cart != null) {
                        Log.d("DeleteCarrtt", cart!!.idProductCart.toString())
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

    fun createSharePre() {
        Log.d("changeeeeFavo" ,"add " + (checkFavorite != checkFavoriteOld).toString())
        if (checkFavorite != checkFavoriteOld)
            getSharedPreferences("Favorite", Context.MODE_PRIVATE).edit()
                    .putBoolean("changeFavorite", true).apply()
    }

    override fun onResume() {
        super.onResume()
        Log.d("btnnnns", arrToppingSelected.toString())
    }

    override fun onBackPressed() {
        createSharePre()
        super.onBackPressed()
    }

    fun setFavorite() {
        if (checkFavorite) {
            btnFavorite.setImageDrawable(getDrawable(com.example.qthien.t__t.R.drawable.ic_un_favorite_white_24))
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
                setActiBtnAndUnActiBtn(btnS, btnL, btnM)
                val name = product?.nameProduct + " (S)"
                if(arrCartOld == null)
                    cart!!.size = "S"
                txtNameProduct.setText(name)
            }
            product!!.priceMProduct != 0.toLong() -> {
                priceNow = product!!.priceMProduct
                setActiBtnAndUnActiBtn(btnM, btnL, btnS)
                val name = product?.nameProduct + " (M)"
                if(arrCartOld == null)
                    cart!!.size = "M"
                txtNameProduct.setText(name)
            }
            product!!.priceLProduct != 0.toLong() -> {
                priceNow = product!!.priceLProduct
                setActiBtnAndUnActiBtn(btnL, btnS, btnM)
                val name = product?.nameProduct + " (L)"
                if(arrCartOld == null)
                    cart!!.size = "L"
                txtNameProduct.setText(name)
            }
        }

        Log.d("btnnnn", cart!!.size)
        if (arrCartOld != null) {
            when (cart!!.size) {
                "S" -> btnS.performClick()
                "M" -> btnM.performClick()
                "L" -> btnL.performClick()
            }
        }
        txtPriceProduct.text = formatPrice.format(priceNow) + " đ"
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when (v?.id) {
            com.example.qthien.t__t.R.id.btnS -> {
                priceNow = product!!.priceProduct
                cart?.size = "S"
                checkChangeInfo()
                setActiBtnAndUnActiBtn(btnS, btnL, btnM)
            }
            com.example.qthien.t__t.R.id.btnM -> {
                priceNow = product!!.priceMProduct
                cart?.size = "M"
                checkChangeInfo()
                setActiBtnAndUnActiBtn(btnM, btnL, btnS)
            }
            com.example.qthien.t__t.R.id.btnL -> {
                priceNow = product!!.priceLProduct
                cart?.size = "L"
                checkChangeInfo()
                setActiBtnAndUnActiBtn(btnL, btnS, btnM)
            }
            com.example.qthien.t__t.R.id.ibtnPlus -> {
                val quantity_now = edtQuantity.text.toString()
                val quantity_plus = quantity_now.toInt() + 1
                edtQuantity.setText(quantity_plus.toString())
                ibtnMinus.isEnabled = true
                checkChangeInfo()

                cart?.quantity = quantity_plus

                caculationPriceTmp()
            }
            com.example.qthien.t__t.R.id.ibtnMinus -> {
                val quantity_now = edtQuantity.text.toString()
                val quantity_minus = quantity_now.toInt() - 1

                if (quantity_minus > 0) {
                    edtQuantity.setText(quantity_minus.toString())
                    checkChangeInfo()
                    cart?.quantity = quantity_minus

                } else {
                    if (arrCartOld != null) {
                        edtQuantity.setText(quantity_minus.toString())
                        cart?.quantity = quantity_minus

                        btnAddCart.setTag(R.drawable.ic_remove_shopping_cart_white_24dp)
                        btnAddCart.setBackgroundResource(R.drawable.shape_btn_cart_remove)
                        btnAddCart.setImageDrawable(getDrawable(R.drawable.ic_remove_shopping_cart_white_24dp))
                    }
                    ibtnMinus.isEnabled = false
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
    fun caculationPriceTmp() {
        var price = priceNow * edtQuantity.text.toString().toInt()
        price += arrToppingSelected.sumBy { it.priceProduct.toInt() * it.quantity } * edtQuantity.text.toString().toInt()
        priceSum = price
        txtSumPrice.setText(formatPrice.format(price) + " đ")
    }

    @SuppressLint("SetTextI18n")
    fun setActiBtnAndUnActiBtn(
            btnActi: Button,
            btnUnActi1: Button,
            btnUnActi2: Button
    ) {
        btnActi.background = getDrawable(com.example.qthien.t__t.R.drawable.shap_btn_size_acti)
        btnActi.setTextColor(Color.WHITE)
        btnUnActi1.background = getDrawable(com.example.qthien.t__t.R.drawable.shape_btn_size_nomal)
        btnUnActi1.setTextColor(Color.BLACK)
        btnUnActi2.background = getDrawable(com.example.qthien.t__t.R.drawable.shape_btn_size_nomal)
        btnUnActi2.setTextColor(Color.BLACK)

        caculationPriceTmp()
        txtNameProduct.setText("${product?.nameProduct} (${cart?.size})")
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
