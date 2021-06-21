package com.cerdenia.android.fullcup.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.data.local.FullCupPreferences
import com.cerdenia.android.fullcup.databinding.FragmentHelloUserBinding
import com.cerdenia.android.fullcup.ui.OnDoneWithScreenListener

class HelloUserFragment : Fragment() {

    private var _binding: FragmentHelloUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelloUserBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = FullCupPreferences.userName

        binding.helloTextView.text = getString(R.string.hello_name, name)

        binding.root.setOnClickListener {
            val callback = context as OnDoneWithScreenListener?
            callback?.onDoneWithScreen(TAG)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "HelloUserFragment"

        fun newInstance() = HelloUserFragment()
    }
}