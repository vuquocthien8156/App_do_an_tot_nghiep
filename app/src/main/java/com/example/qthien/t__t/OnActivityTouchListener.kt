package com.example.qthien.t__t

import android.view.MotionEvent


interface OnActivityTouchListener {
    fun getTouchCoordinates(ev: MotionEvent)
}