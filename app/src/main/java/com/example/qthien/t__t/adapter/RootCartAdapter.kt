package com.example.qthien.t__t.adapter

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.qthien.t__t.GlideApp
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.MainProductCart
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter
import com.github.aakira.expandablelayout.Utils
import kotlinx.android.synthetic.main.item_recy_cart_root.view.*
import java.text.DecimalFormat

class RootCartAdapter(var context : Context, var arrCart : ArrayList<MainProductCart>)
    : RecyclerView.Adapter<RootCartAdapter.ViewHolder>(){

    var arrBoolean : SparseBooleanArray

    var adapterCartCallActivity : AdapterCartCallActivity? = null

    interface AdapterCartCallActivity{
        fun createEditCart(position : Int)
    }

    init {
        arrBoolean = SparseBooleanArray()
        for (i in 0 until arrCart.size)
            arrBoolean.append(i , false)

        if(context is AdapterCartCallActivity)
            adapterCartCallActivity = context as AdapterCartCallActivity
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recy_cart_root , p0 , false))

    override fun getItemCount(): Int = arrCart.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        val c = arrCart.get(p1)

        GlideApp.with(context)
            .load("${RetrofitInstance.baseUrl}/${c.imageUrl}")
            .into(holder.imgProduct)

        holder.txtName.setText("${c.nameProduct} (${c.size})")
        val price = when(c.size){
            "S" -> c.priceProduct
            "M" -> c.priceMProduct
            else -> c.priceLProduct
        }

        holder.txtPrice.setText(DecimalFormat("###,###,###").format(price)+" đ")
        holder.txtQuantity.setText(c.quantity.toString())

        holder.setIsRecyclable(false)

        holder.expandableLinearLayout.setInRecyclerView(false)

        //set effect and expand collap
        holder.expandableLinearLayout.setListener(object : ExpandableLayoutListenerAdapter() {

            override fun onPreOpen() {
                changeRotate(holder.ibtn, 180f, 0f).start()
                arrBoolean.put(p1, true)
            }

            override fun onPreClose() {
                //                holder.expandableLinearLayout.initLayout();

                changeRotate(holder.ibtn, 0f, 180f).start()
                arrBoolean.put(p1, false)
            }

        })
        // kich hoat btn
        holder.ibtn.setRotation(if (arrBoolean.get(p1)) 0f else 180f)
        holder.ibtn.setOnClickListener(View.OnClickListener {
            // toggle vua dong vua mo?
            holder.expandableLinearLayout.toggle()
            holder.expandableLinearLayout.initLayout()
        })

        //ibtn không nằm trong layout thỳ quay lên
        if (!holder.ibtn.isInLayout()) {
            arrBoolean.put(p1, false)
            holder.ibtn.setRotation(if (arrBoolean.get(p1)) 0f else 180f)
        } else
            holder.expandableLinearLayout.initLayout()

        holder.recyToping.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)

        if(c.arrTopping.size > 0) {
            holder.ibtn.visibility = View.VISIBLE
        } else
            holder.ibtn.visibility = View.GONE

        holder.recyToping.adapter = ToppingSelectedAdapter(context , c.arrTopping)

        holder.editCart.setOnClickListener({
            if(adapterCartCallActivity != null)
                adapterCartCallActivity?.createEditCart(p1)
        })
    }

    private fun changeRotate(ibtn: ImageButton, from: Float, to: Float): ObjectAnimator {
        val animator = ObjectAnimator.ofFloat(ibtn, "rotation", from, to)
        animator.duration = 300
        animator.interpolator = Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR)
        return animator
    }

    class ViewHolder(itemView : View) :  RecyclerView.ViewHolder(itemView){
        val recyToping = itemView.Detail
        val ibtn = itemView.ibtnExpand
        val expandableLinearLayout = itemView.expandLinear
        val imgProduct = itemView.imgProductCart
        val editCart = itemView.editCart
        val txtName = itemView.txtNameProductCart
        val txtPrice = itemView.txtPriceProductCart
        val txtQuantity = itemView.txtQuantityProductCart
    }
}