package com.eap.snackapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Tools {
    companion object {
        private lateinit var loadingDialog: AlertDialog
        lateinit var username: String
        var likes = mutableListOf<String>()
        var dislikes = mutableListOf<String>()
        var currentSnack: Snack? = null

        // Show a loading Dialog
        fun showLoadingDialog(context: Context) {
            val progressBarView =
                LayoutInflater.from(context).inflate(R.layout.view_progress_bar, null)

            loadingDialog = AlertDialog.Builder(context)
                .setView(progressBarView)
                .show()

            loadingDialog.window!!.setLayout(400, 400)
        }

        // Dismiss a loading Dialog
        fun dismissLoadingDialog() {
            loadingDialog.dismiss()
        }


        fun checkFieldFilled(field: String, layout: TextInputLayout, error: String): Boolean {
            if (field.isEmpty()) {
                layout.error = error
                return false
            }
            layout.error = null
            return true
        }

        // Check if an email address is valid and return a Boolean accordingly
        private fun isEmailValid(email: CharSequence): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        // Check if an email address field is valid and set an error if not
        fun checkValidEmailField(email: String, layout: TextInputLayout, error: String): Boolean {
            if (!isEmailValid(email)) {
                layout.error = error
                return false
            }
            layout.error = null
            return true
        }

        // Check if a password field is valid and set an error if not
        fun checkValidPassword(password: String, layout: TextInputLayout, error: String): Boolean {
            if (password.length < 8) {
                layout.error = error
                return false
            }
            layout.error = null
            return true
        }

        fun TextInputEditText.setUneditableField(info: String) {
            setText(info)
            setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus)
                    v.clearFocus()
            }
        }

    }
}

// Function to call in a Fragment to hide the keyboard
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

// Function to call in an Activity to hide the keyboard
fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

// Hide the keyboard
private fun Context.hideKeyboard(view: View) {
    val inputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
