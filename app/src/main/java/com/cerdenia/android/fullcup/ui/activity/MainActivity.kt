package com.cerdenia.android.fullcup.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.databinding.ActivityMainBinding
import com.cerdenia.android.fullcup.ui.fragment.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, HomeFragment.newInstance())
                .commit()
        }
    }

    companion object {
        fun newIntent(packageContext: Context): Intent {
            return Intent(packageContext, MainActivity::class.java)
        }
    }
}