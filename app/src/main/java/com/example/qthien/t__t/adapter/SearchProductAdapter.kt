package com.example.qthien.t__t.adapter

import android.content.Context
import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.qthien.t__t.GlideApp
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import kotlinx.android.synthetic.main.item_recycler_search_product.view.*
import java.text.DecimalFormat

class SearchProductAdapter(var context : Context,
                           var mComparator: Comparator<Product>? = null):
    RecyclerView.Adapter<SearchProductAdapter.ViewHolder>() {

    private val mSortedList =
        SortedList<Product>(Product::class.java, object : SortedList.Callback<Product>() {
            override fun compare(a: Product, b: Product): Int {
                return mComparator!!.compare(a, b)
            }

            override fun onInserted(position: Int, count: Int) {
                notifyItemRangeInserted(position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                notifyItemRangeRemoved(position, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }

            override fun onChanged(position: Int, count: Int) {
                notifyItemRangeChanged(position, count)
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.equals(newItem)
            }

            override fun areItemsTheSame(item1: Product, item2: Product): Boolean {
                return item1 === item2
            }
        })

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_recycler_search_product,
                p0,
                false
            )
        )


    override fun getItemCount(): Int = mSortedList.size()

    override fun onBindViewHolder(vh: ViewHolder, p1: Int) {
        val p = mSortedList.get(p1)
        GlideApp.with(context)
            .load("${RetrofitInstance.baseUrl}/${p.imageProduct}")
            .into(vh.img)
        vh.txtName.setText(p.nameProduct)
        var price : Long = 0
        when{
            p.priceProduct != 0.toLong() -> {
                price = p.priceProduct
            }
            p.priceMProduct != 0.toLong() -> {
                price = p.priceMProduct
            }
            p.priceLProduct != 0.toLong() ->{
                price = p.priceLProduct
            }
        }
        vh.txtPrice.setText(DecimalFormat("#.###").format(price))

        vh.ibtnAddCart.setOnClickListener({

        })

        vh.ibtnFavorite.setOnClickListener({

        })
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val img : ImageView = itemView.imgProductSearch
        val txtName : TextView = itemView.txtNameProductSearch
        val txtPrice : TextView = itemView.txtPriceProductSearch
        val ibtnAddCart : ImageButton = itemView.ibtnAddToCart
        val ibtnFavorite : ImageButton = itemView.ibtnFavorite
    }

    fun add(model: Product) {
        mSortedList.add(model)
    }

    fun remove(model: Product) {
        mSortedList.remove(model)
    }

    fun add(models: List<Product>) {
        mSortedList.addAll(models)
    }

    fun remove(models: List<Product>) {
        mSortedList.beginBatchedUpdates()
        for (model in models) {
            mSortedList.remove(model)
        }
        mSortedList.endBatchedUpdates()
    }

    fun replaceAll(models: List<Product>) {
        mSortedList.beginBatchedUpdates()
        for (i in mSortedList.size() - 1 downTo 0) {
            val model = mSortedList.get(i)
            if (!models.contains(model)) {
                mSortedList.remove(model)
            }
        }
        mSortedList.addAll(models)
        mSortedList.endBatchedUpdates()
    }
}