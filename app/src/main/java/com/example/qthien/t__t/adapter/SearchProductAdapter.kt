package com.example.qthien.t__t.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.qthien.t__t.GlideApp
import com.example.qthien.t__t.R
import com.example.qthien.t__t.model.Discount
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.mvp.presenter.pre_product_favorite.PreProductFavoriteActi
import com.example.qthien.t__t.mvp.view.cart.AddToCartActivity
import com.example.qthien.t__t.mvp.view.detail_product.DetailProductActivity
import com.example.qthien.t__t.mvp.view.main.MainActivity
import com.example.qthien.t__t.mvp.view.product_favorite.IViewProductFavoriteActi
import com.example.qthien.t__t.retrofit2.RetrofitInstance
import kotlinx.android.synthetic.main.item_recycler_search_product.view.*
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class SearchProductAdapter(var context : Context,
                           var mComparator: Comparator<Product>? = null,
                           var discount : Discount?):
    RecyclerView.Adapter<SearchProductAdapter.ViewHolder>() , IViewProductFavoriteActi {

    override fun resultGetProductFavorite(arrResult: ArrayList<Product>?) {}

    override fun favoriteProduct(resultCode: String) {
        addOrRemoveFavorite()
    }

    override fun failureFavorite(message: String) {
        Toast.makeText(context , message , Toast.LENGTH_LONG).show()
    }

    var arrFavorite = ArrayList<String>()
    var position = -1
    var idProduct = -1
    var favo = 0
    init {
        val stringFavorite = context.getSharedPreferences("Favorite", Context.MODE_PRIVATE).getString("arrFavorite", null)
        if(stringFavorite != null)
            arrFavorite.addAll(stringFavorite.replace("[", "").replace("]", "").split(","))
    }

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(vh: ViewHolder, p1: Int) {
        val p = mSortedList.get(p1)


        if(arrFavorite.find { it.trim().equals(p.idProduct.toString()) } != null) {
            vh.ibtnFavorite.setImageResource(R.drawable.ic_unlike)
            vh.ibtnFavorite.setTag(R.drawable.ic_unlike)
            Log.d("favoooooo" , "No")
        }else {
            vh.ibtnFavorite.setImageResource(R.drawable.ic_favorite_red_36dp)
            vh.ibtnFavorite.setTag(R.drawable.ic_favorite_red_36dp)
            Log.d("favoooooo" , "Yes")
        }

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
        vh.txtPrice.setText(DecimalFormat("#,###,###").format(price) + " đ")

        vh.ibtnAddCart.setOnClickListener({
            val i = Intent(context , AddToCartActivity::class.java)
            i.putExtra("product" , p)
            context.startActivity(i)
        })

        vh.ibtnFavorite.setOnClickListener({
            favo = 0
            if(it.getTag() == R.drawable.ic_favorite_red_36dp){
                favo = 1
            }
            this.position = p1
            idProduct = p.idProduct
            PreProductFavoriteActi(this).favoriteProduct(p.idProduct ,
                    MainActivity.customer!!.idCustomer , favo)
        })

        vh.layout.setOnClickListener({
            val i = Intent(context , DetailProductActivity::class.java)
            i.putExtra("product" , p)
            i.putExtra("discount" , discount)
            context.startActivity(i)
        })
    }

    fun addOrRemoveFavorite(){

        Log.d("favoooooo" , arrFavorite.toString())
        Log.d("favoooooo" , favo.toString())

        if(favo == 1){
            arrFavorite.add(idProduct.toString().trim())
        }
        else{
            arrFavorite.remove(idProduct.toString().trim())
        }
        context.getSharedPreferences("Favorite" , Context.MODE_PRIVATE).edit()
                .putString( "arrFavorite" , arrFavorite.toString()).apply()
        Log.d("favoooooo" , arrFavorite.toString())
        notifyItemChanged(position)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val img : ImageView = itemView.imgProductSearch
        val txtName : TextView = itemView.txtNameProductSearch
        val txtPrice : TextView = itemView.txtPriceProductSearch
        val ibtnAddCart : ImageButton = itemView.ibtnAddToCart
        val ibtnFavorite : ImageButton = itemView.ibtnFavorite
        val layout = itemView.layout
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