package com.example.qthien.t__t.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.qthien.t__t.GlideApp
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.mvp.presenter.pre_product_favorite.PreProductFavoriteActi
import com.example.qthien.t__t.mvp.view.cart.AddToCartActivity
import com.example.qthien.t__t.mvp.view.main.MainActivity
import com.example.qthien.t__t.mvp.view.product_favorite.IViewProductFavoriteActi
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import kotlinx.android.synthetic.main.item_recy_product_favorite.view.*
import java.text.DecimalFormat


class ProductFavoriteAdapter(var context : Context,
                             var mComparator: Comparator<Product>? = null):
    RecyclerView.Adapter<ProductFavoriteAdapter.ViewHolder>() , IViewProductFavoriteActi {

    interface CommunicateActivity{
        fun removeLastPosition()
    }

    var communicateActivity : CommunicateActivity? = null
    init {
        if(context is CommunicateActivity)
            communicateActivity = context as CommunicateActivity
    }

    var positionClick = -1

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
        ViewHolder(LayoutInflater.from(context).inflate(com.example.qthien.t__t.R.layout.item_recy_product_favorite , p0 , false))

    override fun getItemCount(): Int = mSortedList.size()

    @SuppressLint("SetTextI18n")
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
        vh.txtPrice.setText(DecimalFormat("#,###,###").format(price) + "đ")


        if(positionClick == vh.adapterPosition){
            vh.relaItem.setBackgroundColor(Color.LTGRAY)
            vh.ibtnAddCart.isEnabled = false
            vh.ibtnUnFavorite.isEnabled = false
        }else{
            vh.relaItem.setBackgroundColor(Color.WHITE)
            vh.ibtnAddCart.isEnabled = true
            vh.ibtnUnFavorite.isEnabled = true
        }

        vh.ibtnUnFavorite.setOnClickListener({
            positionClick = vh.adapterPosition
            vh.relaItem.setBackgroundColor(Color.LTGRAY)
            vh.ibtnAddCart.isEnabled = false
            vh.ibtnUnFavorite.isEnabled = false

            PreProductFavoriteActi(this).favoriteProduct(p.idProduct , MainActivity.customer?.idCustomer!! , 0)
        })


        vh.ibtnAddCart.setOnClickListener({
            val i = Intent(context , AddToCartActivity::class.java)
            i.putExtra("product" , p)
            context.startActivity(i)
        })
    }

    override fun resultGetProductFavorite(arrResult: ArrayList<Product>?) {}

    override fun favoriteProduct(resultCode: String) {
        Log.d("removeeeee", "true1")
        mSortedList.removeItemAt(positionClick)
        positionClick = -1
        if(mSortedList.size() == 0){
            communicateActivity?.removeLastPosition()
        }
    }

    override fun failureFavorite(message: String) {
        Toast.makeText(context , message , Toast.LENGTH_LONG).show()
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val img = itemView.imgProductFavorite
        val txtName = itemView.txtNameProductFavorite
        val txtPrice = itemView.txtPriceProductFavorite
        val ibtnUnFavorite = itemView.ibtnUnFavorite
        val ibtnAddCart = itemView.ibtnAddToCart
        val relaItem = itemView.relaItem
    }

    fun add(model: Product) {
        mSortedList.add(model)
    }

    fun remove(model: Product) {
        mSortedList.remove(model)
    }

    fun clear() {
        mSortedList.clear()
        notifyDataSetChanged()
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