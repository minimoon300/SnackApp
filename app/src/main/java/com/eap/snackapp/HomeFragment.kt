package com.eap.snackapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eap.snackapp.Tools.Companion.currentSnack
import com.eap.snackapp.Tools.Companion.dislikes
import com.eap.snackapp.Tools.Companion.dismissLoadingDialog
import com.eap.snackapp.Tools.Companion.likes
import com.eap.snackapp.Tools.Companion.username
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*

class HomeFragment : Fragment(), IHomeFragment {

    private lateinit var mainActivityListener: IMainActivity
    private var originalSnacksList = mutableListOf<Snack>()
    private var snacksList = mutableListOf<Snack>()

    override fun navigate(fragment: Int) {
        mainActivityListener.navigate(fragment)
    }

    override fun navigate(fragment: Int, arguments: Bundle) {
        mainActivityListener.navigate(fragment, arguments)
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

        currentSnack = null
        Firebase.firestore.collection("users_db").document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                username = it["NAME"] as String
                likes = it["LIKES"] as MutableList<String>
                dislikes = it["DISLIKES"] as MutableList<String>
                FirebaseFirestore.getInstance().collection("snacks_db").get()
                    .addOnSuccessListener { snacksQuery ->
                        for (document in snacksQuery.documents)
                            originalSnacksList.add(document.toObject(Snack::class.java)!!)
                        snacksList.addAll(originalSnacksList)
                        setSnacksRecycler()
                        setSearch()
                    }
                dismissLoadingDialog()
            }
    }

    private fun setSnacksRecycler() {
        snack_recycler?.apply {
            layoutManager = GridLayoutManager(context, 3)

            adapter = SnackAdapter(snacksList, this@HomeFragment)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy != 0) {
                        home_et_search.clearFocus()
                        hideKeyboard()
                        when {
                            dy > 0 -> mainActivityListener.hideMainFAB()
                            dy < 0 -> mainActivityListener.showMainFAB()
                        }
                    }
                }
            })
        }
    }

    private fun setSearch() {
        home_et_search?.doAfterTextChanged {
            if (home_et_search.text.isNotEmpty()) {
                val size = snacksList.size
                snacksList.clear()
                snack_recycler.adapter?.notifyItemRangeRemoved(0, size)

                snacksList = originalSnacksList.filter {
                    it.SEARCH_KEYWORDS!!.contains(home_et_search.text.toString().toLowerCase(Locale.FRANCE))
                }.toMutableList()
                setSnacksRecycler()
            } else {
                val size = snacksList.size
                snacksList.clear()
                snack_recycler.adapter?.notifyItemRangeRemoved(0, size)

                snacksList.addAll(originalSnacksList)
                setSnacksRecycler()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivityListener = context as? IMainActivity
            ?: throw ClassCastException("$context must implement IMainActivity")
    }

    override fun onPause() {
        super.onPause()
        originalSnacksList.clear()
        snacksList.clear()
        home_et_search.text.clear()
    }

}