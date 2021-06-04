package com.cerdenia.android.fullcup.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cerdenia.android.fullcup.databinding.FragmentSetRemindersIntroBinding
import com.cerdenia.android.fullcup.ui.OnDoneWithScreenListener

class SetRemindersIntroFragment : Fragment() {
    private var _binding: FragmentSetRemindersIntroBinding? = null
    private val binding get() = _binding!!
    private var callback: OnDoneWithScreenListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as OnDoneWithScreenListener?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetRemindersIntroBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nextButton.setOnClickListener {
            callback?.onDoneWithScreen(TAG)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    companion object {
        const val TAG = "SetReminderIntroFragment"

        fun newInstance() = SetRemindersIntroFragment()
    }
}