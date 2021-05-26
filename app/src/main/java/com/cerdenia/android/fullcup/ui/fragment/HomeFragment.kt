package com.cerdenia.android.fullcup.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.cerdenia.android.fullcup.databinding.FragmentHomeBinding
import com.cerdenia.android.fullcup.ui.adapter.CategoryAdapter
import com.cerdenia.android.fullcup.ui.viewmodel.HomeViewModel
import java.text.DateFormat
import java.util.*

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.adapter = CategoryAdapter(viewModel.coloredCategories)
    }

    override fun onStart() {
        super.onStart()
        setupDonut()
        binding.dateTextView.text = DateFormat
            .getDateInstance(DateFormat.MEDIUM)
            .format(Date())
    }

    private fun setupDonut() {
        binding.donutView.cap = viewModel.donutCap
        binding.donutView.submitData(emptyList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }
}