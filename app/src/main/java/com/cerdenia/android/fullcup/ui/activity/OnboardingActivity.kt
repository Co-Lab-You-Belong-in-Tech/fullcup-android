package com.cerdenia.android.fullcup.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.databinding.ActivityOnboardingBinding
import com.cerdenia.android.fullcup.ui.fragment.SelectCategoriesFragment
import com.cerdenia.android.fullcup.ui.fragment.SetRemindersFragment

class OnboardingActivity : AppCompatActivity(),
    SelectCategoriesFragment.Callbacks,
    SetRemindersFragment.Callbacks {
    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            // fragment container is empty
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, SelectCategoriesFragment.newInstance())
                .commit()
        }
    }

    override fun onCategoriesSelected() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, SetRemindersFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    override fun onRemindersConfirmed() {
        MainActivity.newIntent(this).run (::startActivity)
    }
}