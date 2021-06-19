package com.cerdenia.android.fullcup.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.data.model.ActivityLog
import com.cerdenia.android.fullcup.data.model.ColoredActivity
import com.cerdenia.android.fullcup.data.model.SelectableActivity
import com.cerdenia.android.fullcup.util.DateTimeUtils
import com.cerdenia.android.fullcup.util.ext.qualified
import com.google.android.material.imageview.ShapeableImageView

class ActivityAdapter(
    var activities: List<Any>,
    private val listener: Listener? = null
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    interface Listener {
        fun onCheckboxItemClicked(activity: String, isChecked: Boolean)
    }

    override fun getItemViewType(position: Int): Int {
        return when (activities[position]) {
            is ColoredActivity -> TYPE_COLORED
            is SelectableActivity -> TYPE_CHECKBOX
            is ActivityLog -> TYPE_ACTIVITY_LOG
            else -> throw Exception("Unexpected item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_COLORED -> {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.list_item_colored_activity, parent, false)
                ColoredHolder(view)
            }
            TYPE_CHECKBOX -> {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.list_item_activity_checkbox, parent, false)
                CheckboxHolder(view)
            }
            TYPE_ACTIVITY_LOG -> {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.list_item_activity_log, parent, false)
                ActivityLogHolder(view)
            }
            else -> throw Exception("Unexpected item type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val activity = activities[position]
        when (holder) {
            is ColoredHolder -> holder.bind(activity as ColoredActivity)
            is CheckboxHolder -> holder.bind(activity as SelectableActivity)
            is ActivityLogHolder -> holder.bind(activity as ActivityLog)
            else -> throw Exception("Unexpected item type")
        }
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    private inner class ColoredHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView? = view.findViewById(R.id.text_view)
        val colorView: ShapeableImageView? = view.findViewById(R.id.color_view)

        fun bind(coloredActivity: ColoredActivity) {
            textView?.text = coloredActivity.activity.qualified()
            colorView?.setBackgroundColor(coloredActivity.color)
        }
    }

    private inner class CheckboxHolder(view: View) :
        RecyclerView.ViewHolder(view),
        View.OnClickListener
    {
        private lateinit var activity: String
        val checkBox: CheckBox? = view.findViewById(R.id.check_box)

        init { checkBox?.setOnClickListener(this) }

        fun bind(selectableActivity: SelectableActivity) {
            activity = selectableActivity.activity
            checkBox?.text = activity.qualified()
            checkBox?.isChecked = selectableActivity.isSelected
        }

        override fun onClick(p0: View?) {
            val isChecked = checkBox?.isChecked ?: false
            listener?.onCheckboxItemClicked(activity, isChecked)
        }
    }

    private inner class ActivityLogHolder(view: View) :
        RecyclerView.ViewHolder(view),
        View.OnClickListener
    {
        private lateinit var activityLog: ActivityLog
        private val activityTextView: TextView? = view.findViewById(R.id.activity_text_view)
        private val timeTextView: TextView? = view.findViewById(R.id.time_text_view)

        init { itemView.setOnClickListener(this) }

        fun bind(activityLog: ActivityLog) {
            this.activityLog = activityLog
            activityTextView?.text = activityLog.name
            timeTextView?.text = DateTimeUtils.to12HourFormat(activityLog.time)
        }

        override fun onClick(p0: View?) {
            // TODO
        }
    }

    companion object {
        private const val TAG = "ActivityAdapter"
        private const val TYPE_COLORED = 0
        private const val TYPE_CHECKBOX = 1
        private const val TYPE_ACTIVITY_LOG = 2
    }
}