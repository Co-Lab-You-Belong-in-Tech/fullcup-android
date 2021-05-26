package com.cerdenia.android.fullcup.ui.viewmodel

import android.graphics.Color
import androidx.lifecycle.ViewModel
import app.futured.donut.DonutSection
import com.cerdenia.android.fullcup.data.local.FullCupPreferences
import com.cerdenia.android.fullcup.data.model.ColoredCategory

class HomeViewModel : ViewModel() {
    // Temporary colors
    private val colors = listOf(Color.RED, Color.YELLOW, Color.BLUE,
        Color.GREEN, Color.CYAN, Color.MAGENTA)

    val categories get() = FullCupPreferences.categories?.toList()?.sorted() ?: emptyList()
    val donutCap = categories.size.toFloat()
    val donutSections = categories.mapIndexed { i, category ->
        DonutSection(
            name = category,
            amount = 1f,
            color = colors[i]
        )
    }

    val coloredCategories = categories.mapIndexed { i, category ->
        ColoredCategory(category, colors[i])
    }
}