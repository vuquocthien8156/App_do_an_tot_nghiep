package com.example.qthien.t__t.mvp.view.order

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.Discount
import com.example.qthien.t__t.model.MainProductCart
import com.example.qthien.t__t.model.OrderAdd
import kotlinx.android.synthetic.main.activity_order.*

class OrderActivity : AppCompatActivity(), FragmentOrderCallActivi {
    override fun hideLoader() {
        frmLoader.visibility =  View.GONE
    }

    override fun successAddOrder() {
        setResult(Activity.RESULT_OK , Intent())
        finish()
    }

    override fun depayToVerify(info: String , orderAdd : OrderAdd , discount : Discount?) {
        fragmentVerify = VerifyFragment.newInstance(info , orderAdd , discount)
        addFragmentContinute(fragmentVerify!!)
        btnContinute.setTag("fragmentVerify")
        btnContinute.setText(R.string.depay)
    }

    override fun addressValidation(info: String) {
            val arrCart = intent.extras?.getParcelableArrayList<MainProductCart>("Cart")
        val discount = intent.extras?.getParcelable<Discount?>("discount")
        fragmentDepay = DepayFragment.newInstance(arrCart!! , info , discount)
            addFragmentContinute(fragmentDepay!!)
            btnContinute.setTag("fragmentDepay")
            btnContinute.setText(R.string.continute)
    }

    var fragmentAddress: AddressFragment? = null
    var fragmentDepay: DepayFragment? = null
    var fragmentVerify: VerifyFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        setSupportActionBar(toolbarOrder)
        toolbarOrder.setTitleTextColor(Color.WHITE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)

        fragmentAddress = AddressFragment.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.frmOrder, fragmentAddress!!).commit()
        btnContinute.setTag("fragmentAddress")

        btnContinute.setOnClickListener({
            when (btnContinute.tag) {
                "fragmentAddress" -> {
                    fragmentAddress?.checkValidation()
                }
                "fragmentDepay" -> {
                    fragmentDepay?.checkContinute()
                }
                "fragmentVerify" -> {
                    frmLoader.visibility = View.VISIBLE
                    fragmentVerify?.addOrder()
                }
            }
            setUpIcon()
        })
    }

    fun setUpIcon() {
        when (btnContinute.tag) {
            "fragmentAddress" -> {
                imgArrow1.setImageResource(R.drawable.ic_arrow_order_unenable)

                lnDepay.setBackgroundResource(R.drawable.shape_stoke_order_unenable)
                lnImgDepay.setBackgroundResource(R.drawable.shape_img_order_unenable)

                txtDepay.setTextColor(ContextCompat.getColor(this, R.color.colorSecondaryText))

                btnContinute.setText(R.string.verify_info)

            }
            "fragmentDepay" -> {
                imgArrow1.setImageResource(R.drawable.ic_arrow_order)
                imgArrow2.setImageResource(R.drawable.ic_arrow_order_unenable)

                lnDepay.setBackgroundResource(R.drawable.shape_stoke_order)
                lnImgDepay.setBackgroundResource(R.drawable.shape_img_order)

                lnVerify.setBackgroundResource(R.drawable.shape_stoke_order_unenable)
                lnImgVeriry.setBackgroundResource(R.drawable.shape_img_order_unenable)

                txtDepay.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                txtVerify.setTextColor(ContextCompat.getColor(this, R.color.colorSecondaryText))
                btnContinute.setText(R.string.continute)
            }
            "fragmentVerify" -> {
                imgArrow2.setImageResource(R.drawable.ic_arrow_order)

                lnVerify.setBackgroundResource(R.drawable.shape_stoke_order)
                lnImgVeriry.setBackgroundResource(R.drawable.shape_img_order)

                txtVerify.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                btnContinute.setText(R.string.depay)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (!btnContinute.tag.equals("fragmentAddress")) {
            when (btnContinute.tag) {
                "fragmentDepay" -> btnContinute.setTag("fragmentAddress")
                "fragmentVerify" -> btnContinute.setTag("fragmentDepay")
            }

            supportFragmentManager.popBackStack()
            setUpIcon()
        } else
            super.onBackPressed()
    }

    fun addFragmentContinute(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frmOrder, fragment).addToBackStack("a").commit()
    }
}
