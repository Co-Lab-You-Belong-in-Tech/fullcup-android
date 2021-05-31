package com.cerdenia.android.fullcup.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cerdenia.android.fullcup.data.model.DailyLog
import com.cerdenia.android.fullcup.data.model.SelectableActivity
import com.cerdenia.android.fullcup.databinding.FragmentLogActivityBinding
import com.cerdenia.android.fullcup.ui.adapter.ActivityAdapter
import com.cerdenia.android.fullcup.util.ext.toEditable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LogActivityFragment : BottomSheetDialogFragment(), ActivityAdapter.Listener {
    private lateinit var binding: FragmentLogActivityBinding
    private var dailyLog: DailyLog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dailyLog = arguments?.getSerializable(DAILY_LOG) as DailyLog?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogActivityBinding
            .inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Populate category list.
        val selectableActivities = dailyLog?.activities
            ?.map { activity -> SelectableActivity(activity.name, activity.isDone) }
            ?: emptyList()
        binding.recyclerView.adapter = ActivityAdapter(selectableActivities, this)

        binding.editText.text = dailyLog?.summary?.content.toEditable()

        binding.doneButton.setOnClickListener {
            // Save user-inputted text to DailyLog object.
            dailyLog?.summary?.content = binding.editText.text.toString()
            parentFragmentManager.setFragmentResult(LOG_ACTIVITY, Bundle().apply {
                putSerializable(DAILY_LOG, dailyLog)
            })
            dismiss()
        }
    }

    override fun onCheckboxItemClicked(activity: String, isChecked: Boolean) {
        // Find item in DailyLog object and update isDone value.
        dailyLog?.let { dailyLog ->
            val i = dailyLog.activities.indexOfFirst { it.name == activity }
            dailyLog.activities[i].isDone = isChecked
            Log.i(TAG, "Item changed: ${dailyLog.activities[i]}")
        }
    }

    companion object {
        const val TAG = "LogActivityFragment"
        const val DAILY_LOG = "DAILY_LOG"
        const val LOG_ACTIVITY = "LOG_ACTIVITY"

        fun newInstance(dailyLog: DailyLog): LogActivityFragment {
            return LogActivityFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(DAILY_LOG, dailyLog)
                }
            }
        }
    }
}