package com.cerdenia.android.fullcup.ui.dialog

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.cerdenia.android.fullcup.data.model.Reminder
import com.cerdenia.android.fullcup.databinding.FragmentSetReminderBinding
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
        val reminder = arguments?.getSerializable(ARG_REMINDER) as Reminder
        val times = arguments?.getStringArray(ARG_TIMES) ?: emptyArray()

        binding.weekdaysButton.setOnClickListener { reminder.days = "weekdays" }
        binding.weekendsButton.setOnClickListener { reminder.days = "weekends" }
        binding.everydayButton.setOnClickListener { reminder.days = "everyday" }

        binding.timeSpinner.apply {
            setSelection(0) // change later
            adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, times).apply {
                setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    reminder.time = times[p2]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                   // Do nothing
                }
            }
        }

        binding.confirmButton.setOnClickListener {
            parentFragmentManager.setFragmentResult(KEY_CONFIRM, Bundle().apply {
                putSerializable(ARG_REMINDER, reminder)
            })
            dismiss()
        }
    }

    companion object {
        const val TAG = "SetReminderFragment"
        const val KEY_CONFIRM = "KEY_CONFIRM"
        const val ARG_REMINDER = "ARG_REMINDER"
        const val ARG_TIMES = "ARG_TIMES"

        fun newInstance(reminder: Reminder, times: Array<String>): SetReminderFragment {
            return SetReminderFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_REMINDER, reminder)
                    putStringArray(ARG_TIMES, times)
                }
            }
        }
    }
}