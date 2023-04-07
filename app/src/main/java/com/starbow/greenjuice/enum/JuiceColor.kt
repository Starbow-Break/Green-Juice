package com.starbow.greenjuice.enum

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.starbow.greenjuice.R
import com.starbow.greenjuice.ui.theme.Green500
import com.starbow.greenjuice.ui.theme.Orange500

enum class JuiceColor(@StringRes val stringRes: Int, @DrawableRes val imgRes: Int, val color: Color) {
    GREEN(stringRes = R.string.green, imgRes = R.drawable.green_juice, color = Green500),
    ORANGE(stringRes = R.string.orange, imgRes = R.drawable.orange_juice, color = Orange500),
    RED(stringRes = R.string.red, imgRes = R.drawable.red_juice, color = Color.Red)
}

