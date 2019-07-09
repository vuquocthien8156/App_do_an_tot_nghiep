package com.example.qthien.t__t.mvp.view.order

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.example.qthien.t__t.model.MainProductCart
import com.example.qthien.t__t.mvp.view.main.MainActivity.Companion.customer
import kotlinx.android.synthetic.main.dialog_enter_point.*
import kotlinx.android.synthetic.main.dialog_enter_point.view.*
import java.text.DecimalFormat


class EnterPointDialogFragment : DialogFragment() {

    interface DialogEditCartCommunication{
        fun update(cart : MainProductCart)
        fun remove(cart : MainProductCart)
    }

    lateinit var dialogEditCartCommunication : DialogEditCartCommunication

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is DialogEditCartCommunication)
            dialogEditCartCommunication = context
    }

    var priceTotal : Int? = 0
    var pointUsee = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        priceTotal = arguments?.getInt("totalPrice")
        pointUsee = arguments?.getInt("point") ?: 0
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return layoutInflater.inflate(com.example.qthien.t__t.R.layout.dialog_enter_point , container , false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.txtPriceCurrent.setText(DecimalFormat("###,###,###").format(priceTotal) + " đ")
        view.txtPointCurrent.setText(customer?.point.toString())
        view.edtQuantity.setText((pointUsee).toString())
        view.edtQuantity.setSelection(view.edtQuantity.text.length)

        val priceToPoint = priceTotal!!.div(1000)

        view.edtQuantity.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(!s.isNullOrEmpty()) {
                    if (s.toString().contains("-"))
                        s.toString().replace("-", "")
                    val point = edtQuantity.text.toString().toInt()

                    if (priceToPoint <= customer?.point!!) {
                        if (point > priceToPoint)
                            edtQuantity.setText(priceToPoint.toString())
                    }
                    else {
                        val pointAlow = priceToPoint / 2
                        if (pointAlow < customer?.point!!) {
                            if (pointAlow < point)
                                edtQuantity.setText(pointAlow.toString())
                        } else
                            if (point > customer?.point!!) {
                                edtQuantity.setText(customer?.point!!.toString())
                            }
                    }
                    view.edtQuantity.setSelection(view.edtQuantity.text.length)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        view.ibtnCloseee.setOnClickListener({
            dismiss()
        })

        view.btnEditOrRemove.setOnClickListener({
            if(!edtQuantity.text.isNullOrEmpty()) {
                val point = edtQuantity.text.toString().toInt()
                if (point > 0) {
                } else {
                }
                dismiss()
            }
        })
    }
}