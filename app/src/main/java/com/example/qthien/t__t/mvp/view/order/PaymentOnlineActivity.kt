package com.example.qthien.t__t.mvp.view.order

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.qthien.t__t.mvp.presenter.order.PrePaymentOnline
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.TokenCallback
import com.stripe.android.model.Token
import kotlinx.android.synthetic.main.activity_payment_online.*


class PaymentOnlineActivity : AppCompatActivity() , IPaymentOnline {
    override fun failure(message: String) {
        frmLoader.visibility = View.GONE
        Toast.makeText(this@PaymentOnlineActivity, message, Toast.LENGTH_LONG).show()
    }

    override fun successPaymentOnline(status: String?) {
        if(status != null && status.equals("Success")){

            setResult(Activity.RESULT_OK , Intent())
            finish()
        }
        else{
            frmLoader.visibility = View.GONE
            Toast.makeText(this@PaymentOnlineActivity, com.example.qthien.t__t.R.string.fail_again, Toast.LENGTH_LONG).show()
        }
    }

    private lateinit var stripe: Stripe
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_payment_online)

        setTitle(com.example.qthien.t__t.R.string.info_card_payment)
        setSupportActionBar(toolbarPaymentOnline)
        toolbarPaymentOnline.setTitleTextColor(Color.WHITE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(com.example.qthien.t__t.R.drawable.ic_arrow_back_white_24dp)

        val total = intent.extras?.getLong("total")

        if(total == null) {
            Toast.makeText(this@PaymentOnlineActivity, com.example.qthien.t__t.R.string.fail_again, Toast.LENGTH_LONG).show()
            finish()
        }

        PaymentConfiguration.init("pk_test_lUkiTcaQjHdbXUdGbVY6itxg00SYow5ION")
        stripe = Stripe(
            this,
            PaymentConfiguration.getInstance().publishableKey
        )

        btnContinute.setOnClickListener({
            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.getWindowToken(), 0)
            val card = card_input_widget.getCard()
            if (card != null) {
                frmLoader.visibility = View.VISIBLE
                stripe.createToken(
                    card,
                    object : TokenCallback {
                        override fun onSuccess(token: Token) {
                            PrePaymentOnline(this@PaymentOnlineActivity).createCharge(token.id , total!!)
                        }
                        override fun onError(error: Exception) {
                            frmLoader.visibility = View.VISIBLE
                            Toast.makeText(this@PaymentOnlineActivity, error.message.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                )
            } else {
                Toast.makeText(this, com.example.qthien.t__t.R.string.not_enter_info_error_card, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}

