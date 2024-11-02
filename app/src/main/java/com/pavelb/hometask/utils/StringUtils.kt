package com.pavelb.hometask.utils

import androidx.annotation.StringRes
import com.pavelb.hometask.App

object StringUtils {

    fun getStringSafe(@StringRes stringResId: Int): String =
        App.getInstance().getString(stringResId)

    fun getStringSafe(@StringRes stringResId: Int, vararg formatArgs: Any?): String =
        App.getInstance().getString(stringResId, formatArgs)
}