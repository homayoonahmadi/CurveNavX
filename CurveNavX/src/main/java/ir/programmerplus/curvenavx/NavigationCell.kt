package ir.programmerplus.curvenavx

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.core.view.ViewCompat
import androidx.core.widget.ImageViewCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import ir.programmerplus.curvenavx.databinding.NavigationCellBinding
import kotlin.math.abs


/**
 * This class will create a cell view using xml resource based on RelativeLayout
 */
class NavigationCell @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : RelativeLayout(context, attrs, defStyleAttrs) {

    companion object {
        const val EMPTY = ""
    }

    /**
     * Inflate cell view using viewBinding
     */
    private val binding = NavigationCellBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var defaultIconColor = 0
        set(value) {
            field = value
            if (allowDraw) {
                val colorStateList =
                    ofColorStateList(if (!isEnabledCell) defaultIconColor else selectedIconColor)
                binding.tvTitle.setTextColor(colorStateList)
                ImageViewCompat.setImageTintList(
                    binding.iv,
                    colorStateList
                )
            }
        }

    var selectedIconColor = 0
        set(value) {
            field = value
            if (allowDraw) {
                val colorStateList =
                    ofColorStateList(if (!isEnabledCell) defaultIconColor else selectedIconColor)
                binding.tvTitle.setTextColor(colorStateList)
                ImageViewCompat.setImageTintList(
                    binding.iv,
                    colorStateList
                )
            }
        }

    var circleColor = 0

    var icon = 0
        set(resId) {
            field = resId
            if (allowDraw)
                binding.iv.setImageResource(resId)
        }

    var badgeCount: String? = EMPTY
        set(value) {
            field = value
            if (allowDraw && badgeCount != null) {
                if (badgeCount == EMPTY) {
                    binding.tvCount.text = ""
                    binding.tvCount.visibility = View.INVISIBLE
                } else {
                    if ((badgeCount?.length ?: 0) >= 4) {
                        field = badgeCount?.substring(0, 1) + ".."
                    }
                    binding.tvCount.text = badgeCount
                    binding.tvCount.visibility = View.VISIBLE
                    val scale = if (badgeCount == EMPTY) 0.5f else 1f
                    binding.tvCount.scaleX = scale
                    binding.tvCount.scaleY = scale
                }
            }
        }

    var countTextColor = 0
        set(value) {
            field = value
            if (allowDraw)
                binding.tvCount.setTextColor(field)
        }

    var countBackgroundColor = 0
        set(value) {
            field = value
            if (allowDraw) {
                val d = GradientDrawable()
                d.setColor(field)
                d.shape = GradientDrawable.OVAL
                ViewCompat.setBackground(binding.tvCount, d)
            }
        }

    var countTypeface: Typeface? = null
        set(value) {
            field = value
            if (allowDraw && field != null)
                binding.tvCount.typeface = field
        }

    private var circleTopMargin = 0f
    var bezierInnerHeight = 0
    var bezierShadowHeight = 0

    var isFromLeft = false
    var duration = 0L
    private var progress = 0f
        set(value) {
            field = value
            binding.rl.y = (1f - progress) * (bezierInnerHeight + bezierShadowHeight) +
                    (progress * (circleTopMargin + ((circleSize - iconSize) / 2)))

            val currentColor = ArgbEvaluator().evaluate(progress, defaultIconColor, selectedIconColor) as Int
            val colorStateList = ofColorStateList(currentColor)
            binding.tvTitle.setTextColor(colorStateList)
            ImageViewCompat.setImageTintList(binding.iv, colorStateList)

            val scale = (1f - progress) * (-0.2f) + 1f
            binding.iv.scaleX = scale
            binding.iv.scaleY = scale

            val d = GradientDrawable()
            d.setColor(circleColor)
            d.shape = GradientDrawable.OVAL

            ViewCompat.setBackground(binding.vCircle, d)

            if (Build.VERSION.SDK_INT >= 21)
                ViewCompat.setElevation(
                    binding.vCircle,
                    if (progress > 0.7f) (progress * 4f).dp(context) else 0f
                )


            val m = 24.dp(context)
            binding.vCircle.x = (1f - progress) * (if (isFromLeft) -m else m) +
                    (abs(measuredWidth - circleSize) / 2f)
            binding.vCircle.y = (1f - progress) * measuredHeight + circleTopMargin


        }

    var isEnabledCell = false

    var onClickListener: () -> Unit = {}
        set(value) {
            field = value
            binding.vCircle.setOnClickListener {
                onClickListener()
            }
            binding.rl.setOnClickListener {
                onClickListener()
            }
            binding.tvTitle.setOnClickListener {
                onClickListener()
            }
        }

