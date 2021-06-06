package com.cerdenia.android.fullcup.ui.activity

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import androidx.activity.result.contract.ActivityResultContracts
import com.cerdenia.android.fullcup.databinding.ActivityOnboardingBinding
import com.cerdenia.android.fullcup.ui.OnDoneWithScreenListener
import com.cerdenia.android.fullcup.ui.fragment.*
import java.util.*

class OnboardingActivity : FullCupActivity(),
    GetStartedFragment.Callbacks,
    CalendarSignInFragment.Callbacks,
    OnDoneWithScreenListener
{
    private lateinit var binding: ActivityOnboardingBinding

    private val calendarActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        // On Activity result:
        replaceFragmentWith(SetRemindersIntroFragment.newInstance(), true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentContainer = binding.fragmentContainer.id

        if (savedInstanceState == null) {
            // Fragment container is empty.
            supportFragmentManager
                .beginTransaction()
                .add(fragmentContainer, GetStartedFragment.newInstance())
                .commit()
        }
    }

    override fun onLoginClicked() {
        MainActivity.newIntent(this).run (::startActivity)
        finish()
    }

    override fun onAllowedCalendarAccess() {
        val builder: Uri.Builder = CalendarContract.CONTENT_URI
            .buildUpon()
            .appendPath("time")
        ContentUris.appendId(builder, Date().time)
        val intent = Intent(Intent.ACTION_VIEW)
            .setData(builder.build())

        calendarActivityResult.launch(intent)
    }

    override fun onDoneWithScreen(tag: String, flag: String?) {
        when (tag) {
            GetStartedFragment.TAG ->
                replaceFragmentWith(SelectActivitiesFragment.newInstance(), true)
            SelectActivitiesFragment.TAG ->
                replaceFragmentWith(HelloUserFragment.newInstance(), true)
            HelloUserFragment.TAG ->
                replaceFragmentWith(CalendarSignInFragment.newInstance(), true)
            CalendarSignInFragment.TAG ->
                replaceFragmentWith(SetRemindersIntroFragment.newInstance(), true)
            SetRemindersIntroFragment.TAG ->
                replaceFragmentWith(SetRemindersFragment.newInstance(), true)
            SetRemindersFragment.TAG -> {
                MainActivity.newIntent(this).run (::startActivity)
                finish()
            }
        }
    }

    private fun writeToCalendar() {
        /*
        reminders.forEach { reminder ->
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val date = calendar.get(Calendar.DAY_OF_MONTH)
            val hour = reminder.time?.substringBefore(":")?.toInt() ?: 0
            val minute = reminder.time?.substringAfter(":")?.toInt() ?: 0

            val startMillis: Long = Calendar.getInstance().run {
                set(year, month, date, hour, minute)
                timeInMillis
            }

            val endMillis: Long = Calendar.getInstance().run {
                set(year, month, date, hour + 1, minute)
                timeInMillis
            }
            val intent = Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
                .putExtra(CalendarContract.Events.TITLE, reminder.category)

            countdown -= 1
            launchCalendar.launch(intent)
        }
         */
    }

    companion object {
        fun newIntent(packageContext: Context): Intent {
            return Intent(packageContext, OnboardingActivity::class.java)
        }
    }
}