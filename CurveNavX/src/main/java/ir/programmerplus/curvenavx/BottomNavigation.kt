@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package ir.programmerplus.curvenavx

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import ir.programmerplus.curvenavx.NavigationCell.Companion.EMPTY
import kotlin.math.abs

/**
 * This class will create a bezier curved bottom navigation view
 * using a cell for storing each icon and its detail and a bezier
 * view for providing curve shaped container and its animation
 */
class BottomNavigation @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : FrameLayout(context, attrs, defStyleAttrs) {

    companion object {
        const val LTR = 0
        const val RTL = 1
    }

    /**
     * Arraylists for storing Items and Cells
     */
    private var items = ArrayList<Item>()
    private var cells = ArrayList<NavigationCell>()

    /**
     * Cell and Bezier views
     */
    private var cellsView: LinearLayout
    private var bezierView: BezierView

    /**
     * Listener holders
     */
    private var onShowListener: IBottomNavigationListener = {}
    private var onItemClickListener: IBottomNavigationListener = {}
    private var onItemReselectListener: IBottomNavigationListener = {}

    /**
     * Helper flags and variables
     */
    private var selectedId = 0
    private var allowDraw = false
    private var isAnimating = false
    var callListenerWhenIsSelected = false

    var animationDuration = 200
        set(value) {
            field = value
            updateViews()
        }

    var defaultIconColor = Color.parseColor("#ffffff")
        set(value) {
            field = value
            updateViews()
        }

    var selectedIconColor = Color.parseColor("#ffffff")
        set(value) {
            field = value
            updateViews()
        }

    var backgroundBottomColor = Color.parseColor("#0095B9")
        set(value) {
            field = value
            updateViews()
        }

    var circleColor = Color.parseColor("#0095B9")
        set(value) {
            field = value
            updateViews()
        }

    private var shadowColor = Color.parseColor("#5f212121")
        set(value) {
            field = value
            updateViews()
        }

    var countTextColor = Color.parseColor("#ffffff")
        set(value) {
            field = value
            updateViews()
        }

    var countBackgroundColor = Color.parseColor("#EF6C00")
        set(value) {
            field = value
            updateViews()
        }

    var countTypeface: Typeface? = null
        set(value) {
            field = value
            updateViews()
        }

    var hasAnimation: Boolean = true
        set(value) {
            field = value
            updateViews()
        }

    // ---------- Cell Sizes In Pixels ----------
    private var cellHeight = 90.dp(context)
        set(value) {
            field = value
            updateViews()
        }

    var circleSize = 48.dp(context)
        set(value) {
            field = value
            updateViews()
        }

    var iconSize = 48.dp(context)
        set(value) {
            field = value
            updateViews()
        }

    var iconPadding = 0.dp(context)
        set(value) {
            field = value
            updateViews()
        }

    var titleTextSize = 16f
        set(value) {
            field = value
            updateViews()
        }

    var titleHeight = RelativeLayout.LayoutParams.WRAP_CONTENT
        set(value) {
            field = value
            titleTextSize = abs(titleHeight) * 0.6f
            updateViews()
        }

    // ---------- Bezier Sizes In Pixels ----------
    var bezierShadowHeight = 8.dp(context)
        set(value) {
            field = value
            updateViews()
        }

    var bezierOuterWidth = 72.dp(context)
        set(value) {
            field = value
            updateViews()
        }

    var bezierOuterHeight = 8.dp(context)
        set(value) {
            field = value
            updateViews()
        }

    var bezierInnerWidth = 124.dp(context)
        set(value) {
            field = value
            updateViews()
        }

    var direction = LTR

    var bezierInnerHeight = 0.dp(context)
        set(value) {
            field = value
            updateViews()
        }

    /**
     * This function will get all attributes set from xml resource and fill
     * its corresponding field in the NavigationView class
     */
    private fun applyAttributes(context: Context, attrs: AttributeSet) {
        val attributes = context.theme.obtainStyledAttributes(
            attrs, R.styleable.BottomNavigation, 0, 0
        )

        try {
            with(attributes) {
                direction = getInt(R.styleable.BottomNavigation_direction, LTR)
                circleColor = getColor(R.styleable.BottomNavigation_circleColor, circleColor)
                shadowColor = getColor(R.styleable.BottomNavigation_shadowColor, shadowColor)
                hasAnimation = getBoolean(R.styleable.BottomNavigation_hasAnimation, hasAnimation)
                countTextColor = getColor(R.styleable.BottomNavigation_countTextColor, countTextColor)
                titleTextSize = getDimension(R.styleable.BottomNavigation_titleTextSize, titleTextSize)
                defaultIconColor = getColor(R.styleable.BottomNavigation_defaultIconColor, defaultIconColor)
                animationDuration = getInt(R.styleable.BottomNavigation_animationDuration, animationDuration)
                selectedIconColor = getColor(R.styleable.BottomNavigation_selectedIconColor, selectedIconColor)
                backgroundBottomColor = getColor(R.styleable.BottomNavigation_backgroundBottomColor, backgroundBottomColor)
                countBackgroundColor = getColor(R.styleable.BottomNavigation_countBackgroundColor, countBackgroundColor)
            }

        } finally {
            attributes.recycle()
        }
    }

