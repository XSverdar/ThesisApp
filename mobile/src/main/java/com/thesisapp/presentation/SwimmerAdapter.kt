package com.thesisapp.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.thesisapp.R

class SwimmerAdapter(
    private val swimmers: List<Swimmer>,
    private val onSwimmerSelected: (Swimmer) -> Unit
) : RecyclerView.Adapter<SwimmerAdapter.SwimmerViewHolder>() {

    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwimmerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_swimmer, parent, false)
        return SwimmerViewHolder(view as ViewGroup)
    }

    override fun onBindViewHolder(holder: SwimmerViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val swimmer = swimmers[position]
        holder.radio.text = swimmer.name
        holder.radio.isChecked = (position == selectedPosition)

        holder.radio.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            onSwimmerSelected(swimmer)
        }
    }

    override fun getItemCount() = swimmers.size

    class SwimmerViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        val radio: RadioButton = itemView.findViewById(R.id.radioSwimmer)
    }
}