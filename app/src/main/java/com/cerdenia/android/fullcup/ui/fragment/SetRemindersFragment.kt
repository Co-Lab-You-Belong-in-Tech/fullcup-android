package com.cerdenia.android.fullcup.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cerdenia.android.fullcup.data.model.Reminder
import com.cerdenia.android.fullcup.databinding.FragmentSetRemindersBinding
import com.cerdenia.android.fullcup.ui.adapter.ReminderAdapter
import com.cerdenia.android.fullcup.ui.dialog.SetReminderFragment
import com.cerdenia.android.fullcup.ui.viewmodel.SetRemindersViewModel

class SetRemindersFragment : Fragment(), ReminderAdapter.Listener {
    private var _binding: FragmentSetRemindersBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SetRemindersViewModel
    private lateinit var adapter: ReminderAdapter
    private var callbacks: Callbacks? = null

    interface Callbacks {
        fun onRemindersConfirmed(reminders: List<Reminder>)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SetRemindersViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetRemindersBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ReminderAdapter(resources, this)
        binding.recyclerView.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setRemindersButton.setOnClickListener {
            viewModel.confirmReminders()
            viewModel.remindersLive.value?.let { reminders ->
                //callbacks?.onRemindersConfirmed(reminders)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.remindersLive.observe(viewLifecycleOwner, { reminders ->
            adapter.submitList(reminders.sortedBy { !it.isSet })
            adapter.notifyDataSetChanged()
            // Enable Set Reminders button if all reminders are ready.
            val isReady = !reminders.any { !it.isSet }
            binding.setRemindersButton.isEnabled = isReady
        })

        parentFragmentManager.setFragmentResultListener(
            SetReminderFragment.KEY_CONFIRM,
            viewLifecycleOwner,
            { _, result ->
                val reminder = result.getSerializable(SetReminderFragment.REMINDER) as Reminder
                Log.i(TAG, "Got reminder: $reminder")
                viewModel.updateReminder(reminder)
            }
        )
    }

    override fun onItemSelected(reminder: Reminder) {
        SetReminderFragment
            .newInstance(reminder, viewModel.getFreeTimes())
            .show(parentFragmentManager, SetReminderFragment.TAG)
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
        private const val TAG = "SetRemindersFragment"

        fun newInstance(): SetRemindersFragment {
            return SetRemindersFragment()
        }
    }
}