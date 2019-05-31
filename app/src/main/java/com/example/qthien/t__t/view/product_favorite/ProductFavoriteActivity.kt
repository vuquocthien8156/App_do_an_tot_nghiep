package com.example.qthien.t__t.view.product_favorite

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.qthien.t__t.adapter.ProductFavoriteAdapter
import com.example.qthien.t__t.model.Product
import com.example.qthien.t__t.presenter.pre_product_favorite.PreProductFavoriteActi
import com.example.qthien.t__t.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_product_favorite.*
import java.text.Normalizer
import java.util.regex.Pattern


class ProductFavoriteActivity : AppCompatActivity() , IViewProductFavoriteActi,
        ProductFavoriteAdapter.CommunicateActivity {

    override fun change() {
        change = true
    }

    override fun removeLastPosition() {
        relaFavorite.visibility = View.VISIBLE
        recyProductsFavorite.visibility = View.GONE
        itemMenu?.setVisible(false)
    }

    override fun favoriteProduct(resultCode : String) {}

    override fun resultGetProductFavorite(arrResult: ArrayList<Product>?) {
        Log.d("swipeRefreshLayout" , arrResult?.size.toString())
        if(arrResult != null && arrResult.size > 0){
            arrProducts.clear()
            arrProducts.addAll(arrResult)
            adapter.add(arrResult)
            relaFavorite.visibility = View.GONE
            recyProductsFavorite.visibility = View.VISIBLE
            itemMenu?.setVisible(true)
        }
        else {
            itemMenu?.setVisible(false)
            relaFavorite.visibility = View.VISIBLE
            recyProductsFavorite.visibility = View.GONE
        }

        swipeRefreshLayout.setRefreshing(false)
        Log.d("swipeRefreshLayout" , swipeRefreshLayout.isRefreshing.toString())
    }

    override fun failureFavorite(message: String) {
        swipeRefreshLayout.setRefreshing(false)
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
    }

    private val ALPHABETICAL_COMPARATOR = object : Comparator<Product> {
        override fun compare(a: Product, b: Product): Int {
            return a.nameProduct.compareTo(b.nameProduct)
        }
    }

    lateinit var adapter : ProductFavoriteAdapter
    lateinit var arrProducts : ArrayList<Product>
    lateinit var preProductFavorite : PreProductFavoriteActi
    var change = false
    var itemMenu : MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_product_favorite)

        setSupportActionBar(toolbarProductFavorite)
        toolbarProductFavorite.setTitleTextColor(Color.WHITE)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(com.example.qthien.t__t.R.string.products_like)
        supportActionBar?.setHomeAsUpIndicator(com.example.qthien.t__t.R.drawable.ic_arrow_back_white_24dp)

        preProductFavorite = PreProductFavoriteActi(this)

        arrProducts = ArrayList()
        adapter = ProductFavoriteAdapter(this , ALPHABETICAL_COMPARATOR)
        recyProductsFavorite.layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recyProductsFavorite.adapter = adapter

        preProductFavorite.getProductFavorite(MainActivity.customer!!.idCustomer)

        swipeRefreshLayout.setRefreshing(true)

        swipeRefreshLayout.setOnRefreshListener{
            preProductFavorite.getProductFavorite(MainActivity.customer!!.idCustomer)
        }

        btnContinuteOrder.setOnClickListener({
//            val intent = Intent(this , MainActivity::class.java)
//            if(change)
//                intent.putExtra("change" , change)
//            setResult(Activity.RESULT_OK , intent)
            createSharePre()
            finish()
        })
    }

    fun createSharePre(){
        getSharedPreferences("Favorite" , Context.MODE_PRIVATE).edit()
                .putBoolean( "changeFavorite" , change).apply()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
//        val intent = Intent(this , MainActivity::class.java)
//        if(change)
//            intent.putExtra("change" , change)
//        setResult(Activity.RESULT_CANCELED , intent)
        createSharePre()
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.example.qthien.t__t.R.menu.menu_product_favorite, menu)
        itemMenu = menu?.findItem(com.example.qthien.t__t.R.id.menu_search_product_favorite)
        val searchView = itemMenu?.actionView as SearchView
        searchView.setBackgroundColor(Color.WHITE)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                val lowerCaseQuery = query.toString().toLowerCase()
                val filteredModelList = arrProducts.filter { it.nameProduct.toLowerCase().contains(lowerCaseQuery)
                        || deAccent(it.nameProduct).toLowerCase().contains(lowerCaseQuery) }
                adapter.replaceAll(filteredModelList)
                recyProductsFavorite.scrollToPosition(0)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    fun deAccent(str: String): String {
        val nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(nfdNormalizedString).replaceAll("")
    }
}
