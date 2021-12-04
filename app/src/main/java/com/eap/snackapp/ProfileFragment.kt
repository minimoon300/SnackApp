package com.eap.snackapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.eap.snackapp.Tools.Companion.username
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var mainActivityListener: IMainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user_et_username.setUserInfoField(username)

        user_btn_sign_out.setOnClickListener {
            Firebase.auth.signOut()
            mainActivityListener.navigate(R.id.signInFragment)
        }
    }

    private fun TextInputEditText.setUserInfoField(info: String) {
        setText(info)
        setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                v.clearFocus()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivityListener = context as? IMainActivity
            ?: throw ClassCastException("$context must implement IMainActivity")
    }

}