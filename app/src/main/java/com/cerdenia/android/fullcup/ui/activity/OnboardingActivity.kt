package com.cerdenia.android.fullcup.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.databinding.ActivityOnboardingBinding
import com.cerdenia.android.fullcup.ui.fragment.SetRemindersFragment
import com.cerdenia.android.fullcup.ui.fragment.SignInFragment

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            // fragment container is empty
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, SetRemindersFragment.newInstance())
                .commit()
        }
    }
}