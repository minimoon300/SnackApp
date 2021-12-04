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
import com.eap.snackapp.Tools.Companion.checkValidPassword
import com.eap.snackapp.Tools.Companion.dismissLoadingDialog
import com.eap.snackapp.Tools.Companion.showLoadingDialog
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_sign_up.*
import java.util.*

private const val TAG = "SignUpFragment"

class SignUpFragment : Fragment() {

    private lateinit var mainActivityListener: IMainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUp()
    }

    private fun signUp() {
        sign_up_btn_sign_up.setOnClickListener {
            val username = sign_up_et_username.text.toString().trim()
            val email = sign_up_et_email.text.toString()
            val password = sign_up_et_password.text.toString()

            if (checkValidSignUpForm(username, email, password)) {
                showLoadingDialog(requireContext())
                hideKeyboard()
                createUser(username, email, password)
            }
        }
    }

    private fun checkValidSignUpForm(username: String, email: String, password: String): Boolean {
        return checkFieldFilled(
            field = username,
            layout = sign_up_layout_username,
            error = getString(R.string.field_empty_error)
        ) && checkValidEmailField(
            email = email,
            layout = sign_up_layout_email,
            error = getString(R.string.field_email_error)
        ) && checkValidPassword(
            password = password,
            layout = sign_up_layout_password,
            error = getString((R.string.field_password_error))
        )
    }

    private fun createUser(username: String, email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                dismissLoadingDialog()
                if (task.isSuccessful) {
                    val user = hashMapOf(
                        "NAME" to username,
                        "DISLIKES" to mutableListOf<String>(),
                        "LIKES" to mutableListOf<String>()
                    )

                    Firebase.firestore.collection("users_db").document(Firebase.auth.currentUser!!.uid).set(user)
                    mainActivityListener.navigate(R.id.action_signUpFragment_to_homeFragment)
                    Log.d(TAG, "createUserWithEmail:success")
                } else {
                    Toast.makeText(
                        context, getString(R.string.email_already_used_message),
                        Toast.LENGTH_LONG
                    ).show()
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivityListener = context as? IMainActivity
            ?: throw ClassCastException("$context must implement IMainActivity")
    }

}
