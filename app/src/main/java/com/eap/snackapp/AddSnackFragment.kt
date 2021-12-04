package com.eap.snackapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.eap.snackapp.Tools.Companion.dismissLoadingDialog
import com.eap.snackapp.Tools.Companion.showLoadingDialog
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_add_snack.*


class AddSnackFragment : Fragment() {

    private lateinit var mainActivityListener: IMainActivity
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_snack, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        snack_img_btn.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
        }
        add_btn.setOnClickListener {
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference

            showLoadingDialog(requireContext())
            imageUri?.let {
                val snackRef = storageRef.child("${it.lastPathSegment}")
                snackRef.putFile(it).addOnSuccessListener {
                    snackRef.downloadUrl.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            FirebaseFirestore.getInstance().collection("snacks_db").add(
                                hashMapOf(
                                    "NAME" to add_et_snack_name.text.toString().trim()
                                        .ifEmpty { "NAME" },
                                    "IMAGE_LINK" to task.result.toString(),
                                    "WEIGHT" to add_et_snack_weight.text.toString().trim()
                                        .ifEmpty { "0" }.toLong(),
                                    "CALS_PER100g" to add_et_snack_cals.text.toString().trim()
                                        .ifEmpty { "0" }.toLong(),
                                    "KJ_PER100G" to add_et_snack_joules.text.toString().trim()
                                        .ifEmpty { "0" }.toLong(),
                                    "LOCATION" to hashMapOf(
                                        "AFRICA" to add_location_africa.isChecked,
                                        "ASIA" to add_location_asia.isChecked,
                                        "AUSTRALIA" to add_location_australia.isChecked,
                                        "EUROPE" to add_location_europe.isChecked,
                                        "NORTH_AMERICA" to add_location_north_america.isChecked,
                                        "SOUTH_AMERICA" to add_location_south_america.isChecked
                                    ),
                                    "INGREDIENTS" to getSnackIngredients(),
                                    "SEARCH_KEYWORDS" to generateSearchKeywords(
                                        add_et_snack_name.text.toString().trim().ifEmpty { "NAME" }
                                    )
                                )
                            )
                            dismissLoadingDialog()
                            mainActivityListener.setMainSnackbar(
                                getString(R.string.snack_added),
                                Snackbar.LENGTH_SHORT,
                                getString(R.string.ok)
                            ) { }
                            mainActivityListener.goBack()
                        }
                    }
                }
            }
        }
    }

    private fun generateSearchKeywords(snackName: String): MutableList<String> {
        var searchString = snackName
        val keywords = mutableListOf<String>()
        val words = searchString.split(" ")

        for (word in words) {
            var keyword = ""

            for (charPosition in searchString.indices) {
                keyword += searchString[charPosition].toString()
                keywords.add(keyword)
            }
            searchString = searchString.replace("$word ", "")
        }
        return keywords
    }

    private fun getSnackIngredients(): HashMap<String, Int> {
        val ingredients = HashMap<String, Int>()

        when {
            add_et_ingredient_cacao.text.toString().trim()
                .ifEmpty { "0" } != "0" -> ingredients["CACAO"] =
                add_et_ingredient_cacao.text.toString().toInt()
            add_et_ingredient_salt.text.toString().trim()
                .ifEmpty { "0" } != "0" -> ingredients["SALT"] =
                add_et_ingredient_salt.text.toString().toInt()
            add_et_ingredient_sugar.text.toString().trim()
                .ifEmpty { "0" } != "0" -> ingredients["SUGAR"] =
                add_et_ingredient_sugar.text.toString().toInt()
        }

        return ingredients
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == 100) {
            imageUri = data?.data
            snack_img.setImageURI(imageUri)
            snack_img.visibility = View.VISIBLE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivityListener = context as? IMainActivity
            ?: throw ClassCastException("$context must implement IMainActivity")
    }

}