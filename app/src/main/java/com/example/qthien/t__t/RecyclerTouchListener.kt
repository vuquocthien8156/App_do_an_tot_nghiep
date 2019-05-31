package com.example.qthien.t__t

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.graphics.Rect
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.DecelerateInterpolator
import android.widget.ListView
import java.util.*
import kotlin.collections.ArrayList

class RecyclerTouchListener(
    internal var act: Activity, // Fixed properties
    private val rView: RecyclerView?
) : RecyclerView.OnItemTouchListener , OnActivityTouchListener {
    internal val handler = Handler()
    internal var unSwipeableRows: List<Int>
    /*
     * independentViews are views on the foreground layer which when clicked, act "independent" from the foreground
     * ie, they are treated separately from the "row click" action
     */
    internal var independentViews: List<Int>
    internal var unClickableRows: List<Int>
    internal var optionViews: List<Int>
    internal var ignoredViewTypes: MutableSet<Int>
    // Cached ViewConfiguration and system-wide constant values
    private val touchSlop: Int
    private val minFlingVel: Int
    private val maxFlingVel: Int
    private val ANIMATION_STANDARD: Long = 300
    private val ANIMATION_CLOSE: Long = 150
    // private SwipeListener mSwipeListener;
    private var bgWidth = 1
    private val bgWidthLeft = 1 // 1 and not 0 to prevent dividing by zero
    // Transient properties
    // private List<PendingDismissData> mPendingDismisses = new ArrayList<>();
    private var mDismissAnimationRefCount = 0
    private var touchedX: Float = 0.toFloat()
    private var touchedY: Float = 0.toFloat()
    private var isFgSwiping: Boolean = false
    private var mSwipingSlop: Int = 0
    private var mVelocityTracker: VelocityTracker? = null
    private var touchedPosition: Int = 0
    private var touchedView: View? = null
    private var mPaused: Boolean = false
    private var bgVisible: Boolean = false
    private var fgPartialViewClicked: Boolean = false
    private var bgVisiblePosition: Int = 0
    private var bgVisibleView: View? = null
    private var isRViewScrolling: Boolean = false
    private var heightOutsideRView: Int = 0
    private var screenHeight: Int = 0
    private var mLongClickPerformed: Boolean = false
    // Foreground view (to be swiped), Background view (to show)
    private var fgView: View? = null
    private var bgView: View? = null
    //view ID
    private var fgViewID: Int = 0
    private var bgViewID: Int = 0
    private val bgViewIDLeft: Int = 0
    private var fadeViews: ArrayList<Int>? = null
    private var mRowClickListener: OnRowClickListener? = null
    private var mRowLongClickListener: OnRowLongClickListener? = null
    private var mBgClickListener: OnSwipeOptionsClickListener? = null
    private val mBgClickListenerLeft: OnSwipeOptionsClickListener? = null
    // user choices
    private var clickable = false
    private var longClickable = false
    private var swipeable = false
    private val swipeableLeftOptions = false
    private val LONG_CLICK_DELAY = 800
    private var longClickVibrate: Boolean = false
    internal var mLongPressed: Runnable

    init {
        val vc = ViewConfiguration.get(rView?.getContext())
        touchSlop = vc.getScaledTouchSlop()
        minFlingVel = vc.getScaledMinimumFlingVelocity() * 16
        maxFlingVel = vc.getScaledMaximumFlingVelocity()
        bgVisible = false
        bgVisiblePosition = -1
        bgVisibleView = null
        fgPartialViewClicked = false
        unSwipeableRows = ArrayList()
        unClickableRows = ArrayList()
        ignoredViewTypes = HashSet()
        independentViews = ArrayList()
        optionViews = ArrayList()
        fadeViews = ArrayList()
        isRViewScrolling = false

        mLongPressed = Runnable {
            if (!longClickable)
                return@Runnable

            mLongClickPerformed = true

            if (!bgVisible && touchedPosition >= 0 && !unClickableRows.contains(touchedPosition) && !isRViewScrolling) {
                if (longClickVibrate) {
                    //                    Vibrator vibe = (Vibrator) act.getSystemService(Context.VIBRATOR_SERVICE);
                    //                    vibe.vibrate(100); // do we really need to add vibrate service
                }
                mRowLongClickListener!!.onRowLongClicked(touchedPosition)
            }
        }

        //        mSwipeListener = listener;

        rView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                /**
                 * This will ensure that this RecyclerTouchListener is paused during recycler view scrolling.
                 * If a scroll listener is already assigned, the caller should still pass scroll changes through
                 * to this listener.
                 */
                setEnabled(newState != RecyclerView.SCROLL_STATE_DRAGGING)

                /**
                 * This is used so that clicking a row cannot be done while scrolling
                 */
                isRViewScrolling = newState != RecyclerView.SCROLL_STATE_IDLE
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            }
        })
    }

    /**
     * Enables or disables (pauses or resumes) watching for swipe-to-dismiss gestures.
     *
     * @param enabled Whether or not to watch for gestures.
     */
    fun setEnabled(enabled: Boolean) {
        mPaused = !enabled
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, motionEvent: MotionEvent): Boolean {
        return handleTouchEvent(motionEvent)
    }

    override fun onTouchEvent(rv: RecyclerView, motionEvent: MotionEvent) {
        handleTouchEvent(motionEvent)
    }

