package com.pavelb.hometask.presentation.error

import android.content.Context
import com.pavelb.hometask.R
import com.pavelb.hometask.domain.entities.ErrorEntity

interface ErrorDialogDelegate {

    fun processError(context: Context, error: ErrorEntity)

    fun showErrorDialog(
        context: Context,
        errorMessage: String,
        positiveBtn: String = context.getString(R.string.ok),
        negativeBtn: String? = context.getString(R.string.cancel),
        onPositiveClick: (() -> Unit)?,
        onNegativeClick: (() -> Unit)?
    )
}