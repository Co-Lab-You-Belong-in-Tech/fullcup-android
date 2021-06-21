package com.cerdenia.android.fullcup.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cerdenia.android.fullcup.data.local.FullCupPreferences
import com.cerdenia.android.fullcup.databinding.ActivitySecondaryBinding
import com.cerdenia.android.fullcup.ui.fragment.IntroFragment

class IntroActivity : FullCupActivity(), IntroFragment.Callbacks {

    private lateinit var binding: ActivitySecondaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentContainer = binding.fragmentContainer.id

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(fragmentContainer, IntroFragment.newInstance(0))
                .commit()
        }
    }

    override fun onIntroSkipped() {
        FullCupPreferences.isNewUser = false
        OnboardingActivity.newIntent(this).run (::startActivity)
        finish()
    }

    override fun onNextClicked(page: Int) {
        when (page) {
            0 -> replaceFragmentWith(IntroFragment.newInstance(1), true)
            1 -> onIntroSkipped()
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, IntroActivity::class.java)
        }
    }
}