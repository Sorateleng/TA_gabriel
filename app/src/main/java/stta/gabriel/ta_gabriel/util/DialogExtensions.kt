package stta.gabriel.ta_gabriel.util

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun Context.showDialog(
    cancelable: Boolean = true,
    cancelableTouchOutside: Boolean = true,
    builderFunction: AlertDialog.Builder.() -> Any
) {
    val builder = AlertDialog.Builder(this)
    builder.builderFunction()
    val dialog = builder.create();

    dialog.setCancelable(cancelable)
    dialog.setCanceledOnTouchOutside(cancelableTouchOutside)
    dialog.show()
}


fun AlertDialog.Builder.positiveButton(text: String = "OK", handleClick: (i: Int) -> Unit = {}) {
    this.setPositiveButton(text) { dialogInterface, i ->
        run {
            handleClick(i)
            dialogInterface.dismiss()
        }
    }
}

fun AlertDialog.Builder.negativeButton(
    text: String = "CANCEL",
    handleClick: (i: Int) -> Unit = {}
) {
    this.setNegativeButton(text) { dialogInterface, i ->
        run {
            handleClick(i)
            dialogInterface.dismiss()
        }
    }
}

fun AlertDialog.Builder.neutralButton(text: String, handleClick: (i: Int) -> Unit = {}) {
    this.setNeutralButton(text) { dialogInterface, i ->
        handleClick(i)
        dialogInterface.dismiss()
    }
}