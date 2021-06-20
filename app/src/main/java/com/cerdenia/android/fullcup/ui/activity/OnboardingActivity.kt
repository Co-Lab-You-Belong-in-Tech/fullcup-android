package com.cerdenia.android.fullcup.ui.activity

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import androidx.activity.result.contract.ActivityResultContracts
import com.cerdenia.android.fullcup.data.local.FullCupPreferences
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
        ActivityResultContracts.StartActivityForResult()) {
        MainActivity.newIntent(this).run (::startActivity)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentContainer = binding.fragmentContainer.id

        if (savedInstanceState == null) {
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

        val intent = Intent(Intent.ACTION_VIEW).setData(builder.build())
        calendarActivityResult.launch(intent)
    }

    override fun onDoneWithScreen(tag: String, flag: String?) {
        when (tag) {
            GetStartedFragment.TAG -> SelectActivitiesFragment.newInstance().run {
                replaceFragmentWith(this, true)
            }
            SelectActivitiesFragment.TAG -> HelloUserFragment.newInstance().run {
                replaceFragmentWith(this, true)
            }
            HelloUserFragment.TAG -> SetRemindersIntroFragment.newInstance().run {
                replaceFragmentWith(this, true)
            }
            CalendarSignInFragment.TAG -> SetRemindersIntroFragment.newInstance().run {
                replaceFragmentWith(this, true)
            }
            SetRemindersIntroFragment.TAG -> SetRemindersFragment.newInstance().run {
                replaceFragmentWith(this, true)
            }
            SetRemindersFragment.TAG -> {
                FullCupPreferences.isOnboarded = true
                MainActivity.newIntent(this).run (::startActivity)
                finish()
            }
        }
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, OnboardingActivity::class.java)
        }
    }
}