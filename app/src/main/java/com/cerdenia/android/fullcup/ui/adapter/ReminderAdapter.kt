package com.cerdenia.android.fullcup.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.ui.text.toUpperCase
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.data.model.Reminder
import com.cerdenia.android.fullcup.util.Utils

class ReminderAdapter(
    private val listener: Listener
): ListAdapter<Reminder, ReminderAdapter.ReminderHolder>(DiffCallback()) {
    interface Listener {
        fun onItemSelected(reminder: Reminder)
    }

    override fun getItemViewType(position: Int): Int {
        val reminder = getItem(position)
        return if (reminder.isSet) TYPE_SET else TYPE_UNSET
    }
    
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReminderHolder {
        val layout = if (viewType == TYPE_SET) {
            R.layout.list_item_reminder_set
        } else {
            R.layout.list_item_reminder_unset
        }

        val view = LayoutInflater
            .from(parent.context)
            .inflate(layout, parent, false)
        return ReminderHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderHolder, position: Int) {
        getItem(position).run { holder.bind(this) }
    }

    inner class ReminderHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val categoryTextView: TextView = view.findViewById(R.id.category_text_view)
        // Following TextViews are nullable since the layout may or may not have them.
        private val timeTextView: TextView? = view.findViewById(R.id.time_text_view)
        private val daysTextView: TextView? = view.findViewById(R.id.days_text_view)
        private lateinit var reminder: Reminder

        init { itemView.setOnClickListener(this) }

        fun bind(reminder: Reminder) {
            this.reminder = reminder
            categoryTextView.text = reminder.category
            timeTextView?.text = reminder.time?.let { Utils.to12HourFormat(it) }
            // TODO:
            // Get reminder.days from resources instead of
            // directly from object to make it translatable.
            daysTextView?.text = reminder.days?.uppercase()
        }

        override fun onClick(v: View?) {
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

    companion object {
        private const val TAG = "ReminderAdapter"
        private const val TYPE_UNSET = 0
        private const val TYPE_SET = 1
    }
}