    var extraPadding = 0
    var iconPadding = 0.dp(context)
        set(value) {
            field = value
            val padding = value + extraPadding
            if (allowDraw)
                binding.iv.setPadding(padding, padding, padding, padding)
        }

    var circleSize = 48.dp(context)
        set(value) {
            field = value
            if (allowDraw)
                binding.vCircle.updateLayoutParams<LayoutParams> {
                    it.width = field
                    it.height = field
                }
        }

    var iconSize = 48.dp(context)
        set(value) {
            field = value
            badgeSize = (value * 0.4f)
            if (allowDraw) {
                binding.iv.updateLayoutParams<LayoutParams> {
                    it.width = field
                    it.height = field
                }
                binding.iv.pivotX = field / 2f
                binding.iv.pivotY = field / 2f
            }
        }

    private var badgeSize = 18f.dp(context)
        set(value) {
            field = value
            val size = value.toInt()
            binding.tvCount.updateLayoutParams<LayoutParams> {
                it.width = size
                it.height = size
                it.leftMargin = size / -2
            }
            binding.iv.pivotX = value / 2f
            binding.iv.pivotY = value / 2f

            badgeTextSize = abs(badgeSize) * 0.65f
        }

    private var badgeTextSize = 18f
        set(value) {
            field = value
            binding.tvCount.setTextSize(TypedValue.COMPLEX_UNIT_PX, badgeTextSize)
        }

    var title = ""
        set(value) {
            field = value
            binding.tvTitle.text = value
        }

    var titleTextSize = 16f
        set(value) {
            field = value
            binding.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, value)
        }

    var titleHeight = LayoutParams.WRAP_CONTENT
        set(value) {
            field = value
            circleTopMargin = value * 0.25f
            binding.tvTitle.updateLayoutParams<LayoutParams> {
                it.height = field
            }
        }

    var cellIsSelected = false
    private var allowDraw = false

    init {
        attrs?.let {
            applyAttributes(context, attrs)
        }
        allowDraw = true
        draw()
    }


    private fun applyAttributes(context: Context, attrs: AttributeSet) {

        val attributes = context.theme.obtainStyledAttributes(
            attrs, R.styleable.BottomNavigationCell, 0, 0
        )

        try {
            with(attributes) {
                getString(R.styleable.BottomNavigationCell_title)?.let { title = it }
                icon = getResourceId(R.styleable.BottomNavigationCell_icon, 0)
                badgeCount = getString(R.styleable.BottomNavigationCell_badgeCount)
                cellIsSelected = getBoolean(R.styleable.BottomNavigationCell_selected, false)
                extraPadding = getDimension(R.styleable.BottomNavigationCell_padding, 0f).toInt()
            }

        } finally {
            attributes.recycle()
        }
    }

    private fun draw() {
        if (!allowDraw)
            return

        icon = icon
        badgeCount = badgeCount
        iconSize = iconSize
        countTextColor = countTextColor
        countBackgroundColor = countBackgroundColor
        countTypeface = countTypeface
        onClickListener = onClickListener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        progress = progress
    }

    fun disableCell(isAnimate: Boolean = true) {
        if (isEnabledCell)
            animateProgress(false, isAnimate)
        isEnabledCell = false
    }

    fun enableCell(isAnimate: Boolean = true) {
        if (!isEnabledCell)
            animateProgress(true, isAnimate)
        isEnabledCell = true
    }

    private fun animateProgress(enableCell: Boolean, isAnimate: Boolean = true) {
        val d = if (enableCell) duration else 250
        val anim = ValueAnimator.ofFloat(0f, 1f)
        anim.apply {
            startDelay = if (enableCell) d / 4 else 0L
            duration = if (isAnimate) d else 1L
            interpolator = FastOutSlowInInterpolator()

            if (isInEditMode) {
                progress(enableCell, 1f)
            } else {
                addUpdateListener {
                    progress(enableCell, it.animatedFraction)
                }
                start()
            }
        }
    }

    private fun progress(enableCell: Boolean, animatedFraction: Float) {
        progress = if (enableCell)
            animatedFraction
        else
            1f - animatedFraction

        if (progress < 0.1f) {
            animateAvd(enableCell)
        }
    }

    private fun animateAvd(enableCell: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return

        val drawable: Drawable = binding.iv.drawable

        if (drawable is AnimatedVectorDrawable) {
            if (enableCell) drawable.start()
            else drawable.stop()
        }
    }

}