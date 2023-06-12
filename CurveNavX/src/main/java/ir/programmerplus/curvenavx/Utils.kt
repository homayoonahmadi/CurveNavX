package ir.programmerplus.curvenavx

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.ColorInt

private var dp = 0f

@Synchronized
private fun getDp(context: Context): Float {
    if (dp == 0f) {
        dp = context.resources.displayMetrics.density
    }

    return dp
}

internal fun Float.dp(context: Context) = this * getDp(context)
internal fun Int.dp(context: Context) = (this * getDp(context)).toInt()

internal fun ofColorStateList(@ColorInt color: Int) = ColorStateList.valueOf(color)


fun <T> View?.updateLayoutParams(onLayoutChange: (params: T) -> Unit) {
    if (this == null)
        return

    try {
        @Suppress("UNCHECKED_CAST")
        onLayoutChange(layoutParams as T)
        layoutParams = layoutParams
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
