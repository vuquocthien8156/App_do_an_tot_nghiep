package com.example.qthien.t__t.view.cart

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.MainProductCart
import kotlinx.android.synthetic.main.dialog_edit_cart.view.*

class DialogEditProductCart : DialogFragment() {

    lateinit var cart : MainProductCart

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        cart = arguments?.getParcelable("cart") as MainProductCart
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return layoutInflater.inflate(R.layout.dialog_edit_cart , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.txtNameProductEdit.setText(cart.nameProduct)
        view.edtNote.setText(cart.note)
        view.edtNote.setSelection(view.edtNote.getText().length);
        view.txtQuantity.setText(cart.quantity.toString())

        view.btnPlus.setOnClickListener({
            cart.quantity = cart.quantity.plus(1)
            view.txtQuantity.setText(cart.quantity.toString())
            view.btnMinus.isEnabled = true
            view.btnEditOrRemove.setBackgroundResource(R.drawable.shape_btn_cart_update)
            view.btnEditOrRemove.setText(R.string.update_cart)
            cart.priceProduct = cart.priceProduct.times(cart.quantity)
        })

        view.btnMinus.setOnClickListener({
            if(cart.quantity > 0) {
                cart.quantity = cart.quantity.minus(1)
                view.txtQuantity.setText(cart.quantity.toString())
            }

            if(cart.quantity == 0){
                view.btnMinus.isEnabled = false
                view.btnEditOrRemove.setBackgroundResource(R.drawable.shape_btn_cart_remove)
                view.btnEditOrRemove.setText(R.string.remove_cart)
            }

            cart.priceProduct = cart.priceProduct.times(cart.quantity)
        })

        view.ibtnClose.setOnClickListener({
            dismiss()
        })

        view.btnEditOrRemove.setOnClickListener({
            if(cart.quantity.equals(0)){
                dialogEditCartCommunication.remove(cart)
            }
            else
                dialogEditCartCommunication.update(cart)
            dismiss()
        })
    }
}