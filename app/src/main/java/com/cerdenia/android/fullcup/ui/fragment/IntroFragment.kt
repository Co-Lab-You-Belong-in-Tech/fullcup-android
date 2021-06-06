package com.cerdenia.android.fullcup.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.databinding.FragmentIntroBinding
import com.cerdenia.android.fullcup.ui.OnDoneWithScreenListener

class IntroFragment : Fragment() {
    private var _binding: FragmentIntroBinding? = null
    private val binding get() = _binding!!

    private val flagMap = mapOf(0 to FLAG_FIRST, 1 to FLAG_SECOND)
    private val textMap = mapOf(
        0 to R.string.self_care_intro_1,
        1 to R.string.self_care_intro_2
    )

    interface Callbacks: OnDoneWithScreenListener {
        fun onIntroSkipped()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callbacks = context as Callbacks?
        val page = arguments?.getInt(PAGE) ?: 0

        binding.skipIntroButton.setOnClickListener {
            callbacks?.onIntroSkipped()
        }

        binding.root.setOnClickListener {
            callbacks?.onDoneWithScreen(TAG, flagMap[page])
        }

        binding.mainTextView.text = textMap[page]?.let { getString(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "IntroFragment"
        private const val PAGE = "PAGE"
        const val FLAG_FIRST = "FLAG_1"
        const val FLAG_SECOND = "FLAG_2"

        fun newInstance(page: Int) = IntroFragment().apply {
            arguments = Bundle().apply {
                putInt(PAGE, page)
            }
        }
    }
}