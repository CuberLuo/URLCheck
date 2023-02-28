package cn.edu.zjut.urlcheck.utils

import android.app.AlertDialog
import android.content.Context

object DialogUtil {
    fun createNormalDialog(context: Context, title: String, msg: String): AlertDialog {
        val alertDialog: AlertDialog =
            AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(msg)
            .setPositiveButton("确认", null)
            .create()
        return alertDialog
    }
}