package com.eap.snackapp

import android.os.Bundle
import android.view.View

interface IMainActivity {

    fun navigate(fragment: Int)
    fun navigate(fragment: Int, arguments: Bundle)
    fun goBack()
    fun showMainFAB()
    fun hideMainFAB()
    fun setMainSnackbar(message: String, duration: Int)
    fun setMainSnackbar(message: String, duration: Int, actionMessage: String, action: (v: View) -> Unit)
}