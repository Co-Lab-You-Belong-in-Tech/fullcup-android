package com.cerdenia.android.fullcup.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cerdenia.android.fullcup.data.model.DailyLog
import com.cerdenia.android.fullcup.databinding.FragmentDateExpandBinding
import com.cerdenia.android.fullcup.ui.adapter.ActivityAdapter
import com.cerdenia.android.fullcup.util.DateTimeUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.DateFormat

class DateExpandFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentDateExpandBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDateExpandBinding
            .inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dailyLog = arguments?.getSerializable(DAILY_LOG) as DailyLog?
        val dateString = dailyLog?.summary?.date

        binding.dateTextView.text = dateString?.let {
            DateFormat
            .getDateInstance(DateFormat.MEDIUM)
            .format(DateTimeUtils.toDate(it))
        }

        val activities = dailyLog?.activities ?: emptyList()
        binding.recyclerView.adapter = ActivityAdapter(activities)
    }

    companion object {
        const val TAG = "DateExpandFragment"
        private const val DAILY_LOG = "DAILY_LOG"

        fun newInstance(dailyLog: DailyLog): DateExpandFragment {
            return DateExpandFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(DAILY_LOG, dailyLog)
                }
            }
        }
    }
}