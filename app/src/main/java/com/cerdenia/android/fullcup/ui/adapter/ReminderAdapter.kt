package com.cerdenia.android.fullcup.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.data.model.Reminder

class ReminderAdapter(
    private val listener: Listener
): ListAdapter<Reminder, ReminderAdapter.ReminderHolder>(DiffCallback()) {
    interface Listener {
        fun onItemSelected()
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
        holder.bind(getItem(position))
    }

    inner class ReminderHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(reminder: Reminder) {
            
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