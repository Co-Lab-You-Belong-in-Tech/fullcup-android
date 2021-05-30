package com.cerdenia.android.fullcup.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.TIME_PATTERN
import com.cerdenia.android.fullcup.data.local.FullCupPreferences
import com.cerdenia.android.fullcup.data.model.ActivityLog
import com.cerdenia.android.fullcup.data.model.DailyLog
import com.cerdenia.android.fullcup.data.model.SummaryLog
import com.cerdenia.android.fullcup.databinding.FragmentHomeBinding
import com.cerdenia.android.fullcup.ui.adapter.CategoryAdapter
import com.cerdenia.android.fullcup.ui.dialog.LogActivityFragment
import com.cerdenia.android.fullcup.ui.viewmodel.HomeViewModel
import com.cerdenia.android.fullcup.util.DateTimeUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDailyLog() // Get log data for the day.

        val time = SimpleDateFormat(TIME_PATTERN).format((Date()))
        binding.greetingTextView.text = getString(
            R.string.greeting_with_name,
            DateTimeUtils.getGreeting(resources, time),
            FullCupPreferences.userName
        )

        binding.dateTextView.text = DateFormat
            .getDateInstance(DateFormat.MEDIUM)
            .format(Date())

        binding.logButton.setOnClickListener {
            val log = viewModel.dailyLogLive.value
                ?: createNewLog(viewModel.categories)
            LogActivityFragment
                .newInstance(log)
                .show(parentFragmentManager, LogActivityFragment.TAG)
        }
    }

    private fun createNewLog(categories: List<String>): DailyLog {
        return DailyLog(
            summary = SummaryLog(),
            activities = categories.map { category ->
                ActivityLog(category = category)
            } as MutableList<ActivityLog>
        )
    }

    override fun onStart() {
        super.onStart()
        viewModel.remindersLive.observe(viewLifecycleOwner, {
            viewModel.onRemindersFetched()
        })

        viewModel.donutDataLive.observe(viewLifecycleOwner, { donutData ->
            binding.donutView.cap = viewModel.categories.size.toFloat()
            binding.donutView.submitData(donutData)
            binding.recyclerView.adapter = CategoryAdapter(viewModel.coloredCategories)
        })

        parentFragmentManager.setFragmentResultListener(
            LogActivityFragment.LOG_ACTIVITY,
            viewLifecycleOwner,
            { _, result ->
                val log = result.getSerializable(LogActivityFragment.DAILY_LOG) as DailyLog?
                Log.i(TAG, "Got result: $log")
                log?.let { viewModel.saveDailyLog(it) }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "HomeFragment"

        fun newInstance(): HomeFragment = HomeFragment()
    }
}