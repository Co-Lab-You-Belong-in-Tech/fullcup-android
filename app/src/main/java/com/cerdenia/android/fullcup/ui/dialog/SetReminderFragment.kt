package com.cerdenia.android.fullcup.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.weekdaysButton.setOnClickListener { reminder.days = "weekdays" }
        binding.weekendsButton.setOnClickListener { reminder.days = "weekends" }
        binding.everydayButton.setOnClickListener { reminder.days = "everyday" }

        binding.confirmButton.setOnClickListener {
            reminder.time = "8:00 am" // for testing only
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

        fun newInstance(reminder: Reminder): SetReminderFragment {
            return SetReminderFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_REMINDER, reminder)
                }
            }
        }
    }
}