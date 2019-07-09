package com.example.qthien.t__t.mvp.view.ealuation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.qthien.t__t.EndlessRecyclerViewScrollListener
import com.example.qthien.t__t.adapter.ImagesAdapter
import com.example.qthien.t__t.model.ImagesSelected
import kotlinx.android.synthetic.main.activity_selected_img.*



class SelectedImgActivity : AppCompatActivity() , ImagesAdapter.ImagesAdapterCallActi {

    @SuppressLint("SetTextI18n")
    override fun selectedImage(url: String) {
        if(arrSelected.size + count!! < 3) {
            arrSelected.add(url)
            setTitle("${arrSelected.size + count!!}/$max")
        }
    }

    @SuppressLint("SetTextI18n")
    override fun unSelectedImage(url: String) {
        arrSelected.remove(url)
        setTitle("${arrSelected.size + count!!}/$max")
    }

    lateinit var arrImage : ArrayList<ImagesSelected>
    lateinit var arrImagePati : ArrayList<ImagesSelected>
    lateinit var arrSelected : ArrayList<String>
    lateinit var adapter : ImagesAdapter
    lateinit var layoutManager : GridLayoutManager
    var pagee = 1
    val limit = 40
    var max = 3
    var count : Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.qthien.t__t.R.layout.activity_selected_img)
        setTitle(com.example.qthien.t__t.R.string.selected_image)

        setSupportActionBar(toolbarSelectedImg)
        toolbarSelectedImg.setTitleTextColor(Color.WHITE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(com.example.qthien.t__t.R.drawable.ic_close)

        count = intent.extras?.getInt("count")

        arrSelected = ArrayList()
        getAllExternalImagesPath(this)
        arrImagePati = ArrayList()

        arrImagePati.addAll(arrImage.get(0 , limit))

        adapter = ImagesAdapter(this , arrImagePati)
        layoutManager = GridLayoutManager(this , 3)
        recyclerImg.layoutManager = layoutManager
        recyclerImg.adapter = adapter
        recyclerImg.isNestedScrollingEnabled = false

        val scrollListener =  object : EndlessRecyclerViewScrollListener(layoutManager){

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                pagee += 1
                val offset = (pagee - 1) * limit
                val arr = arrImage.get(offset , limit)
                arrImagePati.addAll(arr)
                adapter.notifyItemRangeInserted(adapter.itemCount , arr.size - 1)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        if(layoutManager.findFirstCompletelyVisibleItemPosition() == 0)
                            ibtnScrollToTop.visibility = View.GONE
                        else
                            ibtnScrollToTop.visibility = View.VISIBLE
                    }
                }
            }
        }

        recyclerImg.addOnScrollListener(scrollListener)

        ibtnScrollToTop.setOnClickListener({
            recyclerImg.scrollToPosition(0)
            ibtnScrollToTop.visibility = View.GONE
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.example.qthien.t__t.R.menu.menu_selected_img , menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == com.example.qthien.t__t.R.id.menu_donee){
            val i = Intent(this , CreateEvaluationActivity::class.java)
            i.putExtra("arrImage" , arrSelected)
            setResult(Activity.RESULT_OK , i)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun <T> ArrayList<T>.get(offset : Int , limit :Int) : ArrayList<T>{
        var l = limit + offset
        if(limit >= this.size)
            l = this.size - 1
        val arrResult = ArrayList<T>()
        for(i in offset..l - 1){
            arrResult.add(this.get(i))
        }
        return arrResult
    }

    @SuppressLint("Recycle")
    private fun getAllExternalImagesPath(activity: Activity){
        arrImage = ArrayList()
        val uri: Uri
        val cursor: Cursor?
        val column_index_data: Int
//        val column_index_folder_name: Int
        var absolutePathOfImage: String? = null
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.ImageColumns.DATE_TAKEN)

        cursor = activity.contentResolver.query(uri, projection, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC")

        column_index_data = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
//        column_index_folder_name = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)

            arrImage.add(ImagesSelected(absolutePathOfImage , false))
        }
    }
}
