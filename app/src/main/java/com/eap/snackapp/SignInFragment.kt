package com.eap.snackapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.eap.snackapp.Tools.Companion.checkFieldFilled
import com.eap.snackapp.Tools.Companion.checkValidEmailField
import com.eap.snackapp.Tools.Companion.dismissLoadingDialog
import com.eap.snackapp.Tools.Companion.showLoadingDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_sign_in.*

private const val TAG = "SignInFragment"

class SignInFragment : Fragment() {

    private lateinit var mainActivityListener: IMainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signIn()
        navigateToSignUp()
    }

    private fun signIn() {
        sign_in_btn_sign_in.setOnClickListener {
            val email = sign_in_et_email.text.toString()
            val password = sign_in_et_password.text.toString()

            if (checkValidSignInForm(email, password)) {
                showLoadingDialog(requireContext())
                hideKeyboard()
                Firebase.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        dismissLoadingDialog()
                        if (task.isSuccessful) {
                            Log.d(TAG, "signInWithEmail:success")
                            mainActivityListener.navigate(R.id.action_signInFragment_to_homeFragment)
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                context, R.string.sign_in_error_incorrect_login,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }
    }

    private fun checkValidSignInForm(email: String, password: String): Boolean {
        return checkValidEmailField(
            email = email,
            layout = sign_in_layout_email,
            error = getString(R.string.field_email_error)
        ) && checkFieldFilled(
            field = password,
            layout = sign_in_layout_password,
            error = getString(R.string.field_empty_error)
        )
    }

    private fun navigateToSignUp() {
        sign_in_btn_create_account.setOnClickListener {
            mainActivityListener.navigate(R.id.signUpFragment)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivityListener = context as? IMainActivity
            ?: throw ClassCastException("$context must implement IMainActivity")
    }

    override fun onStart() {
        super.onStart()
        checkAlreadySignIn()
    }

    private fun checkAlreadySignIn() {
        Firebase.auth.currentUser?.let {
            mainActivityListener.navigate(R.id.homeFragment)
        }
    }

}