package com.cerdenia.android.fullcup.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.cerdenia.android.fullcup.DAILY
import com.cerdenia.android.fullcup.WEEKDAY
import com.cerdenia.android.fullcup.WEEKEND
import com.cerdenia.android.fullcup.data.model.Reminder
import com.cerdenia.android.fullcup.databinding.FragmentSetReminderBinding
import com.cerdenia.android.fullcup.util.DateTimeUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SetReminderFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentSetReminderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val reminder = arguments?.getSerializable(REMINDER) as Reminder
        val times = arguments?.getStringArray(AVAILABLE_TIMES) ?: emptyArray()
        Log.d(TAG, "Got reminder: $reminder, and times: $times")

        val buttonMap = mapOf(
            binding.weekdaysButton.id to WEEKDAY,
            binding.weekendsButton.id to WEEKEND,
            binding.everydayButton.id to DAILY
        )

        binding.toggleGroup.apply {
            reminder.days?.let { check(getKey(buttonMap, it)) }
            addOnButtonCheckedListener { _, checkedId, _ ->
                reminder.days = buttonMap[checkedId]
            }
        }

        binding.timeSpinner.apply {
            val formattedTimes = times.map { DateTimeUtils.to12HourFormat(it) }
            adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                formattedTimes
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    reminder.time = times[p2]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                   // Do nothing
                }
            }

            val i = times.indexOf(reminder.time)
            setSelection(i) // Initial state.
        }

        binding.confirmButton.setOnClickListener {
            parentFragmentManager.setFragmentResult(KEY_CONFIRM, Bundle().apply {
                putSerializable(REMINDER, reminder)
            })
            dismiss()
        }
    }

    private fun getKey(map: Map<Int, String>, value: String): Int {
        map.forEach { pair -> if (pair.value == value) return pair.key }
        return 0
    }

    companion object {
        const val TAG = "SetReminderFragment"
        const val KEY_CONFIRM = "KEY_CONFIRM"
        const val REMINDER = "REMINDER"
        const val AVAILABLE_TIMES = "AVAILABLE_TIMES"

        fun newInstance(reminder: Reminder, times: Array<String>): SetReminderFragment {
            return SetReminderFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(REMINDER, reminder)
                    putStringArray(AVAILABLE_TIMES, times)
                }
            }
        }
    }
}