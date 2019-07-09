package com.example.qthien.t__t.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.model.DetailOrder
import kotlinx.android.synthetic.main.item_recy_product_order.view.*
import java.text.DecimalFormat


class OrderProductAdapter(var context : Context, var arrDetail : ArrayList<DetailOrder>)
    : RecyclerView.Adapter<OrderProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(com.example.qthien.t__t.R.layout.item_recy_product_order , p0 , false))

    override fun getItemCount(): Int = arrDetail.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val p = arrDetail.get(p1)
        val d = DecimalFormat("###,###,###")

        val name = "${p.nameProduct} (${p.size})"
        p0.txtName.setText(name)

        p0.txtQuantity.setText(p.quantity.toString() + "x")

        var price = p.quantity

        p0.txtPrice.setText(d.format(p.total)+ " đ")
        p0.txtPriceCurrent.setText(d.format(p.unitPrice)+ " đ")
        if(p.priceDiscount != 0L) {
            p0.txtPriceDiscount.visibility = View.VISIBLE
            p0.ibtnArrow.visibility = View.VISIBLE
            p0.txtPriceDiscount.setText(d.format(p.priceDiscount) + " đ")
            p0.txtPriceCurrent.setPaintFlags(p0.txtPriceCurrent.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
            price  *= p.priceDiscount.toInt()
        }
        else{
            p0.txtPriceDiscount.visibility = View.GONE
            p0.ibtnArrow.visibility = View.GONE
            price  *= p.unitPrice.toInt()
        }

        if(p.arrTopping.size == 0 && (p.note == null || p.note.equals(""))) {
            p0.txtToppingNote.visibility = View.GONE
        }else{
            p0.txtToppingNote.visibility = View.VISIBLE
            var toppingStr = ""
            for(t in p.arrTopping) {
                toppingStr +=  " , " + t.nameProduct
            }
            var text = ""
            if(toppingStr.equals("")){
                text = p.note ?: ""
            }else{
                toppingStr = toppingStr.replaceFirst(" , " , "")
                text = if(p.note == null || p.note.equals(""))
                            toppingStr
                        else
                            toppingStr + " , " + p.note

            }
            p0.txtToppingNote.setText(text)
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtQuantity = itemView.txtQuantity
        val txtName = itemView.txtName
        val txtToppingNote = itemView.txtToppingNote
        val txtPrice = itemView.txtPrice
        val ibtnArrow = itemView.imgArrow
        val txtPriceCurrent = itemView.txtPriceCurrent
        val txtPriceDiscount = itemView.txtPriceDiscount
    }
}