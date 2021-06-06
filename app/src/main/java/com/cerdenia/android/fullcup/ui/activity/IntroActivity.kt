package com.cerdenia.android.fullcup.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.databinding.ActivitySecondaryBinding
import com.cerdenia.android.fullcup.ui.fragment.IntroFragment

class IntroActivity : AppCompatActivity(), IntroFragment.Callbacks {
    private lateinit var binding: ActivitySecondaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val container = binding.fragmentContainer.id
            supportFragmentManager
                .beginTransaction()
                .add(container, IntroFragment.newInstance(0))
                .commit()
        }
    }

    override fun onIntroSkipped() {
        OnboardingActivity.newIntent(this).run (::startActivity)
        finish()
    }

    override fun onDoneWithScreen(tag: String, flag: String?) {
        when (flag) {
            IntroFragment.FLAG_FIRST -> replaceFragmentWith(IntroFragment.newInstance(1))
            IntroFragment.FLAG_SECOND -> onIntroSkipped()
        }
    }

    private fun replaceFragmentWith(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}