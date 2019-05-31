package com.example.qthien.t__t.view.cart

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.ToppingAdapter
import com.example.qthien.t__t.adapter.ToppingSelectedAdapter
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.model.ToppingProductCart
import com.example.qthien.t__t.presenter.pre_cart.PreSheetTopping
import kotlinx.android.synthetic.main.dialog_sheet_topping.*

class DialogSheetTopping : BottomSheetDialogFragment() ,
    ToppingAdapter.SelectedTopping,
    ToppingSelectedAdapter.CommunicationActiAdd,
    IDialogSheetTopping{

    interface CompleteTopping{
        fun completeEditTopping(arrToppingEdit : ArrayList<ToppingProductCart>)
    }

    private var completeTopping : CompleteTopping? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is CompleteTopping)
            completeTopping = context
    }

    override fun failureTopping(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun getTopping(arrTopping: ArrayList<Product>?) {
        Log.d("arrTopping", arrTopping.toString())
        if (arrTopping != null) {
            this.arrToping.addAll(arrTopping)
            adapterMain.notifyDataSetChanged()
            progress_load_topping.visibility = View.GONE
            if(context!!.getSharedPreferences("lead_add_topping" , MODE_PRIVATE).getInt("lead" , 0) == 0){
                showAlertLead()
            }
        }
    }

    override fun removerTopping(position: Int) {
        arrTopingSelected.get(position).quantity -= 1

        if(arrTopingSelected.get(position).quantity == 0)
            arrTopingSelected.removeAt(position)

        if (arrTopingSelected.size == 0)
            txtParent.setText(com.example.qthien.t__t.R.string.not_topping_selected)
        adapterOrder.notifyDataSetChanged()

        checkChange()
    }

    override fun selectedTopping(position: Int) {

        Log.d("quantityyyy", arrTopingSelected.toString())
        Log.d("quantityyyy", arrTopingSelected.map { it.quantity }.sum().toString())
        if (arrTopingSelected.sumBy { it.quantity } <= 2){
            val tpSelected = arrToping[position]
            val p = arrTopingSelected.find { it.idProduct == tpSelected.idProduct }

            val product = ToppingProductCart(
                tpSelected.idProduct, tpSelected.nameProduct, tpSelected.priceProduct, 1
            )
            if (p != null) {
                product.quantity += p.quantity
                arrTopingSelected.remove(p)
            }

            if (arrTopingSelected.size == 0)
                arrTopingSelected.add(product)
            else {
                arrTopingSelected.add(0, product)
            }
        }else
            showArleTopping()

        Log.d("SelecteddTopping", arrTopingSelected.size.toString())

        adapterOrder.notifyDataSetChanged()
        txtParent.setText(com.example.qthien.t__t.R.string.view_topping_selected)

        checkChange()
    }

    fun checkChange(){
        var i = 0
        for(topping in arrTopingSelected){
            val t = arrTopingSelectedOld.find { it.equals(topping) }
            if(t != null)
                i += 1
        }

        if(i == arrTopingSelected.size){
            btnComplete.setText(R.string.back)
            btnComplete.setBackgroundResource(R.drawable.shape_btn_cart_remove)
        }
        else{
            btnComplete.setText(R.string.update)
            btnComplete.setBackgroundResource(R.drawable.shape_btn_cart_update)
        }
    }

    private lateinit var adapterMain: ToppingAdapter
    private lateinit var adapterOrder: ToppingSelectedAdapter

    private lateinit var arrTopingSelected: ArrayList<ToppingProductCart>
    private lateinit var arrToping: ArrayList<Product>
    private lateinit var arrTopingSelectedOld : ArrayList<ToppingProductCart>

    private var preSheetTopping = PreSheetTopping(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.dialog_sheet_topping , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrTopingSelected = ArrayList()
        arrToping = ArrayList()
        arrTopingSelectedOld = ArrayList()

        val arr = arguments?.getParcelableArrayList<ToppingProductCart>("topping")
        if(arr != null) {
            arrTopingSelectedOld.addAll(arr)
            arrTopingSelected.addAll(arr)
        }

        adapterMain = ToppingAdapter(context!! , arrToping)
        adapterOrder = ToppingSelectedAdapter(context!! , arrTopingSelected)

        adapterMain.selectedTopping = this
        adapterOrder.communicationActiAdd = this

        recyToping.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyToping.adapter = adapterMain

        recy_topping_selected.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recy_topping_selected.adapter = adapterOrder

        preSheetTopping.getAllProductTopping()

        btnComplete.setOnClickListener({
            if(btnComplete.text.equals(context!!.getString(R.string.back))){
                dismiss()
            }
            else{
                if(completeTopping != null)
                    completeTopping?.completeEditTopping(arrTopingSelected)
                dismiss()
            }
        })

    }

    fun showAlertLead(){
        val alert = AlertDialog.Builder(context!!)

        alert.setTitle(R.string.can_not_know)
        alert.setIcon(R.drawable.ic_mainn)
        alert.setMessage(R.string.lead_add_topping)
        alert.setPositiveButton(R.string.know , ({ dialogInterface: DialogInterface, i: Int ->
            onCancel(dialogInterface)
        }))
        alert.setNegativeButton(R.string.not_show_again , ({ dialogInterface: DialogInterface, i: Int ->
            context!!.getSharedPreferences("lead_add_topping" , MODE_PRIVATE)
                .edit().putInt("lead" , 1).apply()
            onCancel(dialogInterface)
        }))
        alert.show()
    }

    fun showArleTopping(){
        val alert = AlertDialog.Builder(context!!)
        alert.setTitle(R.string.noti)
        alert.setIcon(R.drawable.ic_main)
        alert.setMessage(R.string.error_add_topping)
        alert.setPositiveButton(R.string.know , ({ dialogInterface: DialogInterface, i: Int ->
            onCancel(dialogInterface)
        }))
        alert.show()
    }
}