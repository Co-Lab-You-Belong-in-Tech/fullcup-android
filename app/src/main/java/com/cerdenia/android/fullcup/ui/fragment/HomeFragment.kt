package com.cerdenia.android.fullcup.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.cerdenia.android.fullcup.data.model.ActivityLog
import com.cerdenia.android.fullcup.data.model.DailyLog
import com.cerdenia.android.fullcup.data.model.SummaryLog
import com.cerdenia.android.fullcup.databinding.FragmentHomeBinding
import com.cerdenia.android.fullcup.ui.adapter.CategoryAdapter
import com.cerdenia.android.fullcup.ui.dialog.LogActivityFragment
import com.cerdenia.android.fullcup.ui.viewmodel.HomeViewModel
import java.text.DateFormat
import java.util.*

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private var _dailyLog: DailyLog? = null
    private val dailyLog get() = _dailyLog

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
        binding.recyclerView.adapter = CategoryAdapter(viewModel.coloredCategories)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.donutView.cap = viewModel.categories.size.toFloat()
        //Get log data for the day.
        viewModel.getDailyLog()

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
        viewModel.donutDataLive.observe(viewLifecycleOwner, { donutData ->
            binding.donutView.submitData(donutData)
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