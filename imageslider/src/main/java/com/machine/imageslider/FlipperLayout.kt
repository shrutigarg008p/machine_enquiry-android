package com.machine.imageslider

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.machine.imageslider.utils.UIUtils
import kotlinx.android.synthetic.main.flipper_layout.view.*

import java.util.*

/**
 * FlipperLayout is a compound layout which consists of a View Pager and a View Pager Indicator
 */
class FlipperLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    ConstraintLayout(context, attrs, defStyleAttr), CircularFlipperHandler.CurrentPageListener {

    init {
        setLayout(context)
    }

    private var flipperLayout: View? = null

    /**
     * Used for auto cycling to keep the count of current page
     */
    private var currentPage = 0

    private lateinit var circularFlipperHandler: CircularFlipperHandler

    /**
     * Flipping View Pager
     */
    private lateinit var mFlippingPager: ViewPager

    /**
     * Flipping Pager Adapter
     */
    private lateinit var mFlippingPagerAdapter: FlipperAdapter

    /**
     * Getter for FlippingPagerAdapter
     */
    private val flippingPagerAdapter: PagerAdapter?
        get() = mFlippingPagerAdapter

    /**
     * Runnable to update the current page
     */
    private val update = Runnable {
        if (currentPage == flippingPagerAdapter!!.count) {
            currentPage = 0
        }
        // true set for smooth transition between pager
        mFlippingPager.setCurrentItem(currentPage, true)
        currentPage += 1
    }

    /**
     * CircleIndicator implementation which Indicates the View Pager
     */
    private var circularPagerIndicator: TabLayout? = null

    /**
     * Scroll Time in seconds
     */
    var scrollTimeInSec = 3
        /**
         * Setting up Scrolling Time for a page
         *
         * @param time in second is sent
         */
        set(time) {
            field = time
            startAutoCycle()
        }

    /**
     * Handler for handling auto cycle
     */
    private val flipperHandler: Handler = Handler()

    /**
     * Scheduler for auto-flipping
     */
    private var flippingTimer: Timer? = null

    /**
     * This returns the current page position of view pager
     */
    val currentPagePosition: Int
        get() = if (flippingPagerAdapter != null) {
            mFlippingPager.currentItem.rem(mFlippingPagerAdapter.count)
        } else {
            throw NullPointerException("Adapter not set")
        }

    constructor(context: Context) : this(context, null, 0) {
        setLayout(context)
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        setLayout(context)
    }

    /**
     * @param widthInDP {int} width of circularPagerIndicator in dp
     */
    fun setCircleIndicatorWidth(widthInDP: Int) {
        val params = circularPagerIndicator!!.layoutParams
        val widthInPixels =
            UIUtils.getInstance(circularPagerIndicator?.context).dpToPixels(widthInDP)
        params.width = widthInPixels
        circularPagerIndicator?.layoutParams = params
    }

    /**
     * @param heightInDP {int} height of circularPagerIndicator in dp
     */
    fun setCircleIndicatorHeight(heightInDP: Int) {
        val params = circularPagerIndicator!!.layoutParams
        val heightInPixels =
            UIUtils.getInstance(circularPagerIndicator?.context).dpToPixels(heightInDP)
        params.height = heightInPixels
        circularPagerIndicator?.layoutParams = params
    }

    /**
     * @param width  {int} width of circularPagerIndicator
     * @param height {int} height of circularPagerIndicator
     */
    fun setCircularIndicatorLayoutParams(width: Int, height: Int) {
        val params = circularPagerIndicator!!.layoutParams
        params.height = height
        params.width = width
        circularPagerIndicator?.layoutParams = params
    }

    /**
     * Method to remove circlePagerIndicator from viewFlipper
     */
    fun removeCircleIndicator() {
        circularPagerIndicator?.visibility = View.GONE
    }

    /**
     * Method to show circlePagerIndicator for viewFlipper
     */
    fun showCircleIndicator() {
        circularPagerIndicator?.visibility = View.VISIBLE
    }

    /**
     * Method to show inner pager indicator rather than an exterior indicator
     */
    fun showInnerPagerIndicator() {
        interiorTabLayout.visibility = VISIBLE
        exteriorTabLayout.visibility = GONE
        circularPagerIndicator = interiorTabLayout
    }

    /**
     * Method to set indicator background drawable using a drawable resource
     * @param drawable {Drawable?} the drawable resource to be used
     */
    fun setIndicatorBackground(drawable: Drawable?) {
        circularPagerIndicator?.background = drawable
    }

    /**
     * Method to set indicator background drawable using a drawable resource id
     * @param drawableRes {Int} the resource Id of the drawable to be used
     */
    fun setIndicatorBackground(@DrawableRes drawableRes: Int) {
        circularPagerIndicator?.background = ContextCompat.getDrawable(context, drawableRes)
    }

    /**
     * Method to set indicator background color
     * @param color {Int} the color to be set as the background
     */
    fun setIndicatorBackgroundColor(@ColorInt color: Int) {
        circularPagerIndicator?.setBackgroundColor(color)
    }

    /**
     * Method to customise FlipperPager as per user's need
     * @param customizeBlock {(ViewPager) -> Unit} a lambda method which exposes the flipper pager to the user
     */
    fun customizeFlipperPager(customizeBlock: (flipperPager: ViewPager) -> Unit) {
        customizeBlock(mFlippingPager)
    }

