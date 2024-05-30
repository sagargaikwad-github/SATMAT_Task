package com.example.satmattask.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.satmattask.R

object Utils {

    private var dialog: AlertDialog? = null

    fun showDialog(context: android.content.Context) {
        dialog =
            AlertDialog.Builder(context).setView(R.layout.custom_alertdialog).setCancelable(false)
                .create()
        dialog!!.show()
    }

    fun hideDialog() {
        dialog?.dismiss()
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}