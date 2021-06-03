package com.cerdenia.android.fullcup.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cerdenia.android.fullcup.databinding.FragmentCongratulationsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CongratulationsFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCongratulationsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCongratulationsBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shareButton.isEnabled = false // For now.
        binding.laterButton.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        const val TAG = "CongratulationsFragment"

        fun newInstance() = CongratulationsFragment()
    }
}