package com.eap.snackapp

import android.os.Bundle

interface IHomeFragment {
    fun navigate(fragment: Int)
    fun navigate(fragment: Int, arguments: Bundle)
}