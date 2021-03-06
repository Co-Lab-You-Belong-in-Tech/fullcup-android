package com.cerdenia.android.fullcup.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.databinding.ActivityMainBinding
import com.cerdenia.android.fullcup.ui.fragment.CalendarFragment
import com.cerdenia.android.fullcup.ui.fragment.HomeFragment

class MainActivity : FullCupActivity() {

    private lateinit var binding: ActivityMainBinding

    private val settingsActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        binding.bottomNavigationView.selectedItemId = R.id.nav_home
    }

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

        binding.bottomNavigationView.apply {
            selectedItemId = R.id.nav_home

            setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.nav_home -> replaceFragmentWith(HomeFragment.newInstance())
                    R.id.nav_activity_log -> replaceFragmentWith(CalendarFragment.newInstance())
                    R.id.nav_progress -> {
                        // TODO
                    }
                    R.id.nav_account -> {
                        // Just for now:
                        SettingsActivity.newIntent(this@MainActivity).run {
                            settingsActivityLauncher.launch(this)
                        }
                    }
                }

                true
            }
        }
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}