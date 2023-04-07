package com.starbow.greenjuice.enum

import androidx.annotation.StringRes
import com.starbow.greenjuice.R

enum class GreenJuiceTheme(@StringRes val stringRes: Int, val value: Int) {
    LIGHT(stringRes = R.string.light_theme, value = 0),
    DARK(stringRes = R.string.dark_theme, value = 1),
    SYSTEM(stringRes = R.string.system_theme, value = 2);

    companion object {
        fun fromValue(value: Int): GreenJuiceTheme
            = values().first { it.value == value }
    }
}