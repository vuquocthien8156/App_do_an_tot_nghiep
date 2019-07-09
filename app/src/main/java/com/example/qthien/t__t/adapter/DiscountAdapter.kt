package com.example.qthien.t__t.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.Discount
import com.example.qthien.t__t.mvp.view.discount.DiscountActivity
import kotlinx.android.synthetic.main.item_recy_discount.view.*
import java.text.SimpleDateFormat
import java.util.*






class DiscountAdapter(var context : Context, var arrDiscount : ArrayList<Discount>)
    : RecyclerView.Adapter<DiscountAdapter.ViewHolder>(){

    interface DiscountAdapterCommunicate{
        fun startActivityDetail(discount: Discount)
    }

    var discountAdapterCommunicate : DiscountAdapterCommunicate? = null

    init {
        if(context is DiscountAdapterCommunicate)
            discountAdapterCommunicate = context as DiscountAdapterCommunicate
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(context).inflate(com.example.qthien.t__t.R.layout.item_recy_discount , p0 , false))
    override fun getItemCount(): Int = arrDiscount.size

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val discount = arrDiscount.get(p1)
        p0.txtNameDiscount.setText(discount.nameDiscount)

        val calender = Calendar.getInstance()
        val dateNow = calender.time
        val dateEnd = SimpleDateFormat("yyyy-MM-dd").parse(discount.dateEnd)
        p0.txtxDate.setText(SimpleDateFormat("yyyy-MM-dd").format(dateEnd))

        val noOfDaysBetween = dateEnd.time - dateNow.time

        val seconds = noOfDaysBetween / 1000
        val minutes = seconds / 60
        var hours = minutes / 60
        if(minutes < 60)
            hours += 1

        val days = (hours / 24)

        Log.d("dayyyyyy" , days.toString())
        Log.d("hours" , hours.toString())
        if(hours > 24) {
            if (days > 0L) {
                p0.txtTimer.setText("$days ${context.getString(com.example.qthien.t__t.R.string.day_left)}")
                p0.txtTimer.setBackgroundResource(com.example.qthien.t__t.R.color.colorAccent)
            }
        }
        else{
            if(hours > 0){
                p0.txtTimer.setText("${hours} ${context.getString(com.example.qthien.t__t.R.string.house_left)}")
                p0.txtTimer.setBackgroundResource(com.example.qthien.t__t.R.color.colorAccent)
                p0.txtxDate.setBackgroundResource(com.example.qthien.t__t.R.color.colorAccent)
                p0.txtTimer.setTag(R.color.colorAccent)
            }else {
                p0.txtTimer.setText(context.getString(com.example.qthien.t__t.R.string.dealine))
                p0.txtTimer.setBackgroundResource(com.example.qthien.t__t.R.color.colorPrimary)
                p0.txtxDate.setBackgroundResource(com.example.qthien.t__t.R.color.colorPrimary)
                p0.txtTimer.setTag(R.color.colorPrimary)
            }
        }

        if(discount.limitCode > 0) {
            if(discount.codeLeft > 0)
                p0.txtLimit.setText(com.example.qthien.t__t.R.string.has_limit)
            else {
                p0.txtLimit.setText(com.example.qthien.t__t.R.string.use_full_limit)
                p0.txtTimer.setText(context.getString(com.example.qthien.t__t.R.string.dealine))
                p0.txtTimer.setBackgroundResource(com.example.qthien.t__t.R.color.colorPrimary)
                p0.txtxDate.setBackgroundResource(com.example.qthien.t__t.R.color.colorPrimary)
                p0.txtTimer.setTag(R.color.colorPrimary)
            }
        }else
            p0.txtLimit.setText(com.example.qthien.t__t.R.string.not_limit)

        p0.relaLayout.setOnClickListener({
            if(p0.txtTimer.tag != R.color.colorPrimary) {
                val i = Intent()
                i.putExtra("discount", discount)
                (context as DiscountActivity).setResult(Activity.RESULT_OK, i)
                (context as DiscountActivity).finish()
            }
        })

        p0.txtReadMore.setOnClickListener({
            discountAdapterCommunicate?.startActivityDetail(discount)
        })
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtNameDiscount = itemView.txtNameDiscount
        val txtLimit = itemView.txtLimit
        val txtTimer = itemView.txtTime
        val txtxDate = itemView.txtDate
        val txtReadMore = itemView.txtReadMore
        val relaLayout = itemView.relaLayout
    }
}