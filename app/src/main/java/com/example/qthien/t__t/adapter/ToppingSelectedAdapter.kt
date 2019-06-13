package com.example.qthien.t__t.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.ToppingProductCart
import kotlinx.android.synthetic.main.item_topping_selected.view.*
import java.text.DecimalFormat


class ToppingSelectedAdapter(var context: Context
                  ,var arrTopping: ArrayList<ToppingProductCart>
) : RecyclerView.Adapter<ToppingSelectedAdapter.ViewHolder>() {

    interface CommunicationActiAdd{
        fun removerTopping(position: Int)
    }

    var remove = false

    var communicationActiAdd : CommunicationActiAdd? = null

    init {
        if(context is CommunicationActiAdd){
            communicationActiAdd = context as CommunicationActiAdd
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder
            = ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_topping_selected , p0 , false))

    override fun getItemCount(): Int = arrTopping.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val t = arrTopping.get(position)

        if(communicationActiAdd == null)
            holder.ibtn.visibility = View.GONE
        else {
            holder.ibtn.visibility = View.VISIBLE
            if(!remove)
                setAnimation( holder.itemView ,position)
        }

        holder.txtName.setText(t.nameProduct)
        holder.txtPrice.setText(DecimalFormat("###,###,###").format(t.priceProduct)+" đ")
        holder.txtQuantity.setText(t.quantity.toString())

        holder.ibtn.setOnClickListener({
            communicationActiAdd?.removerTopping(position)
            remove = true
        })
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position == 0) {
        val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
        viewToAnimate.startAnimation(animation)
//            lastPosition = position
        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtName = itemView.txtNameTopping
        val txtQuantity = itemView.txtQuantityTopping
        val txtPrice = itemView.txtPriceTopping
        val ibtn = itemView.btnDeleteToping
    }
}