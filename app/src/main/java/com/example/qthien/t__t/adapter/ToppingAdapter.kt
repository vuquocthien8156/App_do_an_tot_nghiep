package com.example.qthien.t__t.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.GlideApp
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import kotlinx.android.synthetic.main.item_recy_topping.view.*
import java.text.DecimalFormat

class ToppingAdapter(var context : Context,
                     var arrTopping : ArrayList<Product> ) : RecyclerView.Adapter<ToppingAdapter.ViewHolder>() {

    interface SelectedTopping{
        fun selectedTopping(position : Int)
    }

    var selectedTopping : SelectedTopping? = null
    init {
        if(context is SelectedTopping)
            selectedTopping = context as SelectedTopping
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recy_topping , p0 , false))

    override fun getItemCount(): Int = arrTopping.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(vh : ViewHolder, p1: Int) {

        vh.name.setText(arrTopping[p1].nameProduct)

        val priceFormat = DecimalFormat("###,###,###").format(arrTopping[p1].priceProduct)
        vh.price.setText(priceFormat.toString() + " đ")
        GlideApp.with(context)
            .load("${RetrofitInstance.baseUrl}/${arrTopping[p1].imageProduct}")
            .placeholder(R.drawable.img_placeholder)
            .into(vh.img)

        vh.layout.setOnClickListener({
            if(selectedTopping != null){
                Log.d("SelecteddTopping" , p1.toString())
                selectedTopping!!.selectedTopping(p1)
            }
        })
    }

    // effect for expand
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val img = itemView.imgTopping
        val name = itemView.txtNameTopping
        val price = itemView.txtPriceToping
        val layout = itemView.relaClickTopping
    }
}