package com.eap.snackapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.view_snack.view.*

class SnackAdapter(private val snacks: List<Snack>, private val homeListener: IHomeFragment) :
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
            is SnackHolder -> holder.bind(snacks[position], homeListener)
        }
    }

    override fun getItemCount(): Int {
        return snacks.size
    }

    class SnackHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(snack: Snack, homeListener: IHomeFragment) {
            Ion.with(view.snack_logo).load(snack.IMAGE_LINK)
        }

    }
}