/*
////////////// Clickable ////////////////////
* */

    fun setClickable(listener: OnRowClickListener): RecyclerTouchListener {
        this.clickable = true
        this.mRowClickListener = listener
        return this
    }

    fun setClickable(clickable: Boolean): RecyclerTouchListener {
        this.clickable = clickable
        return this
    }

    fun setLongClickable(vibrate: Boolean, listener: OnRowLongClickListener): RecyclerTouchListener {
        this.longClickable = true
        this.mRowLongClickListener = listener
        this.longClickVibrate = vibrate
        return this
    }

    fun setLongClickable(longClickable: Boolean): RecyclerTouchListener {
        this.longClickable = longClickable
        return this
    }

    fun setIndependentViews(vararg viewIds: Int): RecyclerTouchListener {
        this.independentViews = viewIds.asList()
        return this
    }

    fun setUnClickableRows(vararg rows: Int): RecyclerTouchListener {
        this.unClickableRows = rows.asList()
        return this
    }

    fun setIgnoredViewTypes(vararg viewTypes: Int): RecyclerTouchListener {
        ignoredViewTypes.clear()
        ignoredViewTypes.addAll(viewTypes.asList())
        return this
    }

    //////////////// Swipeable ////////////////////

    fun setSwipeable(
        foregroundID: Int,
        backgroundID: Int,
        listener: OnSwipeOptionsClickListener
    ): RecyclerTouchListener {
        this.swipeable = true
        if (fgViewID != 0 && foregroundID != fgViewID)
            throw IllegalArgumentException("foregroundID does not match previously set ID")
        fgViewID = foregroundID
        bgViewID = backgroundID
        this.mBgClickListener = listener

        if (act is RecyclerTouchListenerHelper)
            (act as RecyclerTouchListenerHelper).setOnActivityTouchListener(this)

        val displaymetrics = DisplayMetrics()
        act.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics)
        screenHeight = displaymetrics.heightPixels

        return this
    }

    fun setSwipeable(value: Boolean): RecyclerTouchListener {
        this.swipeable = value
        if (!value)
            invalidateSwipeOptions()
        return this
    }

    fun setSwipeOptionViews(vararg viewIds: Int): RecyclerTouchListener {
        this.optionViews = viewIds.asList()
        return this
    }

    fun setUnSwipeableRows(vararg rows: Int): RecyclerTouchListener {
        this.unSwipeableRows = rows.asList()
        return this
    }

    //////////////// Fade Views ////////////////////

    // Set views which are faded out as fg is opened
    fun setViewsToFade(vararg viewIds: Int): RecyclerTouchListener {
        this.fadeViews = ArrayList(viewIds.asList())
        return this
    }

    // the entire foreground is faded out as it is opened
    fun setFgFade(): RecyclerTouchListener {
        if (!fadeViews!!.contains(fgViewID))
            this.fadeViews!!.add(fgViewID)
        return this
    }

    //-------------- Checkers for preventing ---------------//

    private fun isIndependentViewClicked(motionEvent: MotionEvent): Boolean {
        for (i in independentViews.indices) {
            if (touchedView != null) {
                val rect = Rect()
                val x = motionEvent.getRawX() as Int
                val y = motionEvent.getRawY() as Int
                touchedView!!.findViewById<View>(independentViews.get(i)).getGlobalVisibleRect(rect)
                if (rect.contains(x, y)) {
                    return false
                }
            }
        }
        return true
    }

    private fun getOptionViewID(motionEvent: MotionEvent): Int {
        for (i in optionViews.indices) {
            if (touchedView != null) {
                val rect = Rect()
                val x = motionEvent.getRawX()
                val y = motionEvent.getRawY()
                touchedView!!.findViewById<View>(optionViews.get(i)).getGlobalVisibleRect(rect)
                if (rect.contains(x.toInt(), y.toInt())) {
                    return optionViews.get(i)
                }
            }
        }
        return -1
    }

    private fun getIndependentViewID(motionEvent: MotionEvent): Int {
        for (i in independentViews.indices) {
            if (touchedView != null) {
                val rect = Rect()
                val x = motionEvent.getRawX() as Int
                val y = motionEvent.getRawY() as Int
                touchedView!!.findViewById<View>(independentViews.get(i)).getGlobalVisibleRect(rect)
                if (rect.contains(x, y)) {
                    return independentViews.get(i)
                }
            }
        }
        return -1
    }

    public override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }

    fun invalidateSwipeOptions() {
        bgWidth = 1
    }

    fun openSwipeOptions(position: Int) {
        if ((!swipeable || rView!!.getChildAt(position) == null
                    || unSwipeableRows.contains(position) || shouldIgnoreAction(position))
        )
            return
        if (bgWidth < 2) {
            if (act.findViewById<View>(bgViewID) != null)
                bgWidth = act.findViewById<View>(bgViewID).getWidth()
            heightOutsideRView = screenHeight - rView.getHeight()
        }
        touchedPosition = position
        touchedView = rView.getChildAt(position)
        fgView = touchedView!!.findViewById(fgViewID)
        bgView = touchedView!!.findViewById(bgViewID)
        bgView!!.setMinimumHeight(fgView!!.getHeight())

        closeVisibleBG(null)
        animateFG(touchedView, Animation.OPEN, ANIMATION_STANDARD)
        bgVisible = true
        bgVisibleView = fgView
        bgVisiblePosition = touchedPosition
    }

    @Deprecated("")
    fun closeVisibleBG() {
        if (bgVisibleView == null) {
            Log.e(TAG, "No rows found for which background options are visible")
            return
        }
        bgVisibleView!!.animate()
            .translationX(0F)
            .setDuration(ANIMATION_CLOSE)
            .setListener(null)

        animateFadeViews(bgVisibleView, 1f, ANIMATION_CLOSE)
        bgVisible = false
        bgVisibleView = null
        bgVisiblePosition = -1
    }

    fun closeVisibleBG(mSwipeCloseListener: OnSwipeListener?) {
        if (bgVisibleView == null) {
            Log.e(TAG, "No rows found for which background options are visible")
            return
        }
        val translateAnimator = ObjectAnimator.ofFloat(
            bgVisibleView,
            View.TRANSLATION_X, 0f
        )
        translateAnimator.setDuration(ANIMATION_CLOSE)
        translateAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                if (mSwipeCloseListener != null)
                    mSwipeCloseListener.onSwipeOptionsClosed()
                translateAnimator.removeAllListeners()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
        translateAnimator.start()

        animateFadeViews(bgVisibleView, 1f, ANIMATION_CLOSE)
        bgVisible = false
        bgVisibleView = null
        bgVisiblePosition = -1
    }

    private fun animateFadeViews(downView: View?, alpha: Float, duration: Long) {
        if (fadeViews != null) {
            for (viewID in fadeViews!!) {
                downView!!.findViewById<View>(viewID).animate()
                    .alpha(alpha)
                    .setDuration(duration)
            }
        }
    }

    private fun animateFG(downView: View?, animateType: Animation, duration: Long) {
        if (animateType == Animation.OPEN) {
            val translateAnimator = ObjectAnimator.ofFloat(
                fgView, View.TRANSLATION_X, -bgWidth.toFloat()
            )
            translateAnimator.setDuration(duration)
            translateAnimator.setInterpolator(DecelerateInterpolator(1.5f))
            translateAnimator.start()
            animateFadeViews(downView, 0f, duration)
        } else if (animateType == Animation.CLOSE) {
            val translateAnimator = ObjectAnimator.ofFloat(
                fgView, View.TRANSLATION_X, 0f
            )
            translateAnimator.setDuration(duration)
            translateAnimator.setInterpolator(DecelerateInterpolator(1.5f))
            translateAnimator.start()
            animateFadeViews(downView, 1f, duration)
        }
    }

    private fun animateFG(
        downView: View?, animateType: Animation, duration: Long,
        mSwipeCloseListener: OnSwipeListener?
    ) {
        val translateAnimator: ObjectAnimator
        if (animateType == Animation.OPEN) {
            translateAnimator = ObjectAnimator.ofFloat(fgView, View.TRANSLATION_X, -bgWidth.toFloat())
            translateAnimator.setDuration(duration)
            translateAnimator.setInterpolator(DecelerateInterpolator(1.5f))
            translateAnimator.start()
            animateFadeViews(downView, 0f, duration)
        } else
        /*if (animateType == Animation.CLOSE)*/ {
            translateAnimator = ObjectAnimator.ofFloat(fgView, View.TRANSLATION_X, 0f)
            translateAnimator.setDuration(duration)
            translateAnimator.setInterpolator(DecelerateInterpolator(1.5f))
            translateAnimator.start()
            animateFadeViews(downView, 1f, duration)
        }

        translateAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                if (mSwipeCloseListener != null) {
                    if (animateType == Animation.OPEN)
                        mSwipeCloseListener.onSwipeOptionsOpened()
                    else if (animateType == Animation.CLOSE)
                        mSwipeCloseListener.onSwipeOptionsClosed()
                }
                translateAnimator.removeAllListeners()
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    private fun handleTouchEvent(motionEvent: MotionEvent): Boolean {
        if (swipeable && bgWidth < 2) {
            //            bgWidth = rView.getWidth();
            if (act.findViewById<View>(bgViewID) != null)
                bgWidth = act.findViewById<View>(bgViewID).getWidth()

            heightOutsideRView = screenHeight - rView!!.getHeight()
        }

        when (motionEvent.getActionMasked()) {

            // When finger touches screen
            MotionEvent.ACTION_DOWN -> {
                if (mPaused) {
                    return false
                }

                // Find the child view that was touched (perform a hit test)
                val rect = Rect()
                val childCount = rView!!.getChildCount()
                val listViewCoords = IntArray(2)
                rView.getLocationOnScreen(listViewCoords)
                // x and y values respective to the recycler view
                var x = motionEvent.getRawX() - listViewCoords[0]
                var y = motionEvent.getRawY() - listViewCoords[1]
                var child: View

                /*
                                * check for every child (row) in the recycler view whether the touched co-ordinates belong to that
                                * respective child and if it does, register that child as the touched view (touchedView)
                                */
                for (i in 0 until childCount) {
                    child = rView.getChildAt(i)
                    child.getHitRect(rect)
                    if (rect.contains(x.toInt(), y.toInt())) {
                        touchedView = child
                        break
                    }
                }

                if (touchedView != null) {
                    touchedX = motionEvent.getRawX()
                    touchedY = motionEvent.getRawY()
                    touchedPosition = rView.getChildAdapterPosition(touchedView!!)

                    if (shouldIgnoreAction(touchedPosition)) {
                        touchedPosition = ListView.INVALID_POSITION
                        return false   // <-- guard here allows for ignoring events, allowing more than one view type and preventing NPE
                    }

                    if (longClickable) {
                        mLongClickPerformed = false
                        handler.postDelayed(mLongPressed, LONG_CLICK_DELAY.toLong())
                    }
                    if (swipeable) {
                        mVelocityTracker = VelocityTracker.obtain()
                        mVelocityTracker!!.addMovement(motionEvent)
                        fgView = touchedView!!.findViewById(fgViewID)
                        bgView = touchedView!!.findViewById(bgViewID)
                        //                        bgView.getLayoutParams().height = fgView.getHeight();
                        bgView!!.setMinimumHeight(fgView!!.getHeight())

                        /*
                                                * bgVisible is true when the options menu is opened
                                                * This block is to register fgPartialViewClicked status - Partial view is the view that is still
                                                * shown on the screen if the options width is < device width
                                                */
                        if (bgVisible && fgView != null) {
                            handler.removeCallbacks(mLongPressed)
                            x = motionEvent.getRawX()
                            y = motionEvent.getRawY()
                            fgView!!.getGlobalVisibleRect(rect)
                            fgPartialViewClicked = rect.contains(x.toInt(), y.toInt())
                        } else {
                            fgPartialViewClicked = false
                        }
                    }
                }

                /*
                                * If options menu is shown and the touched position is not the same as the row for which the
                                * options is displayed - close the options menu for the row which is displaying it
                                * (bgVisibleView and bgVisiblePosition is used for this purpose which registers which view and
                                * which position has it's options menu opened)
                                */
                x = motionEvent.getRawX()
                y = motionEvent.getRawY()
                rView.getHitRect(rect)
                if (swipeable && bgVisible && touchedPosition != bgVisiblePosition) {
                    handler.removeCallbacks(mLongPressed)
                    closeVisibleBG(null)
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                handler.removeCallbacks(mLongPressed)
                if (mLongClickPerformed)
                    return false

                if (mVelocityTracker == null) {
                    return false
                }
                if (swipeable) {
                    if (touchedView != null && isFgSwiping) {
                        // cancel
                        animateFG(touchedView, Animation.CLOSE, ANIMATION_STANDARD)
                    }
                    mVelocityTracker!!.recycle()
                    mVelocityTracker = null
                    isFgSwiping = false
                    bgView = null
                }
                touchedX = 0f
                touchedY = 0f
                touchedView = null
                touchedPosition = ListView.INVALID_POSITION
            }

            // When finger is lifted off the screen (after clicking, flinging, swiping, etc..)
            MotionEvent.ACTION_UP -> {
                run({
                    handler.removeCallbacks(mLongPressed)
                    if (mLongClickPerformed)
                        return false

                    if (mVelocityTracker == null && swipeable) {
                        return false
                    }
                    if (touchedPosition < 0)
                        return false

                    // swipedLeft and swipedRight are true if the user swipes in the respective direction (no conditions)
                    var swipedLeft = false
                    var swipedRight = false
                    /*
                                    * swipedLeftProper and swipedRightProper are true if user swipes in the respective direction
                                    * and if certain conditions are satisfied (given some few lines below)
                                    */
                    var swipedLeftProper = false
                    var swipedRightProper = false

                    val mFinalDelta = motionEvent.getRawX() - touchedX

                    // if swiped in a direction, make that respective variable true
                    if (isFgSwiping) {
                        swipedLeft = mFinalDelta < 0
                        swipedRight = mFinalDelta > 0
                    }

                    /*
                                    * If the user has swiped more than half of the width of the options menu, or if the
                                    * velocity of swiping is between min and max fling values
                                    * "proper" variable are set true
                                    */
                    if (Math.abs(mFinalDelta) > bgWidth / 2 && isFgSwiping) {
                        swipedLeftProper = mFinalDelta < 0
                        swipedRightProper = mFinalDelta > 0
                    } else if (swipeable) {
                        mVelocityTracker!!.addMovement(motionEvent)
                        mVelocityTracker!!.computeCurrentVelocity(1000)
                        val velocityX = mVelocityTracker!!.getXVelocity()
                        val absVelocityX = Math.abs(velocityX)
                        val absVelocityY = Math.abs(mVelocityTracker!!.getYVelocity())
                        if ((minFlingVel <= absVelocityX && absVelocityX <= maxFlingVel
                                    && absVelocityY < absVelocityX && isFgSwiping)
                        ) {
                            // dismiss only if flinging in the same direction as dragging
                            swipedLeftProper = (velocityX < 0) == (mFinalDelta < 0)
                            swipedRightProper = (velocityX > 0) == (mFinalDelta > 0)
                        }
                    }

                    ///////// Manipulation of view based on the 4 variables mentioned above ///////////

                    // if swiped left properly and options menu isn't already visible, animate the foreground to the left
                    if ((swipeable && !swipedRight && swipedLeftProper && touchedPosition != RecyclerView.NO_POSITION
                                && !unSwipeableRows.contains(touchedPosition) && !bgVisible)
                    ) {

                        val downView = touchedView // touchedView gets null'd before animation ends
                        val downPosition = touchedPosition
                        ++mDismissAnimationRefCount
                        //TODO - speed
                        animateFG(touchedView, Animation.OPEN, ANIMATION_STANDARD)
                        bgVisible = true
                        bgVisibleView = fgView
                        bgVisiblePosition = downPosition
                    } else if ((swipeable && !swipedLeft && swipedRightProper && touchedPosition != RecyclerView.NO_POSITION
                                && !unSwipeableRows.contains(touchedPosition) && bgVisible)
                    ) {
                        // dismiss
                        val downView = touchedView // touchedView gets null'd before animation ends
                        val downPosition = touchedPosition

                        ++mDismissAnimationRefCount
                        //TODO - speed
                        animateFG(touchedView, Animation.CLOSE, ANIMATION_STANDARD)
                        bgVisible = false
                        bgVisibleView = null
                        bgVisiblePosition = -1
                    } else if (swipeable && swipedLeft && !bgVisible) {
                        // cancel
                        val tempBgView = bgView
                        animateFG(touchedView, Animation.CLOSE, ANIMATION_STANDARD, object : OnSwipeListener {
                            public override fun onSwipeOptionsClosed() {
                                if (tempBgView != null)
                                    tempBgView!!.setVisibility(View.VISIBLE)
                            }

                            public override fun onSwipeOptionsOpened() {

                            }
                        })

                        bgVisible = false
                        bgVisibleView = null
                        bgVisiblePosition = -1
                    } else if (swipeable && swipedRight && bgVisible) {
                        // cancel
                        animateFG(touchedView, Animation.OPEN, ANIMATION_STANDARD)
                        bgVisible = true
                        bgVisibleView = fgView
                        bgVisiblePosition = touchedPosition
                    } else if (swipeable && swipedRight && !bgVisible) {
                        // cancel
                        animateFG(touchedView, Animation.CLOSE, ANIMATION_STANDARD)
                        bgVisible = false
                        bgVisibleView = null
                        bgVisiblePosition = -1
                    } else if (swipeable && swipedLeft && bgVisible) {
                        // cancel
                        animateFG(touchedView, Animation.OPEN, ANIMATION_STANDARD)
                        bgVisible = true
                        bgVisibleView = fgView
                        bgVisiblePosition = touchedPosition
                    } else if (!swipedRight && !swipedLeft) {
                        // if partial foreground view is clicked (see ACTION_DOWN) bring foreground back to original position
                        // bgVisible is true automatically since it's already checked in ACTION_DOWN block
                        if (swipeable && fgPartialViewClicked) {
                            animateFG(touchedView, Animation.CLOSE, ANIMATION_STANDARD)
                            bgVisible = false
                            bgVisibleView = null
                            bgVisiblePosition = -1
                        } else if ((clickable && !bgVisible && touchedPosition >= 0 && !unClickableRows.contains(
                                touchedPosition
                            )
                                    && isIndependentViewClicked(motionEvent) && !isRViewScrolling)
                        ) {
                            mRowClickListener!!.onRowClicked(touchedPosition)
                        } else if ((clickable && !bgVisible && touchedPosition >= 0 && !unClickableRows.contains(
                                touchedPosition
                            )
                                    && !isIndependentViewClicked(motionEvent) && !isRViewScrolling)
                        ) {
                            val independentViewID = getIndependentViewID(motionEvent)
                            if (independentViewID >= 0)
                                mRowClickListener!!.onIndependentViewClicked(independentViewID, touchedPosition)
                        } else if (swipeable && bgVisible && !fgPartialViewClicked) {
                            val optionID = getOptionViewID(motionEvent)
                            if (optionID >= 0 && touchedPosition >= 0) {
                                val downPosition = touchedPosition
                                closeVisibleBG(object : OnSwipeListener {
                                    public override fun onSwipeOptionsClosed() {
                                        mBgClickListener!!.onSwipeOptionClicked(optionID, downPosition)
                                    }

                                    public override fun onSwipeOptionsOpened() {

                                    }
                                })
                            }
                        }// On Click listener for background options
                        // On Click listener for independent views inside the rows
                        // On Click listener for rows
                    }// if clicked
                    // This case deals with an error where the user can swipe right, then left
                    // really fast and the fg is stuck open - so in that case we open the fg
                    // This case deals with an error where the user can swipe left, then right
                    // really fast and the fg is stuck open - so in that case we close the fg
                    // else if swiped right incorrectly (not satisfying the above conditions), animate the foreground to
                    // it's open position (spring effect)
                    // else if swiped left incorrectly (not satisfying the above conditions), animate the foreground back to
                    // it's original position (spring effect)
                    // else if swiped right properly when options menu is visible, close the menu and bring the foreground
                    // to it's original position
                })
                // if clicked and not swiped

                if (swipeable) {
                    mVelocityTracker!!.recycle()
                    mVelocityTracker = null
                }
                touchedX = 0f
                touchedY = 0f
                touchedView = null
                touchedPosition = ListView.INVALID_POSITION
                isFgSwiping = false
                bgView = null
            }

            // when finger is moving across the screen (and not yet lifted)
            MotionEvent.ACTION_MOVE -> {
                if (mLongClickPerformed)
                    return false
                if (mVelocityTracker == null || mPaused || !swipeable) {
                    return false
                }

                mVelocityTracker!!.addMovement(motionEvent)
                val deltaX = motionEvent.getRawX() - touchedX
                val deltaY = motionEvent.getRawY() - touchedY

                /*
                                * isFgSwiping variable which is set to true here is used to alter the swipedLeft, swipedRightProper
                                * variables in "ACTION_UP" block by checking if user is actually swiping at present or not
                                */
                if (!isFgSwiping && Math.abs(deltaX) > touchSlop && Math.abs(deltaY) < Math.abs(deltaX) / 2) {
                    handler.removeCallbacks(mLongPressed)
                    isFgSwiping = true
                    mSwipingSlop = (if (deltaX > 0) touchSlop else -touchSlop)
                }

                // This block moves the foreground along with the finger when swiping
                if (swipeable && isFgSwiping && !unSwipeableRows.contains(touchedPosition)) {
                    if (bgView == null) {
                        bgView = touchedView!!.findViewById(bgViewID)
                        bgView!!.setVisibility(View.VISIBLE)
                    }
                    // if fg is being swiped left
                    if (deltaX < touchSlop && !bgVisible) {
                        val translateAmount = deltaX - mSwipingSlop
                        //                        if ((Math.abs(translateAmount) > bgWidth ? -bgWidth : translateAmount) <= 0) {
                        // swipe fg till width of bg. If swiped further, nothing happens (stalls at width of bg)
                        fgView!!.setTranslationX(if (Math.abs(translateAmount) > bgWidth) -bgWidth.toFloat() else translateAmount)
                        if (fgView!!.getTranslationX() > 0) fgView!!.setTranslationX(0F)
                        //                        }

                        // fades all the fadeViews gradually to 0 alpha as dragged
                        if (fadeViews != null) {
                            for (viewID in fadeViews!!) {
                                touchedView!!.findViewById<View>(viewID).setAlpha(1 - (Math.abs(translateAmount) / bgWidth))
                            }
                        }
                    } else if (deltaX > 0 && bgVisible) {
                        // for closing rightOptions
                        if (bgVisible) {
                            val translateAmount = (deltaX - mSwipingSlop) - bgWidth

                            // swipe fg till it reaches original position. If swiped further, nothing happens (stalls at 0)
                            fgView!!.setTranslationX(if (translateAmount > 0) 0F else translateAmount)

                            // fades all the fadeViews gradually to 0 alpha as dragged
                            if (fadeViews != null) {
                                for (viewID in fadeViews!!) {
                                    touchedView!!.findViewById<View>(viewID)
                                        .setAlpha(1 - (Math.abs(translateAmount) / bgWidth))
                                }
                            }
                        } else {
                            val translateAmount = (deltaX - mSwipingSlop) - bgWidth

                            // swipe fg till it reaches original position. If swiped further, nothing happens (stalls at 0)
                            fgView!!.setTranslationX(if (translateAmount > 0) 0F else translateAmount)

                            // fades all the fadeViews gradually to 0 alpha as dragged
                            if (fadeViews != null) {
                                for (viewID in fadeViews!!) {
                                    touchedView!!.findViewById<View>(viewID)
                                        .setAlpha(1 - (Math.abs(translateAmount) / bgWidth))
                                }
                            }
                        }// for opening leftOptions
                    }// if fg is being swiped right
                    return true
                } else if (swipeable && isFgSwiping && unSwipeableRows.contains(touchedPosition)) {
                    if (deltaX < touchSlop && !bgVisible) {
                        val translateAmount = deltaX - mSwipingSlop
                        if (bgView == null)
                            bgView = touchedView!!.findViewById(bgViewID)

                        if (bgView != null)
                            bgView!!.setVisibility(View.GONE)

                        // swipe fg till width of bg. If swiped further, nothing happens (stalls at width of bg)
                        fgView!!.setTranslationX(translateAmount / 5)
                        if (fgView!!.getTranslationX() > 0) fgView!!.setTranslationX(0F)

                        // fades all the fadeViews gradually to 0 alpha as dragged
                        //                        if (fadeViews != null) {
                        //                            for (int viewID : fadeViews) {
                        //                                touchedView.findViewById(viewID).setAlpha(1 - (Math.abs(translateAmount) / bgWidth));
                        //                            }
                        //                        }
                    }
                    return true
                }// moves the fg slightly to give the illusion of an "unswipeable" row
            }
        }
        return false
    }

    /**
     * Gets coordinates from Activity and closes any
     * swiped rows if touch happens outside the recycler view
     */
    override fun getTouchCoordinates(ev: MotionEvent) {
        val y = ev.getRawY() as Int
        if ((swipeable && bgVisible && ev.getActionMasked() === MotionEvent.ACTION_DOWN
                    && y < heightOutsideRView)
        )
            closeVisibleBG(null)
    }

    private fun shouldIgnoreAction(touchedPosition: Int): Boolean {
        return rView == null || ignoredViewTypes.contains(rView!!.getAdapter()!!.getItemViewType(touchedPosition))
    }

    private enum class Animation {
        OPEN, CLOSE
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////  Interfaces  /////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////

    interface OnRowClickListener {
        fun onRowClicked(position: Int)

        fun onIndependentViewClicked(independentViewID: Int, position: Int)
    }

    interface OnRowLongClickListener {
        fun onRowLongClicked(position: Int)
    }

    interface OnSwipeOptionsClickListener {
        fun onSwipeOptionClicked(viewID: Int, position: Int)
    }

    interface RecyclerTouchListenerHelper {
        fun setOnActivityTouchListener(listener: OnActivityTouchListener)
    }

    interface OnSwipeListener {
        fun onSwipeOptionsClosed()

        fun onSwipeOptionsOpened()
    }

    companion object {
        private val TAG = "RecyclerTouchListener"
    }
}