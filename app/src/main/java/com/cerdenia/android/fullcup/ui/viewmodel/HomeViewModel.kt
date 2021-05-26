package com.cerdenia.android.fullcup.ui.viewmodel

import android.graphics.Color
import androidx.lifecycle.ViewModel
import app.futured.donut.DonutSection
import com.cerdenia.android.fullcup.data.local.FullCupPreferences
import com.cerdenia.android.fullcup.data.model.ColoredCategory

class HomeViewModel : ViewModel() {
    private val categories = FullCupPreferences.categories?.toList()?.sorted() ?: emptyList()
    val donutCap = categories.size.toFloat()
    val donutSections = categories.map { category ->
        DonutSection(
            name = category,
            amount = 1f,
            color = Color.parseColor("#FFB98E")
        )
    }

    val coloredCategories = categories.map { category ->
        val colors = listOf<Int>(Color.RED, Color.BLUE, Color.GREEN, Color.CYAN) // temporary
        ColoredCategory(category, colors.shuffled().first())
    }
}