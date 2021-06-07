package com.cerdenia.android.fullcup.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cerdenia.android.fullcup.DAY_NAME_PATTERN
import com.cerdenia.android.fullcup.databinding.FragmentDateExpandBinding
import com.cerdenia.android.fullcup.ui.adapter.ActivityAdapter
import com.cerdenia.android.fullcup.ui.viewmodel.DateExpandViewModel
import com.cerdenia.android.fullcup.util.DateTimeUtils
import com.cerdenia.android.fullcup.util.ext.filterDone
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateExpandFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentDateExpandBinding
    private lateinit var viewModel: DateExpandViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)
            .get(DateExpandViewModel::class.java)
    }

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
        val dateString = arguments?.getString(DATE_STRING)
        dateString?.let { viewModel.getDailyLog(it) }

        binding.dateTextView.text = dateString?.let {
            DateFormat
            .getDateInstance(DateFormat.MEDIUM)
            .format(DateTimeUtils.toDate(it))
        }

        viewModel.dailyLoglive.observe(viewLifecycleOwner, { dailyLog ->
            Log.d(TAG, "Got log: $dailyLog")
            dailyLog?.activities?.let {
                binding.recyclerView.adapter = ActivityAdapter(it)
            }
        })
    }

    companion object {
        const val TAG = "DateExpandFragment"
        private const val DATE_STRING = "DATE_STRING"

        fun newInstance(dateString: String): DateExpandFragment {
            return DateExpandFragment().apply {
                arguments = Bundle().apply {
                    putString(DATE_STRING, dateString)
                }
            }
        }
    }
}