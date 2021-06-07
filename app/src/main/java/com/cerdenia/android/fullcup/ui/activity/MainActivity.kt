package com.cerdenia.android.fullcup.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.databinding.ActivityMainBinding
import com.cerdenia.android.fullcup.ui.fragment.CalendarFragment
import com.cerdenia.android.fullcup.ui.fragment.HomeFragment

class MainActivity : FullCupActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentContainer = binding.fragmentContainer.id

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(fragmentContainer, HomeFragment.newInstance())
                .commit()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> replaceFragmentWith(HomeFragment.newInstance())
                R.id.nav_activity_log -> replaceFragmentWith(CalendarFragment.newInstance())
                R.id.nav_progress -> { } // TODO
                R.id.nav_account -> { } // TODO
            }
            true
        }
    }

    companion object {
        private const val TAG = "MainActivityTag"

        fun newIntent(packageContext: Context): Intent {
            return Intent(packageContext, MainActivity::class.java)
        }
    }
}