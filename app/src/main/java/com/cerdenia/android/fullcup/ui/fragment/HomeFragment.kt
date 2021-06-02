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
import com.cerdenia.android.fullcup.data.model.DailyLog
import com.cerdenia.android.fullcup.databinding.FragmentHomeBinding
import com.cerdenia.android.fullcup.ui.adapter.ActivityAdapter
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
        // Set greeting to change depending on time of day.
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
            viewModel.dailyLogLive.value?.let { log ->
                Log.d(TAG, "Current log data size: ${log.activities.size}")
                LogActivityFragment
                    .newInstance(log)
                    .show(parentFragmentManager, LogActivityFragment.TAG)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.remindersLive.observe(viewLifecycleOwner, {
            Log.d(TAG, "Reminder observer fired")
            viewModel.onRemindersFetched()
            viewModel.remindersLive.removeObservers(viewLifecycleOwner)
        })

        viewModel.dailyLogLive.observe(viewLifecycleOwner, { log ->
            //if (log == null) viewModel.initDailyLog()
            viewModel.dailyLogLive.removeObservers(viewLifecycleOwner)
        })

        viewModel.donutDataLive.observe(viewLifecycleOwner, { donutData ->
            Log.d(TAG, "Donut data observer fired: $donutData")
            // Fill donut.
            val cap = viewModel.getActivitiesOfDay().size.toFloat()
            binding.donutView.cap = cap
            binding.donutView.submitData(donutData)
            // Fill color-coded list of day's activities.
            val activities = viewModel.coloredActivities
            binding.recyclerView.adapter = ActivityAdapter(activities)
        })

        parentFragmentManager.setFragmentResultListener(
            LogActivityFragment.LOG_ACTIVITY,
            viewLifecycleOwner,
            { _, result ->
                val log = result.getSerializable(LogActivityFragment.DAILY_LOG) as DailyLog?
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