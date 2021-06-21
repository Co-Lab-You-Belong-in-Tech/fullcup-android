package com.cerdenia.android.fullcup.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cerdenia.android.fullcup.databinding.ActivitySecondaryBinding
import com.cerdenia.android.fullcup.ui.OnDoneWithScreenListener
import com.cerdenia.android.fullcup.ui.fragment.SelectActivitiesFragment
import com.cerdenia.android.fullcup.ui.fragment.SetRemindersFragment

class SettingsActivity : FullCupActivity(), OnDoneWithScreenListener {

    private lateinit var binding: ActivitySecondaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentContainer = binding.fragmentContainer.id

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(fragmentContainer, SelectActivitiesFragment.newInstance())
                .commit()
        }
    }

    override fun onDoneWithScreen(tag: String, flag: String?) {
        when (tag) {
            SelectActivitiesFragment.TAG -> SetRemindersFragment.newInstance().run {
                replaceFragmentWith(this, true)
            }
            SetRemindersFragment.TAG -> finish()
        }
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }
}