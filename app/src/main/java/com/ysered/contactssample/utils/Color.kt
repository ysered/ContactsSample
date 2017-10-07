@file:JvmName("ColorUtils")

package com.ysered.contactssample.utils

import android.graphics.Color
import java.util.*

private val random = Random()
private val materialColors = arrayOf(
        "#F44336", // red
        "#E91E63", // pink
        "#9C27B0", // purple
        "#673AB7", // deep purple
        "#3F51B5", // indigo
        "#2196F3", // blue
        "#03A9F4", // light blue
        "#00BCD4", // cyan
        "#009688", // teal
        "#4CAF50", // green
        "#8BC34A", // light green
        "#CDDC39", // lime
        "#FFEB3B", // yellow
        "#FFC107", // amber
        "#FF9800", // orange
        "#FF5722", // deep orange
        "#795548", // brown
        "#9E9E9E", // grey
        "#607D8B", // blue grey
        "#000000"  // black
)

fun getRandomMaterialColor(): Int {
    val index = random.nextInt(materialColors.size - 1)
    return Color.parseColor(materialColors[index])
}
