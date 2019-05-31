package com.example.qthien.t__t

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.example.qthien.t__t.adapter.DeliveryAdapterAddress

/**
 * Created by ravi on 29/09/17.
 */

class RecyclerItemTouchHelper(dragDirs: Int, swipeDirs: Int , private val listener: RecyclerItemTouchHelperListener) :
    ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            val foregroundView = (viewHolder as DeliveryAdapterAddress.ViewHolder).rowFG

            getDefaultUIUtil().onSelected(foregroundView)
        }
    }

    override fun onChildDrawOver(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {

        val backgroundViewEdit = (viewHolder as DeliveryAdapterAddress.ViewHolder).layoutEdit
        val backgroundViewDelete = (viewHolder).layoutDelete


        if (dX > 0f) {
            backgroundViewEdit.visibility = View.VISIBLE
            backgroundViewDelete.visibility = View.GONE
        }

        if(dX < 0){
            backgroundViewEdit.visibility = View.GONE
            backgroundViewDelete.visibility = View.VISIBLE
        }

        val foregroundView = (viewHolder).rowFG

        getDefaultUIUtil().onDrawOver(
            c, recyclerView, foregroundView, dX, dY,
            actionState, isCurrentlyActive
        )

    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val foregroundView = (viewHolder as DeliveryAdapterAddress.ViewHolder).rowFG
        getDefaultUIUtil().clearView(foregroundView)
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        val backgroundViewEdit = (viewHolder as DeliveryAdapterAddress.ViewHolder).layoutEdit
        val backgroundViewDelete = (viewHolder).layoutDelete


        if (dX > 0f) {
            backgroundViewEdit.visibility = View.VISIBLE
            backgroundViewDelete.visibility = View.GONE
        }

        if(dX < 0){
            backgroundViewEdit.visibility = View.GONE
            backgroundViewDelete.visibility = View.VISIBLE
        }

        val foregroundView = (viewHolder).rowFG

        getDefaultUIUtil().onDraw(
            c, recyclerView, foregroundView, dX, dY,
            actionState, isCurrentlyActive
        )
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        if(viewHolder is DeliveryAdapterAddress.ViewHolder){
            if(viewHolder.adapterPosition == 0){
                val dragFlags = ItemTouchHelper.RIGHT
                val swipeFlags = ItemTouchHelper.RIGHT
                return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
            }
        }

        return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition())
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    interface RecyclerItemTouchHelperListener {
        fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
    }
}