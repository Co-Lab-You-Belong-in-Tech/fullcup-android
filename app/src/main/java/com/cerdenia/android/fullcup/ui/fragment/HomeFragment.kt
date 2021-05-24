package com.cerdenia.android.fullcup.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.futured.donut.DonutSection
import com.cerdenia.android.fullcup.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupDonut()
    }

    private fun setupDonut() {
        val section1 = DonutSection(
            name = "section_1",
            color = Color.parseColor("#FB1D32"),
            amount = 1f
        )

        val section2 = DonutSection(
            name = "section_2",
            color = Color.parseColor("#FFB98E"),
            amount = 1f
        )

        binding.donutView.cap = 5f
        binding.donutView.submitData(listOf(section1, section2))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }
}