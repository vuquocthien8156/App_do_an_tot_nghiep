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
import com.example.qthien.t__t.model.ToppingProductCart
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter
import com.github.aakira.expandablelayout.Utils
import kotlinx.android.synthetic.main.item_recy_detail_order_root.view.*

class DetailOrderAdapter(var context : Context, var arrDetail : ArrayList<String>)
    : RecyclerView.Adapter<DetailOrderAdapter.ViewHolder>(){

    var arrBoolean : SparseBooleanArray

    init {
        arrBoolean = SparseBooleanArray()
        for (i in 0 until arrDetail.size)
            arrBoolean.append(i , false)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder=
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recy_detail_order_root , p0 ,false))

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
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
        holder.relaRoot.setOnClickListener(View.OnClickListener {
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


        val arrTopping = ArrayList<ToppingProductCart>()
        arrTopping.add(ToppingProductCart(1 , 1 , "Trân châu đen" , 10000 , 1))
        arrTopping.add(ToppingProductCart(2 , 2 , "Trân châu trắng" , 10000 , 1))
        arrTopping.add(ToppingProductCart(3 , 3 , "Thạch trái cây" , 6000 , 1))

        holder.recyToping.layoutManager = LinearLayoutManager(context , LinearLayoutManager.VERTICAL , false)
        holder.recyToping.adapter = ToppingSelectedAdapter(context , arrTopping)
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
    }
}