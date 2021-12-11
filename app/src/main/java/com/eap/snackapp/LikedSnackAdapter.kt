package com.eap.snackapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eap.snackapp.Tools.Companion.currentSnack
import com.eap.snackapp.Tools.Companion.dismissLoadingDialog
import com.eap.snackapp.Tools.Companion.showLoadingDialog
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.view_snack.view.*

class LikedSnackAdapter(
    private val snacks: List<Snack>,
    private val likesListener: ILikesFragment
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SnackHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_snack,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SnackHolder -> holder.bind(snacks[position], likesListener)
        }
    }

    override fun getItemCount(): Int {
        return snacks.size
    }

    class SnackHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(snack: Snack, likesListener: ILikesFragment) {
            Ion.with(view.snack_logo).load(snack.IMAGE_LINK)
            view.setOnClickListener {
                currentSnack = snack
                likesListener.navigate(R.id.action_likesFragment_to_snackInfoFragment)
                showLoadingDialog(view.context)
            }
        }

    }
}