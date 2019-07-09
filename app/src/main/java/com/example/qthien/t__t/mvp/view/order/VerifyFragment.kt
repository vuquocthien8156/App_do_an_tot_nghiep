package com.example.qthien.t__t.mvp.view.order

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.OrderProductAdapter
import com.example.qthien.t__t.model.Discount
import com.example.qthien.t__t.model.OrderAdd
import com.example.qthien.t__t.mvp.presenter.order.PreVerifyFrag
import com.example.qthien.t__t.mvp.view.main.MainActivity.Companion.customer
import kotlinx.android.synthetic.main.fragment_verify.*
import kotlinx.android.synthetic.main.fragment_verify.view.*
import java.text.DecimalFormat

class VerifyFragment : Fragment() , IVerifiyFrag{

    override fun failure(message: String) {
        Toast.makeText(context , message , Toast.LENGTH_LONG).show()
        callActivity?.hideLoader()
    }

    override fun successAddOrder(status: String?) {
        if(status != null && status.equals("Success")){
            if(orderAdd?.infoOrder?.paymentMethod == 3) {
                customer?.point = customer?.point!! + (totalSum / 10000).toInt()
                Log.d("points" , "veri" + (totalSum / 10000).toInt().toString())
            }
            else{
                val methodFirst = orderAdd?.infoOrder?.paymentMethod!!/10
                if(methodFirst == 2 || orderAdd?.infoOrder?.paymentMethod == 2)
                    customer?.point = customer?.point!! - orderAdd!!.infoOrder.point
            }
            Log.d("points" , "veri" + customer?.point.toString())
            callActivity?.successAddOrder()
        }
        else{
            Toast.makeText(context , R.string.fail_again , Toast.LENGTH_LONG).show()
            callActivity?.hideLoader()
        }
    }

    companion object {
        fun newInstance(info : String , order : OrderAdd , discount : Discount?) : VerifyFragment {
            val f = VerifyFragment()
            val bundle = Bundle()
            bundle.putParcelable("order" , order)
            bundle.putParcelable("discount" , discount)
            bundle.putString("info" , info)
            f.arguments = bundle
            return f
        }
    }

    var callActivity : FragmentOrderCallActivi? = null
    val REQUEST_CODE_PAYMENT = 1

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is FragmentOrderCallActivi)
            callActivity = context
    }

    override fun onResume() {
        super.onResume()
        (context as OrderActivity).setTitle(R.string.verify)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_verify , container , false)
    }

    var totalSum = 0L
    var orderAdd : OrderAdd? = null
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderAdd = arguments?.getParcelable("order")
        val discount : Discount? = arguments?.getParcelable("discount")
        val info : String? = arguments?.getString("info")

        val arrInfo = info?.split("-")

        if(arrInfo != null){
            view.txtNameCus.setText(arrInfo.get(0))
            view.txtPhoneCus.setText(arrInfo.get(1))
            view.txtAddressCus.setText(arrInfo.get(2))
        }

        recyProductOrder.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)
        recyProductOrder.addItemDecoration(
            DividerItemDecoration(context,
                LinearLayoutManager.VERTICAL)
        )
        recyProductOrder.adapter = OrderProductAdapter(context!! , orderAdd?.arrDetail!!)

        if(orderAdd!!.infoOrder.discount == 0)
            lnDiscount.visibility = View.GONE
        else{
            lnDiscount.visibility = View.VISIBLE
            txtNameDiscount.setText(orderAdd!!.infoOrder.nameDiscount)
        }

        var method = orderAdd!!.infoOrder.paymentMethod
        var methodDiferenceInt = orderAdd!!.infoOrder.paymentMethod
        if(orderAdd!!.infoOrder.paymentMethod > 20) {
            method = orderAdd!!.infoOrder.paymentMethod / 10
            methodDiferenceInt = orderAdd!!.infoOrder.paymentMethod % 10
            Log.d("Orrredr" , method.toString())
            Log.d("Orrredr" , methodDiferenceInt.toString())
        }
        if(method == 2)
            methodPoint.setText(R.string.depay_point)
        else
            methodPoint.visibility = View.GONE

        Log.d("Orrredr" , (methodDiferenceInt == 1).toString())
        if(methodDiferenceInt == 1)
            methodDifferent.setText(R.string.depay_delivery)
        else
            if(methodDiferenceInt == 3)
                methodDifferent.setText(R.string.depay_card)
            else
                methodDifferent.visibility = View.GONE

        val d = DecimalFormat("###,###,###")

        txtTotalProduct.setText(d.format(orderAdd!!.infoOrder.priceTotal) + "đ")
        txtTotalTransportFee.setText(d.format(orderAdd!!.infoOrder.priceTranfrom) + "đ")

        totalSum = orderAdd!!.infoOrder.priceTotal + orderAdd!!.infoOrder.priceTranfrom

        if(orderAdd!!.infoOrder.point != 0){
            relaPointUse.visibility = View.VISIBLE
            txtPoint.setText(orderAdd!!.infoOrder.point.toString())
            totalSum -= (orderAdd!!.infoOrder.point * 1000)
        }
        else
            relaPointUse.visibility = View.GONE

        if(orderAdd!!.infoOrder.noteOrder.equals(""))
            lnNote.visibility = View.GONE
        else{
            lnNote.visibility = View.VISIBLE
            txtNoteOrder.setText(orderAdd!!.infoOrder.noteOrder)
        }


        Log.d("addOrder" , orderAdd?.infoOrder.toString())
        Log.d("addOrder" , orderAdd?.arrDetail.toString())
        if(discount == null || discount.idProduct != 0) {
            relaDiscount.visibility = View.GONE
            txtTotalSum.setText("${d.format(totalSum)} đ")
        }
        else{
            txtPriceDiscount.setText("${d.format(orderAdd?.infoOrder?.priceTotalDiscount)} đ")
            totalSum -= orderAdd?.infoOrder?.priceTotalDiscount!!
            txtTotalSum.setText("${d.format(totalSum)} đ")
        }
    }

    fun addOrder() {
        val methodAfter = orderAdd?.infoOrder?.paymentMethod!!%10
        if(orderAdd?.infoOrder?.paymentMethod == 3 || methodAfter == 3){
            val i = Intent(context , PaymentOnlineActivity::class.java)
            i.putExtra("total" , totalSum)
            startActivityForResult(i , REQUEST_CODE_PAYMENT);
        }
        else
            PreVerifyFrag(this).addOrder(orderAdd!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_PAYMENT && resultCode == Activity.RESULT_OK)
            PreVerifyFrag(this).addOrder(orderAdd!!)
        else
            callActivity?.hideLoader()
    }
}