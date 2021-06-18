package com.cerdenia.android.fullcup.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
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
            val isPermitted = checkCalendarPermissions()
            if (isPermitted) {
                viewModel.confirmReminders()
                AlertFragment
                    .newInstance(getString(R.string.you_re_all_set),
                        getString(R.string.your_google_calendar))
                    .show(parentFragmentManager, AlertFragment.TAG)
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(), permissions, CALENDAR_PERMISSIONS
                )
            }
        }
    }

    private fun checkCalendarPermissions(): Boolean {
        var isPermitted = false
        permissions.forEach { permission ->
            isPermitted = ContextCompat.checkSelfPermission(
                requireContext(), permission
            ) == PackageManager.PERMISSION_GRANTED
        }
        return isPermitted
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val isGranted: Boolean = grantResults.all { it == 0 }
        if (isGranted) {
            viewModel.confirmReminders()
            AlertFragment
                .newInstance(getString(R.string.you_re_all_set),
                    getString(R.string.your_google_calendar))
                .show(parentFragmentManager, AlertFragment.TAG)
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

        parentFragmentManager.setFragmentResultListener(
            SetReminderFragment.KEY_CONFIRM,
            viewLifecycleOwner,
            { _, result ->
                val reminder = result.getSerializable(SetReminderFragment.REMINDER) as Reminder
                Log.i(TAG, "Got reminder: $reminder")
                viewModel.updateReminder(reminder)
            }
        )

        parentFragmentManager.setFragmentResultListener(
            AlertFragment.CLOSE,
            viewLifecycleOwner,
            { _, _ ->
                val callback = context as OnDoneWithScreenListener?
                callback?.onDoneWithScreen(TAG)
            }
        )
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
        const val CALENDAR_PERMISSIONS = 69
        const val TAG = "SetRemindersFragment"

        fun newInstance(): SetRemindersFragment {
            return SetRemindersFragment()
        }
    }
}