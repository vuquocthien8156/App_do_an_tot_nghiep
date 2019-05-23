package com.example.qthien.t__t.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import com.example.qthien.t__t.view.cart.AddToCartActivity
import com.example.qthien.t__t.view.detail_product.DetailProductActivity
import com.example.qthien.week3_ryder.GlideApp
import kotlinx.android.synthetic.main.item_product.view.*
import java.text.DecimalFormat

class ProductsAdapter(internal var context: Context,
                      internal var arrProducts: ArrayList<Product>
) :
                            RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder
        = ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product , p0 , false))

    override fun getItemCount(): Int = arrProducts.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(vh: ViewHolder, p1: Int) {
        val p = arrProducts.get(p1)
        GlideApp.with(context)
            .load("${RetrofitInstance.baseUrl}/${p.imageProduct}")
            .placeholder(R.drawable.img_placeholder)
            .into(vh.imgProducts)
        vh.txtName.setText(p.nameProduct)
        var price : Long = 0
        when{
            p.priceProduct != 0.toLong() -> {
                price = p.priceProduct
                val name = vh.txtName.text.toString() + " (S)"
                vh.txtName.setText(name)
            }
            p.priceMProduct != 0.toLong() -> {
                price = p.priceMProduct
                val name = vh.txtName.text.toString() + " (M)"
                vh.txtName.setText(name)
            }
            p.priceLProduct != 0.toLong() ->{
                price = p.priceLProduct
                val name = vh.txtName.text.toString() + " (L)"
                vh.txtName.setText(name)
            }
        }

        val textFormat = DecimalFormat("###,###,###").format(price)
        vh.txtPrice.setText("$textFormat đ")

        vh.layoutContain.setOnClickListener {
            context.startActivity(Intent(context , DetailProductActivity::class.java))
        }

        vh.imgAdd.setOnClickListener {
            val i = Intent(context , AddToCartActivity::class.java)
            i.putExtra("product" , p)
            context.startActivity(i)
        }
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var imgProducts = itemView.imgProduct
        var imgAdd = itemView.imgAdd
        var layoutContain = itemView.relaContainProduct
        var txtName = itemView.txtNameProduct
        var txtPrice = itemView.txtPriceProduct
    }
}