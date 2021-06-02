package com.cerdenia.android.fullcup.ui.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cerdenia.android.fullcup.DAILY
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.WEEKDAY
import com.cerdenia.android.fullcup.WEEKEND
import com.cerdenia.android.fullcup.data.model.Reminder
import com.cerdenia.android.fullcup.util.DateTimeUtils
import com.cerdenia.android.fullcup.util.ext.hide
import com.cerdenia.android.fullcup.util.ext.show

class ReminderAdapter(
    private val resources: Resources,
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
        val reminder = getItem(position)
        holder.bind(reminder)
    }

    inner class ReminderHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val categoryTextView: TextView = view.findViewById(R.id.category_text_view)
        // Following TextViews are nullable since the layout may or may not have them.
        private val timeTextView: TextView? = view.findViewById(R.id.time_text_view)
        private val daysTextView: TextView? = view.findViewById(R.id.days_text_view)
        private val onTextView: TextView? = view.findViewById(R.id.on_text_view)
        private lateinit var reminder: Reminder

        init { itemView.setOnClickListener(this) }

        fun bind(reminder: Reminder) {
            this.reminder = reminder
            categoryTextView.text = reminder.name
            timeTextView?.text = reminder.time?.let { DateTimeUtils.to12HourFormat(it) }
            if (reminder.recurrence == DAILY) onTextView?.hide() else onTextView?.show()
            // Get reminder days from resources instead of
            // directly from object to make it translatable.
            daysTextView?.text = when (reminder.recurrence) {
                WEEKDAY -> resources.getString(R.string.weekdays).uppercase()
                WEEKEND -> resources.getString(R.string.weekends).uppercase()
                DAILY -> resources.getString(R.string.everyday).uppercase()
                else -> null
            }
        }

        override fun onClick(v: View?) {
            listener.onItemSelected(reminder)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Reminder>() {
        override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
            return (oldItem.time == newItem.time && oldItem.recurrence == newItem.recurrence)
        }
    }

    companion object {
        private const val TAG = "ReminderAdapter"
        private const val TYPE_UNSET = 0
        private const val TYPE_SET = 1
    }
}