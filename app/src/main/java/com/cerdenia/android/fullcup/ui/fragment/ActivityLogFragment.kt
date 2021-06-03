package com.cerdenia.android.fullcup.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cerdenia.android.fullcup.data.model.ActivityLog
import com.cerdenia.android.fullcup.databinding.FragmentActivityLogBinding
import java.util.*

class ActivityLogFragment : Fragment() {
    private var _binding: FragmentActivityLogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActivityLogBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.calendarView.date = Date().time
        binding.calendarView.setOnDateChangeListener() { _, p1, p2, p3 ->
            Log.d(TAG, "Calendar date clicked! $p1, $p2, $p3")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ActivityLogFragment"

        fun newInstance(): ActivityLogFragment = ActivityLogFragment()
    }
}