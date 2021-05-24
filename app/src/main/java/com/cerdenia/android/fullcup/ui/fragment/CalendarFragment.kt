package com.cerdenia.android.fullcup.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cerdenia.android.fullcup.databinding.FragmentCalendarBinding
import com.cerdenia.android.fullcup.ui.viewmodel.CalendarViewModel

class CalendarFragment: Fragment() {

    private lateinit var viewModel: CalendarViewModel

    private var _binding: FragmentCalendarBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener() {
            viewModel.getBusyTimes()
        }
    }

    override fun onStart() {
        super.onStart()
        /*
        viewModel.eventsLive.observe(viewLifecycleOwner, { events ->
          // Do nothing
        })
         */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): CalendarFragment {
            return CalendarFragment()
        }
    }
}