    /**
     * Here we initialize cells and bezier views using provided params
     */
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutDirection = LAYOUT_DIRECTION_LTR
        }

        attrs?.let {
            applyAttributes(context, attrs)
        }

        // init cell view
        cellsView = LinearLayout(context)
        cellsView.apply {
            layoutParams = LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                cellHeight
            ).apply {
                gravity = Gravity.BOTTOM
            }
            orientation = LinearLayout.HORIZONTAL
            clipChildren = false
            clipToPadding = false
        }

        // Init bezier view
        bezierView = BezierView(context)
        bezierView.apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                cellHeight
            )
            color = backgroundBottomColor
            shadowColor = this@BottomNavigation.shadowColor
        }

        addView(bezierView)
        addView(cellsView)
        allowDraw = true
    }

    var itemIndex = 0

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        if (!allowDraw) {
            super.addView(child, params)

        } else if (child is NavigationCell) {

            val item = Item(itemIndex, child.title, child.icon, child.extraPadding, child.badgeCount)
            addItem(item)

            if (child.cellIsSelected) selectedId = itemIndex

            itemIndex++
            updateViews()
        }
    }


    /**
     * in overridden onMeasure function we will render the bottom navigation view
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (layoutParams.height > 0)
            cellHeight = layoutParams.height

        bezierShadowHeight = (measuredHeight * 0.24f).toInt()
        bezierInnerHeight = (measuredHeight * 0.06f).toInt()
        titleHeight = bezierShadowHeight

        val size = (cellHeight * 0.6f).toInt()
        circleSize = size
        iconSize = cellHeight - (bezierShadowHeight * 2 + bezierInnerHeight)

        bezierInnerWidth = (circleSize * 2.5f).toInt()

        if (selectedId != -1)
            show(selectedId, false)
    }

    /**
     * This function will create a new cell, sets its params and adds it to the cells array list
     */
    fun addItem(item: Item) {
        val cell = NavigationCell(context)
        cell.apply {
            layoutParams = LinearLayout.LayoutParams(0, cellHeight, 1f)
            icon = item.icon
            badgeCount = item.count
            title = item.title
            extraPadding = item.extraPadding.dp(context)

            circleColor = this@BottomNavigation.circleColor
            countTypeface = this@BottomNavigation.countTypeface
            countTextColor = this@BottomNavigation.countTextColor
            defaultIconColor = this@BottomNavigation.defaultIconColor
            selectedIconColor = this@BottomNavigation.selectedIconColor
            countBackgroundColor = this@BottomNavigation.countBackgroundColor

            iconSize = this@BottomNavigation.iconSize
            circleSize = this@BottomNavigation.circleSize
            titleTextSize = this@BottomNavigation.titleTextSize
            iconPadding = this@BottomNavigation.iconPadding
            bezierInnerHeight = this@BottomNavigation.bezierInnerHeight
            bezierShadowHeight = this@BottomNavigation.bezierShadowHeight

            onClickListener = {
                if (isShowing(item.id))
                    onItemReselectListener(item)

                if (!cell.isEnabledCell && !isAnimating) {
                    show(item.id, hasAnimation)
                    onItemClickListener(item)

                } else if (callListenerWhenIsSelected) {
                    onItemClickListener(item)
                }
            }
            disableCell(hasAnimation)

            if (direction == RTL) {
                cellsView.addView(this, 0)

            } else {
                cellsView.addView(this)
            }
        }

        cells.add(cell)
        items.add(item)
    }

    /**
     * This function will heck if we have initialized the cell and bezier views,
     * then update their parameters with new values
     */
    private fun updateViews() {
        if (!allowDraw)
            return

        cells.forEach {
            it.layoutParams = LinearLayout.LayoutParams(0, cellHeight, 1f)
            it.defaultIconColor = defaultIconColor
            it.selectedIconColor = selectedIconColor
            it.circleColor = circleColor
            it.countTextColor = countTextColor
            it.countBackgroundColor = countBackgroundColor
            it.countTypeface = countTypeface

            it.circleSize = circleSize
            it.iconSize = iconSize
            it.iconPadding = iconPadding
            it.titleHeight = titleHeight
            it.titleTextSize = titleTextSize
            it.bezierInnerHeight = bezierInnerHeight
            it.bezierShadowHeight = bezierShadowHeight
        }

        cellsView.apply {
            layoutParams = LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, cellHeight).apply {
                gravity = Gravity.BOTTOM
            }
        }

        bezierView.apply {
            layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, cellHeight)
            color = this@BottomNavigation.backgroundBottomColor
            shadowHeight = this@BottomNavigation.bezierShadowHeight.toFloat()
            bezierOuterWidth = this@BottomNavigation.bezierOuterWidth.toFloat()
            bezierOuterHeight = this@BottomNavigation.bezierOuterHeight.toFloat()
            bezierInnerWidth = this@BottomNavigation.bezierInnerWidth.toFloat()
            bezierInnerHeight = this@BottomNavigation.bezierInnerHeight.toFloat()
        }
    }

    /**
     * This function will move bezier view to new position and animate its view if animations is set to enabled
     */
    private fun animateBezierView(
        cell: NavigationCell,
        itemId: Int,
        enabledAnimation: Boolean = true
    ) {
        isAnimating = true

        val destinationPosition = getItemPosition(itemId)
        val currentPosition = getItemPosition(selectedId)
        val selectedPosition = if (direction == LTR) {
            currentPosition
        } else items.lastIndex - currentPosition

        val currentPos = if (currentPosition < 0) 0 else currentPosition
        val difference = abs(destinationPosition - currentPos)
        val calculatedDuration = (difference) * 100L + animationDuration

        val animDuration = if (enabledAnimation && hasAnimation) calculatedDuration else 1L
        val animInterpolator = FastOutSlowInInterpolator()

        val anim = ValueAnimator.ofFloat(0f, 1f)
        anim.apply {
            duration = animDuration
            interpolator = animInterpolator

            val beforeX = bezierView.bezierX
            if (isInEditMode) {
                moveBezierView(cell, beforeX, 1f, selectedPosition)
            } else {
                addUpdateListener {
                    moveBezierView(cell, beforeX, it.animatedFraction)
                }
                start()
            }
        }

        if (abs(destinationPosition - currentPosition) > 1) {
            val progressAnim = ValueAnimator.ofFloat(0f, 1f)
            progressAnim.apply {
                duration = animDuration
                interpolator = animInterpolator

                if (isInEditMode) {
                    progressBezierView(1f)
                } else {
                    addUpdateListener {
                        progressBezierView(it.animatedFraction)
                    }
                    start()
                }
            }
        }

        cell.isFromLeft = destinationPosition > currentPosition
        cells.forEach {
            it.duration = calculatedDuration
        }
    }

    private fun moveBezierView(
        cell: NavigationCell,
        beforeX: Float,
        animatedFraction: Float,
        selectedPosition: Int = 0
    ) {
        var newX = cell.x + (cell.measuredWidth / 2)
        if (isInEditMode) newX += selectedPosition * cell.measuredWidth

        if (newX > beforeX)
            bezierView.bezierX = beforeX + animatedFraction * (newX - beforeX)
        else
            bezierView.bezierX = beforeX - animatedFraction * (beforeX - newX)

        if (animatedFraction == 1f)
            isAnimating = false
    }

    private fun progressBezierView(animatedFraction: Float) {
        bezierView.progress = animatedFraction * 2f
    }

    /**
     * This function renders and shows all items inside the list and if the item is selected then shows that item
     * with an animation if has enabled, else the item will be shows as disabled
     */
    @Synchronized
    fun show(itemId: Int, enabledAnimation: Boolean = true) {
        for (i in items.indices) {
            val item = items[i]
            val cell = cells[i]

            if (item.id == itemId) {
                animateBezierView(cell, itemId, enabledAnimation)
                cell.enableCell(enabledAnimation)
                onShowListener(item)
            } else {
                cell.disableCell(enabledAnimation)
            }
        }
        selectedId = itemId
    }


    // ------------------ Helper Functions -------------------------

    fun isShowing(itemId: Int): Boolean {
        return selectedId == itemId
    }

    fun getItemById(itemId: Int): Item? {
        items.forEach {
            if (it.id == itemId)
                return it
        }
        return null
    }

    fun getCellById(itemId: Int): NavigationCell {
        return cells[getItemPosition(itemId)]
    }

    fun getItemPosition(itemId: Int): Int {
        for (i in items.indices) {
            val item = items[i]
            if (item.id == itemId)
                return i
        }
        return -1
    }

    fun setBadgeCount(itemId: Int, count: String) {
        val item = getItemById(itemId) ?: return
        val pos = getItemPosition(itemId)
        item.count = count
        cells[pos].badgeCount = count
    }

    fun getBadgeCount(itemId: Int): String {
        val item = getItemById(itemId) ?: return EMPTY
        return item.count
    }

    fun getItemCount(itemId: Int): Int {
        val item = getItemById(itemId) ?: return 0

        return if (item.count == EMPTY)
            0
        else
            item.count.toInt()
    }

    fun clearCount(itemId: Int) {
        if (getItemCount(itemId) != 0) {
            val model = getItemById(itemId) ?: return
            val position = getItemPosition(itemId)
            model.count = EMPTY
            cells[position].badgeCount = EMPTY
        }
    }

    fun clearCountDelayed(itemId: Int, delayMillis: Long) {
        Handler(Looper.getMainLooper()).postDelayed({
            clearCount(itemId)
        }, delayMillis)
    }

    fun clearBadgeCounts() {
        items.forEach {
            clearCount(it.id)
        }
    }

    // -------------------- Listener Setters -----------------------

    fun setOnShowListener(listener: IBottomNavigationListener) {
        onShowListener = listener
    }

    fun setOnItemClickListener(listener: IBottomNavigationListener) {
        onItemClickListener = listener
    }

    fun setOnItemReselectListener(listener: IBottomNavigationListener) {
        onItemReselectListener = listener
    }

}