    /**
     * Method to customise FlipperPager's default circular indicator as per user's need
     * @param customizeBlock {(TabLayout) -> Unit} a lambda method which exposes the flipper pager's circular indicator to the user
     */
    fun customizeCircularIndicator(customizeBlock: (circularIndicator: TabLayout?) -> Unit) {
        check(circularPagerIndicator?.visibility != View.GONE) { "Circular indicator is removed, can't customise it" }
        customizeBlock(circularPagerIndicator)
    }

    /**
     * Use for setting up of FlipperLayout, instantiating view pager, pager indicator
     * and binding the adapter with the view pager
     *
     * @param context for Inflater
     */
    private fun setLayout(context: Context) {
        flipperLayout = LayoutInflater.from(context).inflate(
            R.layout.flipper_layout, this, true
        )
        mFlippingPager = flipperLayout!!.findViewById(R.id.vp_flipper_layout)

        mFlippingPagerAdapter = FlipperAdapter()
        mFlippingPager.adapter = mFlippingPagerAdapter
        exteriorTabLayout.setupWithViewPager(mFlippingPager, true)
        interiorTabLayout.setupWithViewPager(mFlippingPager, true)
        // By default tabLayout will be the circularPagerIndicator
        circularPagerIndicator = exteriorTabLayout

        // Handler for onPageChangeListener
        circularFlipperHandler = CircularFlipperHandler(mFlippingPager)
        circularFlipperHandler.setCurrentPageListener(this)
        mFlippingPager.addOnPageChangeListener(circularFlipperHandler)

        //Starting auto cycle at the time of setting up of layout
        startAutoCycle()
    }

    /**
     * Add Flipper View to the pager adapter
     *
     * @param flipperView is sent as the view to be added to the adapter
     */
    fun addFlipperView(flipperView: FlipperView) {
        flipperView.setViewHeight(flipperLayout!!.layoutParams.height)
        mFlippingPagerAdapter.addFlipperView(flipperView)
        //        pagerIndicator.setViewPager(mFlippingPager);
    }

    /**
     * Add multiple Flipper Views to the pager adapter
     *
     * @param flipperViewList is sent as the list of views to be added to the adapter
     */
    fun addFlipperViewList(flipperViewList: List<FlipperView>) {
        flipperViewList.forEach {
            it.setViewHeight(flipperLayout?.layoutParams?.height!!)
        }
        mFlippingPagerAdapter.addAllFlipperViews(flipperViewList)
        //        pagerIndicator.setViewPager(mFlippingPager);
    }

    /**
     * Method to remove all flipper views from FlipperLayout
     */
    fun removeAllFlipperViews() {
        mFlippingPagerAdapter.removeAllFlipperViews()
    }

    /**
     * Method to start Auto Cycle using Handler, Runnable and Timer
     */
    fun startAutoCycle() {
        removeExistingTimer()
        if (scrollTimeInSec == 0) {
            return
        }
        flippingTimer = Timer()
        flippingTimer!!.schedule(object : TimerTask() {
            override fun run() {
                flipperHandler.post(update)
            }
        }, DELAY_MS, (this.scrollTimeInSec * 1000).toLong())
    }

    /**
     * Method to start Auto Cycle using Handler, Runnable and Timer using the given scroll time
     * @param scrollTime {Int} The scroll delay in seconds (>=0) between each auto sliding
     */
    fun startAutoCycle(scrollTime: Int) {
        require(scrollTime >= 0) { "Scroll time cannot be a negative value" }
        scrollTimeInSec = scrollTime
        removeExistingTimer()
        if (scrollTimeInSec == 0) {
            return
        }
        flippingTimer = Timer()
        flippingTimer!!.schedule(object : TimerTask() {
            override fun run() {
                flipperHandler.post(update)
            }
        }, DELAY_MS, (this.scrollTimeInSec * 1000).toLong())
    }

    /**
     * Method to remove any existing cycling timer of FlipperLayout
     */
    private fun removeExistingTimer() {
        if (flippingTimer == null) {
            return
        }
        flippingTimer!!.cancel()
        flippingTimer!!.purge()
        flippingTimer = null
    }

    /**
     * Method to remove automatic cycling of FlipperLayout
     */
    fun removeAutoCycle() {
        removeExistingTimer()
    }

    override fun onCurrentPageChanged(currentPosition: Int) {
        this.currentPage = currentPosition
    }


    /**
     * @param reverseDrawingOrder true if the supplied PageTransformer requires page views
     * to be drawn from last to first instead of first to last.
     * @param transformer         PageTransformer that will modify each page's animation properties
     * @link {https://developer.android.com/training/animation/screen-slide.html#kotlin}
     * Method to add Page transformer into the Flipper layout
     */
    fun addPageTransformer(
        reverseDrawingOrder: Boolean,
        transformer: ViewPager.PageTransformer?
    ) {
        mFlippingPager.setPageTransformer(reverseDrawingOrder, transformer)
    }

    companion object {

        /**
         * Delay for Timer Task
         */
        private const val DELAY_MS: Long = 500
    }
}
