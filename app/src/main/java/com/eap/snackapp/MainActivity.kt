package com.eap.snackapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.NonCancellable.cancel

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    lateinit var imageView: ImageView
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_SnackApp)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.home_fragment_host)
        bottom_nav.setupWithNavController(navController)

        setBottomNavigation()
        setFloatingActionButon()
    }

    private fun setBottomNavigation() {
        bottom_nav.setupWithNavController(navController)
        bottom_nav.setOnItemReselectedListener {}

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.addSnackFragment -> bottom_nav.visibility = View.GONE
                R.id.homeFragment, R.id.likesFragment -> bottom_nav.visibility = View.VISIBLE
            }
        }
    }

    private fun setFloatingActionButon() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.profileFragment, R.id.addSnackFragment -> main_activity_fab.hide()
                R.id.homeFragment, R.id.likesFragment -> main_activity_fab.show()
            }
            main_activity_fab.setOnClickListener {
                navController.navigate(R.id.addSnackFragment)

            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }
    }

}