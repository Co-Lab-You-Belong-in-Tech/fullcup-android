package com.cerdenia.android.fullcup.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cerdenia.android.fullcup.data.model.DailyLog
import com.cerdenia.android.fullcup.databinding.FragmentLogActivityBinding
import com.cerdenia.android.fullcup.ui.adapter.CategoryAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LogActivityFragment : BottomSheetDialogFragment(), CategoryAdapter.Listener {
    private lateinit var binding: FragmentLogActivityBinding
    private val selectedItems = mutableSetOf<String>()
    private lateinit var log: DailyLog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogActivityBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categories = arguments?.getStringArray(CATEGORIES)?.toList() ?: emptyList()
        val completion = arguments?.getBooleanArray(COMPLETION)?.toList() ?: emptyList()
        log = DailyLog(categories = categories.toMutableList())

        binding.recyclerView.adapter = CategoryAdapter(categories, this)

        binding.doneButton.setOnClickListener {
            parentFragmentManager.setFragmentResult(LOG_ACTIVITY, Bundle().apply {
                putStringArray(CATEGORIES, selectedItems.toTypedArray())
            })
            dismiss()
        }
    }

    override fun onCheckboxItemClicked(category: String, isChecked: Boolean) {
        log.markAsDone(category, isChecked)
    }

    companion object {
        const val TAG = "LogActivityFragment"
        const val LOG_ACTIVITY = "LOG_ACTIVITY"
        const val CATEGORIES = "CATEGORIES"
        const val COMPLETION = "COMPLETION"

        fun newInstance(
            categories: List<String>,
            completion: List<Boolean> = emptyList()
        ): LogActivityFragment {
            return LogActivityFragment().apply {
                arguments = Bundle().apply {
                    putStringArray(CATEGORIES, categories.toTypedArray())
                    putBooleanArray(COMPLETION, completion.toBooleanArray())
                }
            }
        }
    }
}