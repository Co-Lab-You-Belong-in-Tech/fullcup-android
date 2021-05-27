package com.cerdenia.android.fullcup.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cerdenia.android.fullcup.databinding.FragmentSelectCategoriesBinding
import com.cerdenia.android.fullcup.ui.viewmodel.SelectCategoriesViewModel

class SelectCategoriesFragment : Fragment() {
    private var _binding: FragmentSelectCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SelectCategoriesViewModel
    private var callbacks: Callbacks? = null

    interface Callbacks {
        fun onCategoriesSelected()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SelectCategoriesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val checkBoxes = listOf(
            binding.healthAndFitnessCheckbox,
            binding.lovedOnesCheckbox,
            binding.mindfulnessCheckbox,
            binding.nutritionCheckbox,
            binding.productivityCheckbox,
            binding.sleepHabitsCheckbox
        )

        checkBoxes.forEach { checkBox ->
            // Set initial state based on stored categories.
            checkBox.isChecked = viewModel.categories.contains(checkBox.text)
            checkBox.setOnClickListener {
                // Disable Next button if no checkboxes are selected
                binding.nextButton.isEnabled = checkBoxes.any { it.isChecked }
            }
        }

        binding.nextButton.apply {
            isEnabled = checkBoxes.any { it.isChecked }
            setOnClickListener {
                // Pass selected and deselected items to ViewModel.
                val selections = checkBoxes
                    .filter { it.isChecked }
                    .map { it.text.toString() }
                val deselections = checkBoxes
                    .filter { !it.isChecked }
                    .map { it.text.toString() }
                viewModel.submitCategories(selections, deselections)
                // Save user name.
                val userName = binding.nameEditText.text.toString()
                viewModel.submitUserName(userName)
                callbacks?.onCategoriesSelected()
            }
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
        private const val TAG = "SelectCategoriesFrag"

        fun newInstance(): SelectCategoriesFragment = SelectCategoriesFragment()
    }
}