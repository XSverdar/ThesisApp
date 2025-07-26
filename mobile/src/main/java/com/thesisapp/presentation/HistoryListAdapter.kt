package com.thesisapp.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.thesisapp.R
import com.thesisapp.data.AppDatabase
import com.thesisapp.data.MlResultSummary
import com.thesisapp.data.SessionOnly
import com.thesisapp.utils.animateClick
import java.text.SimpleDateFormat
import java.util.*

class HistoryListAdapter(
    private val sessions: List<SessionOnly>,
    private val onViewDetailsClick: (SessionOnly) -> Unit
) : RecyclerView.Adapter<HistoryListAdapter.SessionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return SessionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val session = sessions[position]
        val context = holder.itemView.context
        val db = AppDatabase.getInstance(context)

        // Fetch first timestamp for this session in background
        Thread {
            val firstTimestampStr = db.swimDataDao().getFirstTimestampForSession(session.sessionId)

            val formattedDate: String
            val formattedTime: String

            if (firstTimestampStr != null) {
                // Parse the string into a Date object
                val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val date = inputFormat.parse(firstTimestampStr.toString())

                val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

                formattedDate = dateFormat.format(date ?: Date())
                formattedTime = timeFormat.format(date ?: Date())
            } else {
                formattedDate = "No Date"
                formattedTime = "No Time"
            }

            // Safely update UI on main thread
            (context as AppCompatActivity).runOnUiThread {
                holder.txtDate.text = formattedDate
                holder.txtTime.text = formattedTime
            }
        }.start()

        holder.btnViewDetails.setOnClickListener {
            it.animateClick()
            onViewDetailsClick(session)
        }
    }

    override fun getItemCount() = sessions.size

    class SessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val txtTime: TextView = itemView.findViewById(R.id.txtTime)
        val btnViewDetails: Button = itemView.findViewById(R.id.btnViewDetails)
    }
}