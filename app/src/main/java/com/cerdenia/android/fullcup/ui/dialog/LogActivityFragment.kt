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
import com.cerdenia.android.fullcup.util.ext.toEditable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LogActivityFragment : BottomSheetDialogFragment(), CategoryAdapter.Listener {
    private lateinit var binding: FragmentLogActivityBinding
    private val selectedItems = mutableSetOf<String>()
    private lateinit var log: DailyLog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log = arguments?.getSerializable(DAILY_LOG) as DailyLog
        Log.d(TAG, "Got log: $log")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogActivityBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        // Populate category list.
        val categories = log.activities.map { Pair(it.category, it.isDone) }
        binding.recyclerView.adapter = CategoryAdapter(categories, this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editText.text = log.summary.content.toEditable()

        binding.doneButton.setOnClickListener {
            // Save user-inputted text to DailyLog object.
            log.summary.content = binding.editText.text.toString()
            parentFragmentManager.setFragmentResult(LOG_ACTIVITY, Bundle().apply {
                putSerializable(DAILY_LOG, log)
            })
            dismiss()
        }
    }

    override fun onCheckboxItemClicked(category: String, isChecked: Boolean) {
        // Find item in DailyLog object and update isDone value.
        val i = log.activities.indexOfFirst { it.category == category }
        log.activities[i].isDone = isChecked
        Log.i(TAG, "Item changed: ${log.activities[i]}")
    }

    companion object {
        const val TAG = "LogActivityFragment"
        const val DAILY_LOG = "DAILY_LOG"
        const val LOG_ACTIVITY = "LOG_ACTIVITY"
        const val CATEGORIES = "CATEGORIES"

        fun newInstance(log: DailyLog): LogActivityFragment {
            return LogActivityFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(DAILY_LOG, log)
                }
            }
        }
    }
}