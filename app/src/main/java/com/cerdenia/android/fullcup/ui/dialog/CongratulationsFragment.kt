package com.cerdenia.android.fullcup.ui.dialog

import android.content.Intent
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
        binding.laterButton.setOnClickListener {
            dismiss()
        }

        binding.shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Write something here about FullCup.")
                putExtra(Intent.EXTRA_SUBJECT, "This is a subject.")
            }

            val chooserIntent = Intent.createChooser(intent, "Share your progress")
            startActivity(chooserIntent)
            dismiss()
        }
    }

    companion object {
        const val TAG = "CongratulationsFragment"

        fun newInstance() = CongratulationsFragment()
    }
}