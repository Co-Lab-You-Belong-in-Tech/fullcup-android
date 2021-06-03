package com.cerdenia.android.fullcup.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.databinding.ActivityMainBinding
import com.cerdenia.android.fullcup.ui.fragment.ActivityLogFragment
import com.cerdenia.android.fullcup.ui.fragment.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, HomeFragment.newInstance())
                .commit()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            Log.d(TAG, "${menuItem.title} selected")
            when (menuItem.title) {
                "Home" -> launchFragment(HomeFragment.newInstance())
                "Activity Log" -> launchFragment(ActivityLogFragment.newInstance())
            }
            true
        }
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    companion object {
        private const val TAG = "MainActivityTag"

        fun newIntent(packageContext: Context): Intent {
            return Intent(packageContext, MainActivity::class.java)
        }
    }
}