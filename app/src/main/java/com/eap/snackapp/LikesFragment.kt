package com.eap.snackapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eap.snackapp.Tools.Companion.currentSnack
import com.eap.snackapp.Tools.Companion.dislikes
import com.eap.snackapp.Tools.Companion.dismissLoadingDialog
import com.eap.snackapp.Tools.Companion.likes
import com.eap.snackapp.Tools.Companion.showLoadingDialog
import com.eap.snackapp.Tools.Companion.username
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_likes.*
import java.util.*

class LikesFragment : Fragment(), ILikesFragment {

    private lateinit var mainActivityListener: IMainActivity
    private var originalLikedSnacksList = mutableListOf<Snack>()
    private var likedSnacksList = mutableListOf<Snack>()

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
        showLoadingDialog(requireContext())
        return inflater.inflate(R.layout.fragment_likes, container, false)
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
                            if (likes.contains(document.toObject(Snack::class.java)!!.NAME))
                                originalLikedSnacksList.add(document.toObject(Snack::class.java)!!)
                        likedSnacksList.addAll(originalLikedSnacksList)
                        setSnacksRecycler()
                        setSearch()
                    }
                dismissLoadingDialog()
            }
    }

    private fun setSnacksRecycler() {
        liked_snack_recycler?.apply {
            layoutManager = GridLayoutManager(context, 3)

            adapter = LikedSnackAdapter(likedSnacksList, this@LikesFragment)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy != 0) {
                        likes_et_search.clearFocus()
                        hideKeyboard()
                    }
                }
            })
        }
    }

    private fun setSearch() {
        likes_et_search?.doAfterTextChanged {
            if (likes_et_search.text.isNotEmpty()) {
                val size = likedSnacksList.size
                likedSnacksList.clear()
                liked_snack_recycler.adapter?.notifyItemRangeRemoved(0, size)

                likedSnacksList = originalLikedSnacksList.filter {
                    it.SEARCH_KEYWORDS!!.contains(
                        likes_et_search.text.toString().toLowerCase(Locale.FRANCE)
                    )
                }.toMutableList()
                setSnacksRecycler()
            } else {
                val size = likedSnacksList.size
                likedSnacksList.clear()
                liked_snack_recycler.adapter?.notifyItemRangeRemoved(0, size)

                likedSnacksList.addAll(originalLikedSnacksList)
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
        originalLikedSnacksList.clear()
        likedSnacksList.clear()
        likes_et_search.text.clear()
    }

}