package com.eap.snackapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.NonCancellable.cancel

class MainActivity : AppCompatActivity(), IMainActivity {

    override fun navigate(fragment: Int) {
        navController.navigate(fragment)
    }

    override fun navigate(fragment: Int, arguments: Bundle) {
        navController.navigate(fragment, arguments)
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun setMainSnackbar(message: String, duration: Int) {
        mainSnackbar = Snackbar
            .make(main_activity_coordinator, message, duration)
        mainSnackbar!!.show()
    }

    override fun showMainFAB() {
        main_activity_fab.show()
    }

    override fun hideMainFAB() {
        main_activity_fab.hide()
    }

    override fun setMainSnackbar(
        message: String,
        duration: Int,
        actionMessage: String,
        action: (v: View) -> Unit
    ) {
        mainSnackbar = Snackbar
            .make(main_activity_coordinator, message, duration)
            .setAction(actionMessage, action)
        mainSnackbar!!.show()
    }

    private lateinit var navController: NavController
    private var mainSnackbar: Snackbar? = null

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
        setSoftInputMode()
    }

    private fun setBottomNavigation() {
        bottom_nav.setupWithNavController(navController)
        bottom_nav.setOnItemReselectedListener {}

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.signInFragment, R.id.addSnackFragment, R.id.snackInfoFragment -> bottom_nav.visibility =
                    View.GONE
                R.id.homeFragment, R.id.likesFragment -> bottom_nav.visibility = View.VISIBLE
            }
        }
    }

    private fun setFloatingActionButon() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.signInFragment, R.id.signUpFragment, R.id.likesFragment, R.id.profileFragment, R.id.addSnackFragment, R.id.snackInfoFragment -> main_activity_fab.hide()
                R.id.homeFragment -> main_activity_fab.show()
            }
            main_activity_fab.setOnClickListener {
                navController.navigate(R.id.addSnackFragment)
            }
        }

    }

    private fun setSoftInputMode() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.signInFragment -> window.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                )
                R.id.homeFragment -> window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
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