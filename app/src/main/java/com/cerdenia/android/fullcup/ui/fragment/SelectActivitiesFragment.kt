package com.cerdenia.android.fullcup.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.cerdenia.android.fullcup.databinding.FragmentSelectActivitiesBinding
import com.cerdenia.android.fullcup.ui.OnDoneWithScreenListener
import com.cerdenia.android.fullcup.ui.viewmodel.SelectActivitiesViewModel
import com.cerdenia.android.fullcup.util.ext.toEditable

class SelectActivitiesFragment : Fragment() {

    private var _binding: FragmentSelectActivitiesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SelectActivitiesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectActivitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.nameEditText.text = viewModel.userName.toEditable()

        val checkBoxes = arrayOf(
            binding.healthAndFitnessCheckbox,
            binding.lovedOnesCheckbox,
            binding.mindfulnessCheckbox,
            binding.nutritionCheckbox,
            binding.productivityCheckbox,
            binding.sleepHabitsCheckbox
        )

        checkBoxes.forEach { checkBox ->
            checkBox.apply {
                // Set initial state based on stored activity names.
                isChecked = viewModel.activities.contains(checkBox.text)

                setOnClickListener {
                    // Disable Next button if no checkboxes are selected.
                    binding.nextButton.isEnabled = checkBoxes.any { it.isChecked }
                }
            }
        }

        binding.nextButton.apply {
            isEnabled = checkBoxes.any { it.isChecked }

            setOnClickListener {
                val selections = checkBoxes.filter { it.isChecked }.map { it.text.toString() }
                val deselections = checkBoxes.filter { !it.isChecked }.map { it.text.toString() }
                viewModel.submitActivities(selections, deselections)

                // Save user name.
                viewModel.submitUserName(binding.nameEditText.text.toString())
                val callback = context as OnDoneWithScreenListener?
                callback?.onDoneWithScreen(TAG)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val TAG = "SelectActivitiesFragment"

        fun newInstance(): SelectActivitiesFragment = SelectActivitiesFragment()
    }
}