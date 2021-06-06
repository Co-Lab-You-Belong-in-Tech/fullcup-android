package com.cerdenia.android.fullcup.ui.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cerdenia.android.fullcup.R

abstract class FullCupActivity : AppCompatActivity() {
    var fragmentContainer: Int = R.id.fragment_container

    fun replaceFragmentWith(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(fragmentContainer, fragment)
            .commit()
    }

    fun replaceFragmentWith(fragment: Fragment, addToBackStack: Boolean) {
        supportFragmentManager
            .beginTransaction()
            .replace(fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}