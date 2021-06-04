package com.cerdenia.android.fullcup.ui.activity

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.databinding.ActivityOnboardingBinding
import com.cerdenia.android.fullcup.ui.fragment.GetStartedFragment
import com.cerdenia.android.fullcup.ui.fragment.SelectActivitiesFragment
import com.cerdenia.android.fullcup.ui.fragment.SetRemindersFragment

class OnboardingActivity : AppCompatActivity(),
    GetStartedFragment.Callbacks,
    SelectActivitiesFragment.Callbacks,
    SetRemindersFragment.Callbacks
{
    private lateinit var binding: ActivityOnboardingBinding

    private val launchCalendar = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        // On calendar launch, launch MainActivity. TODO: finish this activity.
        MainActivity.newIntent(this).run(::startActivity)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            // Fragment container is empty.
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, GetStartedFragment.newInstance())
                .commit()
        }
    }

    override fun onActivitiesSelected() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, SetRemindersFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    override fun onRemindersConfirmed() {
        MainActivity.newIntent(this).run(::startActivity)

        /*
        val builder: Uri.Builder = CalendarContract.CONTENT_URI
            .buildUpon()
            .appendPath("time")
        ContentUris.appendId(builder, Date().time)
        val intent = Intent(Intent.ACTION_VIEW)
            .setData(builder.build())
        launchCalendar.launch(intent)
         */

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

    override fun onGetStartedClicked() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, SelectActivitiesFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    override fun onLoginClicked() {
        MainActivity.newIntent(this).run (::startActivity)
        finish()
    }
}