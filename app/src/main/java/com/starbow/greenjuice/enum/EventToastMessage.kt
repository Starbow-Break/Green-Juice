package com.starbow.greenjuice.enum

import androidx.annotation.StringRes
import com.starbow.greenjuice.R

enum class EventToastMessage(@StringRes val messageRes: Int) {
    SIGN_IN(messageRes = R.string.sign_in_success),
    SIGN_IN_FAIL(messageRes = R.string.sign_in_fail),
    SIGN_OUT(messageRes = R.string.sign_out_success),
    SIGN_UP(messageRes = R.string.sign_up_success),
    NETWORK_ERROR(messageRes = R.string.network_error),
    LOAD_DATA_ERROR(messageRes = R.string.load_data_error),
    SIGN_IN_ERROR(messageRes = R.string.sign_in_error),
    SIGN_OUT_ERROR(messageRes = R.string.sign_out_error),
    SIGN_UP_ERROR(messageRes = R.string.sign_up_error),
    ADD_FAV_ERROR(messageRes = R.string.add_favorites_error),
    DELETE_FAV_ERROR(messageRes = R.string.delete_favorites_error),
    VALID_ID(messageRes = R.string.valid_id),
    DUPLICATION_ID(messageRes = R.string.duplicated_id),
    REFUSE(messageRes = R.string.refuse)
}