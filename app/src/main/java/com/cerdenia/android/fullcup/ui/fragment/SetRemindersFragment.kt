package com.cerdenia.android.fullcup.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.data.model.Reminder
import com.cerdenia.android.fullcup.databinding.FragmentSetRemindersBinding
import com.cerdenia.android.fullcup.ui.OnDoneWithScreenListener
import com.cerdenia.android.fullcup.ui.adapter.ReminderAdapter
import com.cerdenia.android.fullcup.ui.dialog.AlertFragment
import com.cerdenia.android.fullcup.ui.dialog.SetReminderFragment
import com.cerdenia.android.fullcup.ui.viewmodel.SetRemindersViewModel
import com.cerdenia.android.fullcup.util.ext.hide
import com.cerdenia.android.fullcup.util.ext.updateList

class SetRemindersFragment : Fragment(), ReminderAdapter.Listener {
    private var _binding: FragmentSetRemindersBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SetRemindersViewModel
    private lateinit var adapter: ReminderAdapter

    private val permissions = arrayOf(
        Manifest.permission.READ_CALENDAR,
        Manifest.permission.WRITE_CALENDAR
    )

    private val calendarPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.all { it.value == true }
        Log.d(TAG, "Permission result: $isGranted")
        if (isGranted) confirmReminders()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)
            .get(SetRemindersViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetRemindersBinding
            .inflate(inflater, container, false)
        adapter = ReminderAdapter(resources, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setRemindersButton.setOnClickListener {
            if (isCalendarPermissionGranted()) {
                confirmReminders()
            } else {
                calendarPermissionLauncher.launch(permissions)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.remindersLive.observe(viewLifecycleOwner, { reminders ->
            binding.progressBar.hide()
            adapter.updateList(reminders.sortedBy { !it.isSet })
            // Enable Set Reminders button if all reminders are ready.
            val isReady = !reminders.any { !it.isSet }
            binding.setRemindersButton.isEnabled = isReady
        })

        parentFragmentManager.apply {
            setFragmentResultListener(SetReminderFragment.CONFIRM, viewLifecycleOwner, { _, res ->
                val reminder = res.getSerializable(SetReminderFragment.REMINDER) as Reminder?
                reminder?.let { viewModel.updateReminder(it) }
            })

            setFragmentResultListener(AlertFragment.CLOSE, viewLifecycleOwner, { _, _ ->
                val callback = context as OnDoneWithScreenListener?
                callback?.onDoneWithScreen(TAG)
            })
        }
    }

    private fun isCalendarPermissionGranted(): Boolean {
        var isPermitted = false
        for (p in permissions) {
            context?.let {
                isPermitted = ContextCompat
                    .checkSelfPermission(it, p) == PackageManager.PERMISSION_GRANTED
            }
        }
        return isPermitted
    }

    private fun confirmReminders() {
        viewModel.confirmReminders()
        val title = getString(R.string.you_re_all_set)
        val message = getString(R.string.your_google_calendar)
        AlertFragment.newInstance(title, message)
            .show(parentFragmentManager, AlertFragment.TAG)
    }

    override fun onItemSelected(reminder: Reminder) {
        SetReminderFragment
            .newInstance(reminder, viewModel.getAvailableTimes())
            .show(parentFragmentManager, SetReminderFragment.TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "SetRemindersFragment"

        fun newInstance(): SetRemindersFragment {
            return SetRemindersFragment()
        }
    }
}