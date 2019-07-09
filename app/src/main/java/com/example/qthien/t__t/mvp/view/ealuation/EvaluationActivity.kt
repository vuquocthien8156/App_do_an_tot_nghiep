package com.example.qthien.t__t.mvp.view.ealuation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.qthien.t__t.EndlessRecyclerViewScrollListener
import com.example.qthien.t__t.R
import com.example.qthien.t__t.adapter.EvaluationAdapter
import com.example.qthien.t__t.model.Evaluation
import com.example.qthien.t__t.model.ResponseEvaluation
import com.example.qthien.t__t.model.VoteQuantity
import com.example.qthien.t__t.mvp.presenter.detail_product.PreDetailProduct
import com.example.qthien.t__t.mvp.view.detail_product.IDetailProduct
import com.example.qthien.t__t.mvp.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_evaluation.*

class EvaluationActivity : AppCompatActivity() , IDetailProduct ,
        EvaluationAdapter.EvaluationAdapterCall , AdapterView.OnItemSelectedListener{

    override fun failureCallDetail(message: String) {
        Toast.makeText(this , message , Toast.LENGTH_LONG).show()
    }

    override fun addTks(idEvluated: Int) {
        idEvaluteSelected = idEvluated
        PreDetailProduct(this).addTks(idEvluated , MainActivity.customer!!.idCustomer)
    }

    override fun successAddTks(status: String?) {
        if(status != null && status.equals("Success")) {
            addTksToSharePreference(idEvaluteSelected.toString())
            arrEvaluation.find { it.idEvaluation == idEvaluteSelected }!!.quantityTks += 1
            adapterEvaluation?.notifyDataSetChanged()
        }
        else
            Toast.makeText(this , R.string.fail_again , Toast.LENGTH_LONG).show()
    }

    override fun successCallDetail(response: ResponseEvaluation) {
        if(response.data.listEvaluttion.size > 0){
            Log.d("pagess" ,"success" +  pagee.toString())
            arrEvaluation.addAll(response.data.listEvaluttion)
            if(adapterEvaluation!!.itemCount == 0)
                adapterEvaluation?.notifyDataSetChanged()
            else
                adapterEvaluation?.notifyItemRangeInserted( adapterEvaluation!!.itemCount ,response.data.listEvaluttion.size - 1)
        }


        if(arrEvaluation.size > 0)
            txtNotEv.visibility = View.GONE
        else
            txtNotEv.visibility = View.VISIBLE

        if(response.data.quantityVote != null){
            setUpQuantityVote(response.data.quantityVote!!)
        }
        progressLoader.visibility = View.GONE
        progressLoaderFirst.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = false
    }

    private var adapterEvaluation : EvaluationAdapter? = null
    private lateinit var arrEvaluation  : ArrayList<Evaluation>
    var idProduct : Int? = 0
    var idEvaluteSelected = 0
    var pagee = 1
    var point : Int? = null
    var time : Int? = null

    lateinit var arrFilterTime : ArrayList<String>
    lateinit var arrFilterStar : ArrayList<String>

    lateinit var onScrollListener : EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_evaluation)

        val nameProduct = intent.extras?.getString("nameProduct")
        setTitle(nameProduct)
        setSupportActionBar(toolbarEvaluation)
        toolbarEvaluation.setTitleTextColor(Color.WHITE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        arrEvaluation = ArrayList()

        adapterEvaluation = EvaluationAdapter(this , arrEvaluation)
        val layoutManager = LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false)
        recyclerEvaluation.layoutManager = layoutManager
        recyclerEvaluation.adapter = adapterEvaluation
        val dividerItemDecoration = DividerItemDecoration(recyclerEvaluation.getContext(),
                LinearLayoutManager.VERTICAL)
        recyclerEvaluation.addItemDecoration(dividerItemDecoration)
        recyclerEvaluation.isNestedScrollingEnabled = false

        progressLoader.visibility = View.VISIBLE

        onScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                progressLoader.visibility = View.VISIBLE
                Log.d("pagess" , page.toString())
                pagee = page
                PreDetailProduct(this@EvaluationActivity).callDetailOrEvaluation(idProduct!! , MainActivity.customer!!.idCustomer , page + 1 , point , time , null)
            }
        }


        recyclerEvaluation.addOnScrollListener(onScrollListener)

        val vote = intent.extras?.getParcelable("vote_quantity") as VoteQuantity?
        idProduct = intent.extras?.getInt("idProduct")
        if(vote != null)
            setUpQuantityVote(vote)

        PreDetailProduct(this@EvaluationActivity).callDetailOrEvaluation(idProduct!! , MainActivity.customer!!.idCustomer , pagee , point , time , null)
        setupSpinner()

        swipeRefreshLayout.setOnRefreshListener {
            pagee = 0
            arrEvaluation.clear()
            adapterEvaluation?.notifyDataSetChanged()
            onScrollListener.resetState()
            PreDetailProduct(this@EvaluationActivity).callDetailOrEvaluation(idProduct!! , MainActivity.customer!!.idCustomer , 1 , point , time , 1)
        }

        btnWriteEv.setOnClickListener({
            val url = intent.extras?.getString("url")

            val i = Intent(this , CreateEvaluationActivity::class.java)
            i.putExtra("url" , url)
            i.putExtra("id" , idProduct!!)
            i.putExtra("name" , nameProduct)
            startActivity(i)
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun addTksToSharePreference(idEvalute : String){
        val share = getSharedPreferences("EvaluateTks" , Context.MODE_PRIVATE)
        val setTks =share.getStringSet("setTks" , null)
        if(setTks != null){
            setTks.add(idEvalute)
            share.edit().putStringSet("setTks" , setTks).apply()
        }
    }

    private fun setupSpinner(){
        arrFilterTime = arrayListOf(getString(R.string.new_evaluation), getString(R.string.old_evaluation))
        arrFilterStar = arrayListOf(getString(R.string.all) ,getString(R.string.five_star) , getString(R.string.four_star),
                getString(R.string.three_star), getString(R.string.two_star) , getString(R.string.one_star))

        val timeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrFilterTime)
        val starAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrFilterStar)
        timeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        starAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
        //Setting the ArrayAdapter data on the Spinner
        spinerTime.adapter = timeAdapter
        spinetVote.adapter = starAdapter

        spinerTime.onItemSelectedListener = this
        spinetVote.onItemSelectedListener = this
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d("ádsafsfas" , (parent?.id == R.id.spinerTime).toString())
        if(parent?.id == R.id.spinerTime){
            Log.d("ádsafsfas" , position.toString())
            time = if(position == 0) null else 1
        }
        if(parent?.id == R.id.spinetVote){
            Log.d("ádsafsfas" , position.toString())
            point = when(position){
                1 -> 5
                2 -> 4
                3 -> 3
                4 -> 2
                5 -> 1
                else -> null
            }
        }
        pagee = 0
        progressLoaderFirst.visibility = View.VISIBLE
        arrEvaluation.clear()
        adapterEvaluation?.notifyDataSetChanged()
        onScrollListener.resetState()
        PreDetailProduct(this@EvaluationActivity).callDetailOrEvaluation(idProduct!! ,
                MainActivity.customer!!.idCustomer , 1 , point , time , null)
    }

    @SuppressLint("SetTextI18n")
    fun setUpQuantityVote(quantityVote : VoteQuantity){
        txtTotalEvaluation.setText("${quantityVote.total} ${getString(R.string.vote)}")
        val five = Math.round((quantityVote.totalFive * 100).toDouble() / quantityVote.total).toInt()
        val four = Math.round((quantityVote.totalFour * 100).toDouble() / quantityVote.total).toInt()
        val three = Math.round((quantityVote.totalThree * 100).toDouble() / quantityVote.total).toInt()
        val two = Math.round((quantityVote.totalTwo * 100).toDouble() / quantityVote.total).toInt()
        val one = Math.round((quantityVote.totalOne * 100).toDouble() / quantityVote.total).toInt()

        progressFive.progress = five
        progressFour.progress = four
        progressThree.progress = three
        progressTwo.progress = two
        progressOne.progress = one

        txtPercentFive.setText("$five%")
        txtPercentFour.setText("$four%")
        txtPercentThree.setText("$three%")
        txtPercentTwo.setText("$two%")
        txtPercentOne.setText("$one%")

        val average = ( (quantityVote.totalFive.times(5)) + (quantityVote.totalFour.times(4)) +
                (quantityVote.totalThree.times(3)) + (quantityVote.totalTwo.times(2)) + (quantityVote.totalOne.times(1)) ).toDouble() / quantityVote.total
        val cacuAve = Math.round(average*2)/2
//        Log.("progress" , )
        ratingRatio.progress = cacuAve.toInt()
        txtRatio.setText("$cacuAve/5")
    }
}
