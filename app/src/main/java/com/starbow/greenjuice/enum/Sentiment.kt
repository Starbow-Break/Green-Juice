package com.starbow.greenjuice.enum

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.starbow.greenjuice.R
import com.starbow.greenjuice.ui.theme.Blue700
import com.starbow.greenjuice.ui.theme.Green500

enum class Sentiment(@StringRes val stringRes: Int, val color: Color) {
    POSITIVE(stringRes = R.string.positive, color = Blue700),
    NEUTRAL(stringRes = R.string.neutrality, color = Green500),
    NEGATIVE(stringRes = R.string.negative, color = Color.Red)
}