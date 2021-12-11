package com.eap.snackapp

import android.os.Bundle

interface ILikesFragment {
    fun navigate(fragment: Int)
    fun navigate(fragment: Int, arguments: Bundle)
}