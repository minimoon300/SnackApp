package com.eap.snackapp

import android.app.AlertDialog
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_likes.*
import java.util.*

class LikesFragment : Fragment(), ILikesFragment {

    private lateinit var mainActivityListener: IMainActivity
    private var originalLikedSnacksList = mutableListOf<Snack>()
    private var likedSnacksList = mutableListOf<Snack>()
    private var sortedLikedSnacksList = mutableListOf<Snack>()
    private var fromAfrica = false
    private var fromAsia = false
    private var fromAustralia = false
    private var fromEurope = false
    private var fromNorthAmerica = false
    private var fromSouthAmerica = false

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
                        setLikesSortListener()
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
                checkSort()
                setSnacksRecycler()
            } else {
                val size = likedSnacksList.size
                likedSnacksList.clear()
                liked_snack_recycler.adapter?.notifyItemRangeRemoved(0, size)

                likedSnacksList.addAll(originalLikedSnacksList)
                checkSort()
                setSnacksRecycler()
            }
        }
    }

    private fun setLikesSortListener() {
        val bundlesSortType =
            arrayOf(
                "Africa",
                "Asia",
                "Australia",
                "Europe",
                "North America",
                "South America"
            )
        val bundlesSortTypeBoolean =
            booleanArrayOf(
                fromAfrica,
                fromAsia,
                fromAustralia,
                fromEurope,
                fromNorthAmerica,
                fromSouthAmerica
            )
        likes_iv_sort?.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.sort)
                .setMultiChoiceItems(
                    bundlesSortType,
                    bundlesSortTypeBoolean
                ) { dialog, which, checked ->
                    when (which) {
                        0 -> fromAfrica = checked
                        1 -> fromAsia = checked
                        2 -> fromAustralia = checked
                        3 -> fromEurope = checked
                        4 -> fromNorthAmerica = checked
                        5 -> fromSouthAmerica = checked
                    }
                }
                .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                    checkSort()
                    setSnacksRecycler()
                }
                .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                    // Respond to neutral button press
                }
                .show()
        }
    }

    private fun checkSort() {
        if (!fromAfrica && !fromAsia && !fromAustralia && !fromEurope && !fromNorthAmerica && !fromSouthAmerica) {
            if (likes_et_search.text.isNotEmpty()) {
                sortedLikedSnacksList = originalLikedSnacksList.filter {
                    it.SEARCH_KEYWORDS!!.contains(
                        likes_et_search.text.toString().toLowerCase(Locale.FRANCE)
                    )
                }.toMutableList()
            } else {
                sortedLikedSnacksList.addAll(originalLikedSnacksList)
            }
        } else {
            if (fromAfrica) {
                sortedLikedSnacksList = likedSnacksList.filter {
                    it.LOCATION!!["AFRICA"]!!
                }.toMutableList()
            }
            if (fromAsia) {
                sortedLikedSnacksList = likedSnacksList.filter {
                    it.LOCATION!!["ASIA"]!!
                }.toMutableList()
            }
            if (fromAustralia) {
                sortedLikedSnacksList = likedSnacksList.filter {
                    it.LOCATION!!["AUSTRALIA"]!!
                }.toMutableList()
            }
            if (fromEurope) {
                sortedLikedSnacksList = likedSnacksList.filter {
                    it.LOCATION!!["EUROPE"]!!
                }.toMutableList()
            }
            if (fromNorthAmerica) {
                sortedLikedSnacksList = likedSnacksList.filter {
                    it.LOCATION!!["NORTH_AMERICA"]!!
                }.toMutableList()
            }
            if (fromSouthAmerica) {
                sortedLikedSnacksList = likedSnacksList.filter {
                    it.LOCATION!!["SOUTH_AMERICA"]!!
                }.toMutableList()
            }
        }

        val size = likedSnacksList.size
        likedSnacksList.clear()
        liked_snack_recycler.adapter?.notifyItemRangeRemoved(0, size)

        likedSnacksList.addAll(sortedLikedSnacksList)
        sortedLikedSnacksList.clear()
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
        sortedLikedSnacksList.clear()
        likes_et_search.text.clear()
    }

}