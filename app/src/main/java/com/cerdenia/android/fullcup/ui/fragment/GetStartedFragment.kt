package com.cerdenia.android.fullcup.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cerdenia.android.fullcup.databinding.FragmentGetStartedBinding

class GetStartedFragment : Fragment() {
    private var _binding: FragmentGetStartedBinding? = null
    private val binding get() = _binding!!
    private var callbacks: Callbacks? = null

    interface Callbacks {
        fun onGetStartedClicked()
        fun onLoginClicked()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGetStartedBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.getStartedButton.setOnClickListener {
            callbacks?.onGetStartedClicked()
        }

        binding.loginButton.setOnClickListener {
            callbacks?.onLoginClicked()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        fun newInstance() = GetStartedFragment()
    }
}