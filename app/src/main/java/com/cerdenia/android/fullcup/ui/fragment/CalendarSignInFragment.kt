package com.cerdenia.android.fullcup.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cerdenia.android.fullcup.databinding.FragmentCalendarSignInBinding
import com.cerdenia.android.fullcup.ui.OnDoneWithScreenListener
import com.cerdenia.android.fullcup.ui.dialog.CalendarPermissionFragment

class CalendarSignInFragment : Fragment() {
    private var _binding: FragmentCalendarSignInBinding? = null
    private val binding get() = _binding!!

    interface Callbacks : OnDoneWithScreenListener {
        fun onAllowedCalendarAccess()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarSignInBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.googleCalendarButton.setOnClickListener {
            CalendarPermissionFragment
                .newInstance()
                .show(parentFragmentManager, CalendarPermissionFragment.TAG)
        }

        binding.outlookCalendarButton.isEnabled = false // For now.
    }

    override fun onStart() {
        super.onStart()
        parentFragmentManager.setFragmentResultListener(
            CalendarPermissionFragment.PERMISSION,
            viewLifecycleOwner,
            { _, result ->
                val isAllowed = result.getBoolean(CalendarPermissionFragment.IS_ALLOWED)
                val callbacks = context as? Callbacks

                // [START] Temporary result.
                if (isAllowed) {
                    callbacks?.onAllowedCalendarAccess()
                } else {
                    callbacks?.onDoneWithScreen(TAG)
                }
                // [END]
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "CalendarSignInFragment"

        fun newInstance() = CalendarSignInFragment()
    }
}