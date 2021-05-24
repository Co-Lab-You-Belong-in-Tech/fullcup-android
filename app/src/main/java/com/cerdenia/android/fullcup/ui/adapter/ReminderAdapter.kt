package com.cerdenia.android.fullcup.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.data.model.Reminder

private const val TAG = "ReminderAdapter"

class ReminderAdapter(
    private val listener: Listener
): ListAdapter<Reminder, ReminderAdapter.ReminderHolder>(DiffCallback()) {
    interface Listener {
        fun onItemSelected(reminder: Reminder)
    }
    
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReminderHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_reminder, parent, false)
        return ReminderHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderHolder, position: Int) {
        getItem(position).run { holder.bind(this) }
    }

    inner class ReminderHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val categoryTextView = view.findViewById<TextView>(R.id.category_text_view)
        private lateinit var reminder: Reminder

        init { itemView.setOnClickListener(this) }

        fun bind(reminder: Reminder) {
            this.reminder = reminder
            categoryTextView.text = reminder.category
        }

        override fun onClick(v: View?) {
            Log.i(TAG, "i r pushed")
            listener.onItemSelected(reminder)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Reminder>() {
        override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
            return oldItem.category == newItem.category
        }

        override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
            return oldItem == newItem
        }
    }
}