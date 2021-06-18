package com.cerdenia.android.fullcup.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cerdenia.android.fullcup.databinding.FragmentAlertBinding
import com.cerdenia.android.fullcup.util.ext.hide

class AlertFragment : DialogFragment() {
    private lateinit var binding: FragmentAlertBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlertBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeButton.setOnClickListener {
            dismiss()
        }

        binding.titleTextView.apply {
           arguments?.getString(TITLE)?.let { title ->
               this.text = title
           } ?: this.hide()
        }

        binding.messageTextView.apply {
            arguments?.getString(MESSAGE)?.let { message ->
                this.text = message
            } ?: this.hide()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        parentFragmentManager.setFragmentResult(CLOSE, Bundle.EMPTY)
        super.onDismiss(dialog)
    }

    companion object {
        const val TAG = "AlertFragment"
        const val CLOSE = "close"
        private const val TITLE = "title"
        private const val MESSAGE = "message"

        fun newInstance(message: String): AlertFragment {
            return AlertFragment().apply {
                arguments = Bundle().apply {
                    putString(MESSAGE, message)
                }
            }
        }

        fun newInstance(title: String, message: String): AlertFragment {
            return AlertFragment().apply {
                arguments = Bundle().apply {
                    putString(TITLE, title)
                    putString(MESSAGE, message)
                }
            }
        }
    }
}