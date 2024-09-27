package com.crunchquest.android.presentation.ui.dialogs

import android.app.Activity
import android.app.AlertDialog
import com.crunchquest.shared.R

class LoadingDialog(private val activity: Activity, private val message: String) {
    private lateinit var dialog: AlertDialog

    fun startLoadingAnimation() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.dialog_custom, null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }
}