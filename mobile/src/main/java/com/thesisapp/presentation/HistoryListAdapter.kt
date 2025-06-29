package com.thesisapp.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thesisapp.R
import com.thesisapp.utils.animateClick

class HistoryListAdapter(
    private val sessions: List<Session>,
    private val onViewDetailsClick: (session: Session) -> Unit
) : RecyclerView.Adapter<HistoryListAdapter.SessionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return SessionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val session = sessions[position]
        holder.tvDate.text = session.date
        holder.tvTime.text = "Time: ${session.time}"
        holder.btnViewDetails.setOnClickListener {
            it.animateClick()
            onViewDetailsClick(session)
        }
    }

    override fun getItemCount() = sessions.size

    class SessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val btnViewDetails: Button = itemView.findViewById(R.id.btnViewDetails)
    }
}