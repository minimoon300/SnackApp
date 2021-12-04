package com.eap.snackapp

import android.os.Bundle
import android.view.View

interface IMainActivity {

    fun navigate(fragment: Int)
    fun goBack()
    fun setMainSnackbar(message: String, duration: Int)
    fun setMainSnackbar(message: String, duration: Int, actionMessage: String, action: (v: View) -> Unit)
}