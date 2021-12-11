package com.eap.snackapp

import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.eap.snackapp.Tools.Companion.currentSnack
import com.eap.snackapp.Tools.Companion.dislikes
import com.eap.snackapp.Tools.Companion.dismissLoadingDialog
import com.eap.snackapp.Tools.Companion.likes
import com.eap.snackapp.Tools.Companion.setUneditableField
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.fragment_snack_info.*


class SnackInfoFragment : Fragment() {

    private lateinit var mainActivityListener: IMainActivity
    private lateinit var status: Status

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_snack_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        info_snack_name.text = currentSnack!!.NAME
        Ion.with(info_snack_img).load(currentSnack!!.IMAGE_LINK)
            .done { _, _ -> dismissLoadingDialog() }
        info_et_snack_weight.setUneditableField(currentSnack!!.WEIGHT.toString())
        info_et_snack_cals.setUneditableField(currentSnack!!.CALS_PER100g.toString())
        info_et_snack_joules.setUneditableField(currentSnack!!.KJ_PER100G.toString())

        info_location_africa.isChecked = currentSnack!!.LOCATION!!["AFRICA"]!!
        info_location_asia.isChecked = currentSnack!!.LOCATION!!["ASIA"]!!
        info_location_australia.isChecked = currentSnack!!.LOCATION!!["AUSTRALIA"]!!
        info_location_europe.isChecked = currentSnack!!.LOCATION!!["EUROPE"]!!
        info_location_north_america.isChecked = currentSnack!!.LOCATION!!["NORTH_AMERICA"]!!
        info_location_south_america.isChecked = currentSnack!!.LOCATION!!["SOUTH_AMERICA"]!!

        info_et_ingredient_salt.setUneditableField(currentSnack!!.INGREDIENTS!!["SALT"]?.toString() ?: "0")
        info_et_ingredient_sugar.setUneditableField(currentSnack!!.INGREDIENTS!!["SUGAR"]?.toString() ?: "0")
        info_et_ingredient_water.setUneditableField(currentSnack!!.INGREDIENTS!!["WATER"]?.toString() ?: "0")
        info_et_ingredient_oil_and_fat.setUneditableField(currentSnack!!.INGREDIENTS!!["OIL AND FAT"]?.toString() ?: "0")
        info_et_ingredient_flavouring.setUneditableField(currentSnack!!.INGREDIENTS!!["FLAVOURING"]?.toString() ?: "0")
        info_et_ingredient_dairy.setUneditableField(currentSnack!!.INGREDIENTS!!["DAIRY"]?.toString() ?: "0")
        info_et_ingredient_vegetable_oil_and_fat.setUneditableField(currentSnack!!.INGREDIENTS!!["VEGETABLE OIL AND FAT"]?.toString() ?: "0")
        info_et_ingredient_cereal.setUneditableField(currentSnack!!.INGREDIENTS!!["CEREAL"]?.toString() ?: "0")
        info_et_ingredient_vegetable.setUneditableField(currentSnack!!.INGREDIENTS!!["VEGETABLE"]?.toString() ?: "0")
        info_et_ingredient_fruit.setUneditableField(currentSnack!!.INGREDIENTS!!["FRUIT"]?.toString() ?: "0")
        info_et_ingredient_flour.setUneditableField(currentSnack!!.INGREDIENTS!!["FLOUR"]?.toString() ?: "0")
        info_et_ingredient_wheat.setUneditableField(currentSnack!!.INGREDIENTS!!["WHEAT"]?.toString() ?: "0")
        info_et_ingredient_root_vegetable.setUneditableField(currentSnack!!.INGREDIENTS!!["ROOT VEGETABLE"]?.toString() ?: "0")
        info_et_ingredient_vegetable_oil.setUneditableField(currentSnack!!.INGREDIENTS!!["VEGETABLE OIL"]?.toString() ?: "0")
        info_et_ingredient_glucose.setUneditableField(currentSnack!!.INGREDIENTS!!["GLUCOSE"]?.toString() ?: "0")
        info_et_ingredient_starch.setUneditableField(currentSnack!!.INGREDIENTS!!["STARCH"]?.toString() ?: "0")
        info_et_ingredient_milk.setUneditableField(currentSnack!!.INGREDIENTS!!["MILK"]?.toString() ?: "0")
        info_et_ingredient_natural_flavouring.setUneditableField(currentSnack!!.INGREDIENTS!!["NATURAL FLAVOURING"]?.toString() ?: "0")
        info_et_ingredient_cereal_flour.setUneditableField(currentSnack!!.INGREDIENTS!!["CEREAL FLOUR"]?.toString() ?: "0")
        info_et_ingredient_spice.setUneditableField(currentSnack!!.INGREDIENTS!!["SPICE"]?.toString() ?: "0")

        status = when {
            likes.contains(currentSnack!!.NAME) -> {
                info_thumb_up.setColorFilter(Color.GREEN)
                Status.LIKED
            }
            dislikes.contains(currentSnack!!.NAME) -> {
                info_thumb_down.setColorFilter(Color.RED)
                Status.DISLIKED
            }
            else -> {
                Status.UNRATED
            }
        }

        info_thumb_up.setOnClickListener {
            when (status) {
                Status.UNRATED -> {
                    info_thumb_up.setColorFilter(Color.GREEN)
                    Firebase.firestore.collection("users_db")
                        .document(Firebase.auth.currentUser!!.uid)
                        .update("LIKES", FieldValue.arrayUnion(currentSnack!!.NAME))
                    status = Status.LIKED
                }
                Status.DISLIKED -> {
                    info_thumb_up.setColorFilter(Color.GREEN)
                    info_thumb_down.colorFilter = null
                    Firebase.firestore.collection("users_db")
                        .document(Firebase.auth.currentUser!!.uid)
                        .update("LIKES", FieldValue.arrayUnion(currentSnack!!.NAME))
                    Firebase.firestore.collection("users_db")
                        .document(Firebase.auth.currentUser!!.uid)
                        .update("DISLIKES", FieldValue.arrayRemove(currentSnack!!.NAME))
                    status = Status.LIKED
                }
                Status.LIKED -> {
                }
            }
        }

        info_thumb_down.setOnClickListener {
            when (status) {
                Status.UNRATED -> {
                    info_thumb_down.setColorFilter(Color.RED)
                    Firebase.firestore.collection("users_db")
                        .document(Firebase.auth.currentUser!!.uid)
                        .update("DISLIKES", FieldValue.arrayUnion(currentSnack!!.NAME))
                    status = Status.DISLIKED
                }
                Status.DISLIKED -> {
                }
                Status.LIKED -> {
                    info_thumb_down.setColorFilter(Color.RED)
                    info_thumb_up.colorFilter = null
                    Firebase.firestore.collection("users_db")
                        .document(Firebase.auth.currentUser!!.uid)
                        .update("DISLIKES", FieldValue.arrayUnion(currentSnack!!.NAME))
                    Firebase.firestore.collection("users_db")
                        .document(Firebase.auth.currentUser!!.uid)
                        .update("LIKES", FieldValue.arrayRemove(currentSnack!!.NAME))
                    status = Status.DISLIKED
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivityListener = context as? IMainActivity
            ?: throw ClassCastException("$context must implement IMainActivity")
    }

    enum class Status {
        LIKED, DISLIKED, UNRATED
    }

}