package com.example.qthien.t__t.mvp.view.order

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.*
import com.example.qthien.t__t.mvp.view.discount.DiscountActivity
import com.example.qthien.t__t.mvp.view.main.MainActivity.Companion.customer
import kotlinx.android.synthetic.main.fragment_depay.*
import java.text.DecimalFormat

class DepayFragment : Fragment(){

    companion object {
        fun newInstance(
            arrCart: ArrayList<MainProductCart>,
            info: String,
            discount: Discount?
        ) : DepayFragment {
            val f = DepayFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList("carts" , arrCart)
            bundle.putString("info" , info)
            bundle.putParcelable("discount" , discount)
            f.arguments = bundle
            return f
        }
    }

    var callActivity : FragmentOrderCallActivi? = null
    var arrCart : ArrayList<MainProductCart>? = null
    val REQUEST_DISCOUNT = 1
    var pointUsee = 0
    var totalPrice = 0
    var totalPriceMain = 0
    var priceDiscount = 0
    var priceDiscountProduct = 0L
    var info : String? = ""
    var discount : Discount? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is FragmentOrderCallActivi)
            callActivity = context
    }

    override fun onResume() {
        super.onResume()
        (context as OrderActivity).setTitle(R.string.depay)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_depay , container , false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrCart = arguments?.getParcelableArrayList("carts")
        info = arguments?.getString("info")

        val share = context!!.getSharedPreferences("QuantityPrice" , Activity.MODE_PRIVATE)
        totalPrice = share.getInt("price" , 0)
        totalPriceMain = share.getInt("price" , 0)

        if(totalPrice < 50000) {
            totalPrice += 10000
            txtNoti.visibility = View.VISIBLE
        }

        val f = DecimalFormat("###,###,###")

        val discountPass = arguments?.getParcelable<Discount?>("discount")

        if(discountPass != null && discount == null)
            discount = discountPass

        if(!rdoGroup.isEnabled){
            rdoDepayCard.isEnabled = false
            rdoDepayGive.isEnabled = false
        }

        cbDepayPoint.setOnCheckedChangeListener{v , c ->
            if(c){
                val priceToPoint = totalPrice.div(1000)
                if(pointUsee == priceToPoint){
                    rdoGroup.clearCheck()
                    rdoGroup.isEnabled = false
                    rdoDepayCard.isEnabled = false
                    rdoDepayGive.isEnabled = false
                }
            }
            else {
                rdoGroup.isEnabled = true
                rdoDepayCard.isEnabled = true
                rdoDepayGive.isEnabled = true
            }
        }

        if(customer!!.point == null || customer!!.point == 0) {
            cbDepayPoint.setText(R.string.not_point_depay)
            cbDepayPoint.isEnabled = false
        }else{
            checkUsePoint(totalPrice)
        }

        if(discount != null) {
            txtDiscount.setText(discount?.nameDiscount)
            ibtnRemoveDiscount.visibility = View.VISIBLE
            checkInvalidDiscount(discount)
        }
        else {
            ibtnRemoveDiscount.visibility = View.GONE
        }

        ibtnRemoveDiscount.setOnClickListener({
            if(discount?.idProduct != 0)
                totalPriceMain += priceDiscount
            totalPrice += priceDiscount
            Log.d("priceDisss" , "3 " + totalPrice.toString())
            discount = null
            priceDiscountProduct = 0
            priceDiscount = 0
            txtDiscount.setText(R.string.add_discount)
            ibtnRemoveDiscount.visibility = View.GONE
            checkUsePoint(totalPrice)
        })

        txtDiscount.setOnClickListener({
            val i = Intent(context , DiscountActivity::class.java)
            startActivityForResult(i , REQUEST_DISCOUNT)
        })

        edtNoteOrder.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                ibtnClearText.visibility = if(s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
        })
        ibtnClearText.setOnClickListener({
            edtNoteOrder.setText("")
        })
    }

    @SuppressLint("SetTextI18n")
    fun checkUsePoint(price : Int){
        val f = DecimalFormat("###,###,###")

        val priceToPoint = price.div(1000)

        if(priceToPoint <  customer!!.point!!) {
            cbDepayPoint.setText(
                "${getString(R.string.use)} ${priceToPoint} ${getString(R.string.point)} ${getString(
                    R.string.equivalent
                )} ${f.format(price)}đ"
            )
            pointUsee = priceToPoint
        }else{
            var pointAlow = priceToPoint/2
            var pointToPrice = 0

            if(pointAlow < customer!!.point!!) {
                pointToPrice = pointAlow.times(1000)
            }
            else{
                pointToPrice = customer!!.point!!.times(1000)
                pointAlow = customer!!.point!!
            }
            pointUsee = pointAlow
            cbDepayPoint.setText("${getString(R.string.use)} ${pointAlow} ${getString(R.string.point)} ${getString(R.string.equivalent)} ${f.format(pointToPrice)}đ")
        }
    }

    fun checkContinute(){
        var method = 0
        if(cbDepayPoint.isChecked)
            method = 2
        if(rdoDepayCard.isChecked)
            method = method*10 + 3
        if(rdoDepayGive.isChecked)
            method = method*10 + 1


        if(method == 0) {
            Toast.makeText(context, R.string.not_selected_method, Toast.LENGTH_LONG).show()
            return
        }

        if(info != null) {

            val arrDetail = ArrayList<DetailOrder>()

            for(p in arrCart!!){
                val price = getPriceBySize(p)

                val arT = ArrayList<ToppingOrder>()

                for (t in p.arrTopping){
                    arT.add(ToppingOrder(t.idProduct , t.nameProduct , t.priceProduct , t.quantity))
                }

                var total : Long
                if(priceDiscountProduct != 0L && discount?.idProduct == p.idProduct){
                    total = priceDiscountProduct  * p.quantity
                    priceDiscount = 0
                }else
                    total = price * p.quantity

                total += ( p.arrTopping.sumBy { it.quantity * it.priceProduct.toInt() } * p.quantity)
                val d = DetailOrder(0 ,
                    p.idProduct ,
                    p.nameProduct ,
                    p.quantity ,
                    price,
                    p.size,
                    if(discount != null && discount?.idProduct == p.idProduct) priceDiscountProduct else 0L,
                    total,
                    p.note ?: "",
                    arT
                    )
                arrDetail.add(d)
            }

            val order = Order(0, "",
                info!! ,
                customer!!.idCustomer, "" ,
                if(discount != null) discount!!.idDiscount else 0 ,
                discount?.nameDiscount,
                if(totalPriceMain < 50000L) 10000 else 0,
                totalPriceMain.toLong(),
                priceDiscount.toLong(),
                edtNoteOrder.text.toString() , method , if(cbDepayPoint.isChecked) pointUsee else 0 , arrayListOf())

            val orderAdd = OrderAdd(order , arrDetail)
            callActivity?.depayToVerify(info.toString() , orderAdd , discount)
        }
    }

    fun getPriceBySize(p : MainProductCart) : Long {
        if(p.size.equals("L"))
            return p.priceLProduct
        if(p.size.equals("M"))
            return p.priceMProduct
        return p.priceProduct
    }

    fun checkInvalidDiscount(discount : Discount?) : Boolean{
        if(discount?.idProduct != 0){
            Log.d("discountttt" , "1")
            val p = arrCart?.find { it.idProduct == discount?.idProduct }
            if(p != null) {
                priceDiscountProduct = getPriceBySize(p) - discount!!.price.toLong()
                priceDiscount = discount.price*p.quantity
                totalPrice -= discount.price*p.quantity
                totalPriceMain -= discount.price*p.quantity
                checkUsePoint(totalPrice)
                return true
            }else
                return false
        }

//        if(discount.quantityRegulationsPro != 0){
//            Log.d("discountttt" , "2")
//            if(discount.priceMinimun > totalPrice) {
//
//
//                checkUsePoint(totalPrice)
//                return true
//            }
//            this.discount = null
//            return false
//        }

        if(discount.persent != 0){
            Log.d("discountttt" , "3")
            if(discount.priceMinimun != 0){
                if(discount.priceMinimun > totalPrice) {
                    this.discount = null
                    return false
                }
            }
            Log.d("priceDisss" , "1 " + totalPrice.toString())
            priceDiscount = (totalPriceMain * discount.persent) / 100
            totalPrice -= priceDiscount
            Log.d("priceDisss" , "2 " + totalPrice.toString())
            checkUsePoint(totalPrice)
            return true
        }

        if(discount.price != 0){
            Log.d("discountttt" , "4")
            if(discount.priceMinimun != 0){
                if(discount.priceMinimun > totalPrice) {
                    this.discount = null
                    return false
                }
            }
            priceDiscount = discount.price
            totalPrice -= priceDiscount
            Log.d("discountttt" , priceDiscount.toString())
            checkUsePoint(totalPrice)
            return true
        }
        this.discount = null
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_DISCOUNT && resultCode == Activity.RESULT_OK && data != null){
            discount = data.extras?.getParcelable("discount")
            if(checkInvalidDiscount(discount)) {
                txtDiscount.setText(discount?.nameDiscount)
                ibtnRemoveDiscount.visibility = View.VISIBLE
            }else
                Toast.makeText(context , R.string.error_use_discount , Toast.LENGTH_LONG).show()
        }
    }
}