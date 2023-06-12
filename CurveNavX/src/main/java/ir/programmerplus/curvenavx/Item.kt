package ir.programmerplus.curvenavx

import androidx.annotation.DrawableRes

/**
 * This class will hold all cell's information like icon resource id
 */
class Item @JvmOverloads constructor(
    var id: Int,
    var title: String,
    @DrawableRes var icon: Int,
    var extraPadding: Int = 0,
    count: String?
) {
    var count: String = if (count.isNullOrEmpty()) NavigationCell.EMPTY else count
}