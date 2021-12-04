package com.eap.snackapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.eap.snackapp.Tools.Companion.username
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*

class HomeFragment : Fragment(), IHomeFragment {

    private lateinit var mainActivityListener: IMainActivity
    private var snacksList = mutableListOf<Snack>()

    override fun navigate(fragment: Int) {
        mainActivityListener.navigate(fragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Firebase.firestore.collection("users_db").document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
            username = it["NAME"] as String
        }

        FirebaseFirestore.getInstance().collection("snacks_db").get()
            .addOnSuccessListener { snacksQuery ->
                for (document in snacksQuery.documents)
                    snacksList.add(document.toObject(Snack::class.java)!!)
//                dismissLoadingDialog()
//                setReviseFragmentUI()
//                setReviseListeners()
                setSnacksRecycler()
            }
    }

    private fun setSnacksRecycler() {
        snack_recycler.apply {
//            Tools.dismissLoadingDialog()
            layoutManager = GridLayoutManager(context, 3)

            adapter = SnackAdapter(snacksList, this@HomeFragment)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivityListener = context as? IMainActivity
            ?: throw ClassCastException("$context must implement IMainActivity")
    }

    override fun onPause() {
        super.onPause()
        snacksList.clear()
    }

}