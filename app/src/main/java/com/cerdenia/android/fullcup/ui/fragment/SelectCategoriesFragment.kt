package com.cerdenia.android.fullcup.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cerdenia.android.fullcup.data.local.FullCupPreferences
import com.cerdenia.android.fullcup.databinding.FragmentSelectCategoriesBinding

class SelectCategoriesFragment : Fragment() {
    private var _binding: FragmentSelectCategoriesBinding? = null
    private val binding get() = _binding!!

    private var callbacks: Callbacks? = null

    interface Callbacks {
        fun onCategoriesSelected()
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
            checkBox.setOnClickListener {
                // Disable Next button if no checkboxes are selected
                binding.nextButton.isEnabled = checkBoxes.any { it.isChecked }
            }
        }

        binding.nextButton.apply {
            isEnabled = checkBoxes.any { it.isChecked }
            setOnClickListener {
                // Save selected items to SharedPreferences.
                val selections: List<String> = checkBoxes
                    .filter { it.isChecked }
                    .map { it.text.toString() }
                Log.i(TAG, "Saving selections: $selections")
                FullCupPreferences.categories = selections.toSet()
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