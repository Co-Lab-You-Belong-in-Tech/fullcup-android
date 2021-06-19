package com.cerdenia.android.fullcup.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cerdenia.android.fullcup.databinding.FragmentActivityLogBinding
import com.cerdenia.android.fullcup.ui.dialog.DateExpandFragment
import com.cerdenia.android.fullcup.util.DateTimeUtils
import java.util.*

class CalendarFragment : Fragment() {
    private var _binding: FragmentActivityLogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivityLogBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.calendarView.apply {
            date = Date().time
            maxDate = Date().time

            setOnDateChangeListener { _, y, m, d ->
                val dateString = DateTimeUtils.toDateString(y, m, d)
                Log.d(TAG, "Calendar date clicked! $dateString")

                DateExpandFragment
                    .newInstance(dateString)
                    .show(parentFragmentManager, DateExpandFragment.TAG)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ActivityLogFragment"

        fun newInstance(): CalendarFragment = CalendarFragment()
    }
}