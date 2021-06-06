package com.cerdenia.android.fullcup.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cerdenia.android.fullcup.databinding.FragmentCalendarPermissionBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CalendarPermissionFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCalendarPermissionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarPermissionBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.notNowButton.setOnClickListener {
            parentFragmentManager.setFragmentResult(PERMISSION, Bundle().apply {
                putBoolean(IS_ALLOWED, false)
            })
            dismiss()
        }

        binding.allowButton.setOnClickListener {
            parentFragmentManager.setFragmentResult(PERMISSION, Bundle().apply {
                putBoolean(IS_ALLOWED, true)
            })
            dismiss()
        }
    }

    companion object {
        const val PERMISSION = "PERMISSION"
        const val IS_ALLOWED = "IS_ALLOWED"
        const val TAG = "CalendarPermissionFragment"

        fun newInstance() = CalendarPermissionFragment()
    }
}