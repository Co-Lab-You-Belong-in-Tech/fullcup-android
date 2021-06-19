package com.cerdenia.android.fullcup.ui.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cerdenia.android.fullcup.DATE_PATTERN
import com.cerdenia.android.fullcup.data.model.DailyLog
import com.cerdenia.android.fullcup.databinding.FragmentDateExpandBinding
import com.cerdenia.android.fullcup.ui.adapter.ActivityAdapter
import com.cerdenia.android.fullcup.util.DateTimeUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

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

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dailyLog = arguments?.getSerializable(DAILY_LOG) as DailyLog?
        val dateString = dailyLog?.summary?.date
        val isToday = dateString == SimpleDateFormat(DATE_PATTERN).format(Date())
        val date = dateString?.let {
            DateFormat.getDateInstance(DateFormat.LONG)
                .format(DateTimeUtils.toDate(it))
        }

        binding.dateTextView.text = if (isToday) "Today | $date" else date

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