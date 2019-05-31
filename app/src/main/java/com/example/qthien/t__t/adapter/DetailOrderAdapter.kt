package com.example.qthien.t__t.adapter

import android.animation.ObjectAnimator
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.DetailOrder
import com.example.qthien.t__t.model.ToppingProductCart
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter
import com.github.aakira.expandablelayout.Utils
import kotlinx.android.synthetic.main.item_recy_detail_order_root.view.*
import java.text.DecimalFormat

class DetailOrderAdapter(var context : Context, var arrDetail : ArrayList<DetailOrder>)
    : RecyclerView.Adapter<DetailOrderAdapter.ViewHolder>(){

    var arrBoolean : SparseBooleanArray

    init {
        arrBoolean = SparseBooleanArray()
        for (i in 0 until arrDetail.size)
            arrBoolean.append(i , false)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder=
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recy_detail_order_root , p0 ,false))

    override fun getItemCount(): Int = arrDetail.size

    override fun onBindViewHolder(holder: ViewHolder, position : Int) {
        val arr = arrDetail[position]

        holder.txtQuantity.text = arr.quantity.toString()
        holder.txtNameProduct.text = arr.nameProduct
        holder.txtPrice.text = DecimalFormat("###,###,###").format(arr.total) + " đ"

        holder.setIsRecyclable(false)

        holder.expandableLinearLayout.setInRecyclerView(false)

        //set effect and expand collap
        holder.expandableLinearLayout.setListener(object : ExpandableLayoutListenerAdapter() {

            override fun onPreOpen() {
                changeRotate(holder.ibtn, 180f, 0f).start()
                arrBoolean.put(position, true)
            }

            override fun onPreClose() {
                //                holder.expandableLinearLayout.initLayout();

                changeRotate(holder.ibtn, 0f, 180f).start()
                arrBoolean.put(position , false)
            }

        })
        // kich hoat btn
        holder.ibtn.setRotation(if (arrBoolean.get(position)) 0f else 180f)
        holder.relaRoot.setOnClickListener(View.OnClickListener {
            // toggle vua dong vua mo?
            holder.expandableLinearLayout.toggle()
            holder.expandableLinearLayout.initLayout()
        })

        //ibtn không nằm trong layout thỳ quay lên
        if (!holder.ibtn.isInLayout()) {
            arrBoolean.put(position, false)
            holder.ibtn.setRotation(if (arrBoolean.get(position)) 0f else 180f)
        } else
            holder.expandableLinearLayout.initLayout()


        if(arr.arrTopping != null){
            holder.ibtn.visibility = View.VISIBLE

            val arrTopping = ArrayList<ToppingProductCart>()
            for(t in arr.arrTopping!!){
                arrTopping.add(ToppingProductCart( t.idProduct , t.nameProduct , t.unitPrice , t.quantity))
            }

            holder.recyToping.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)
            holder.recyToping.adapter = ToppingSelectedAdapter(context , arrTopping)
        }
        else
            holder.ibtn.visibility = View.GONE

    }

    private fun changeRotate(ibtn: ImageButton, from: Float, to: Float): ObjectAnimator {
        val animator = ObjectAnimator.ofFloat(ibtn, "rotation", from, to)
        animator.duration = 300
        animator.interpolator = Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR)
        return animator
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val recyToping = itemView.Detail
        val ibtn = itemView.ibtnExpand
        val expandableLinearLayout = itemView.expandLinear
        val relaRoot = itemView.relaRoot
        val txtQuantity = itemView.txtQuantityProductOrder
        val txtNameProduct = itemView.txtNameProductOrder
        val txtPrice = itemView.txtPriceProductOrder
    }
}