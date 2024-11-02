package com.pavelb.hometask.presentation.error

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.pavelb.hometask.R
import com.pavelb.hometask.domain.entities.ErrorEntity
import com.pavelb.hometask.domain.entities.ErrorType

class ErrorDialogDelegateImpl() : ErrorDialogDelegate {

    // allows to process different types of errors and show appropriate dialog
    // or handle the error in a different way
    override fun processError(context: Context, errorEntity: ErrorEntity) {
        when (errorEntity.type) {
            ErrorType.NETWORK -> {
                showErrorDialog(
                    context = context,
                    errorMessage = errorEntity.message,
                    positiveBtn = context.getString(R.string.close),
                    negativeBtn = null,
                    onPositiveClick = null,
                    onNegativeClick = null
                )
            }
            ErrorType.ENCRYPTION -> {
                showErrorDialog(
                    context = context,
                    errorMessage = errorEntity.message,
                    positiveBtn = context.getString(R.string.close),
                    negativeBtn = null,
                    onPositiveClick = null,
                    onNegativeClick = null
                )
            }
            ErrorType.AUTH -> {
                showErrorDialog(
                    context = context,
                    errorMessage = errorEntity.message,
                    positiveBtn = context.getString(R.string.close),
                    negativeBtn = null,
                    onPositiveClick = null,
                    onNegativeClick = null
                )
            }
            ErrorType.UNKNOWN -> {
                showErrorDialog(
                    context = context,
                    errorMessage = errorEntity.message,
                    positiveBtn = context.getString(R.string.close),
                    negativeBtn = null,
                    onPositiveClick = null,
                    onNegativeClick = null
                )
            }
        }
    }

    override fun showErrorDialog(
        context: Context,
        errorMessage: String,
        positiveBtn: String,
        negativeBtn: String?,
        onPositiveClick: (() -> Unit)?,
        onNegativeClick: (() -> Unit)?
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.error_title))
        builder.setMessage(errorMessage)
        builder.setPositiveButton(positiveBtn) { dialog, _ ->
            onPositiveClick?.invoke()
            dialog.dismiss()
        }
        negativeBtn?.let {
            builder.setNegativeButton(it) { dialog, _ ->
                onNegativeClick?.invoke()
                dialog.dismiss()
            }
        }
        builder.create().show()
    }
}