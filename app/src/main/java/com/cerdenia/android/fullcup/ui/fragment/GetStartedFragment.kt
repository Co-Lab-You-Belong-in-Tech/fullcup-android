package com.cerdenia.android.fullcup.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cerdenia.android.fullcup.databinding.FragmentGetStartedBinding
import com.cerdenia.android.fullcup.ui.OnDoneWithScreenListener

class GetStartedFragment : Fragment() {

    private var _binding: FragmentGetStartedBinding? = null
    private val binding get() = _binding!!

    interface Callbacks : OnDoneWithScreenListener {

        fun onLoginClicked()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGetStartedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callbacks = context as Callbacks?

        binding.getStartedButton.setOnClickListener {
            callbacks?.onDoneWithScreen(TAG)
        }

        binding.loginButton.setOnClickListener {
            callbacks?.onLoginClicked()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val TAG = "GetStartedFragment"

        fun newInstance() = GetStartedFragment()
    }
}