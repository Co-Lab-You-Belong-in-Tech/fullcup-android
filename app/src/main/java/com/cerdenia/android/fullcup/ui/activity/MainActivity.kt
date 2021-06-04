package com.cerdenia.android.fullcup.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
            when (menuItem.itemId) {
                R.id.nav_home -> HomeFragment.newInstance().launch()
                R.id.nav_activity_log -> ActivityLogFragment.newInstance().launch()
                R.id.nav_progress -> { } // TODO
                R.id.nav_account -> { } // TODO
            }
            true
        }
    }

    private fun Fragment.launch() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, this)
            .commit()
    }

    companion object {
        private const val TAG = "MainActivityTag"

        fun newIntent(packageContext: Context): Intent {
            return Intent(packageContext, MainActivity::class.java)
        }
    }
}