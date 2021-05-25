package com.cerdenia.android.fullcup.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.data.model.ColoredCategory
import com.google.android.material.imageview.ShapeableImageView
import java.lang.IllegalArgumentException

class CategoryAdapter(var categories: List<Any>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    override fun getItemViewType(position: Int): Int {
        return when (categories[position]) {
            is ColoredCategory -> TYPE_COLORED
            is String -> TYPE_CHECKBOX
            else -> throw IllegalArgumentException()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // TODO: change layout depending on viewType
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_colored_category, parent, false)
        return ColoredCategoryHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val category = categories[position]
        (holder as ColoredCategoryHolder).bind(category as ColoredCategory)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    private inner class ColoredCategoryHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var coloredCategory: ColoredCategory
        val categoryTextView: TextView = view.findViewById(R.id.category_text_view)
        val colorView: ShapeableImageView = view.findViewById(R.id.color_view)

        fun bind(coloredCategory: ColoredCategory) {
            categoryTextView.text = coloredCategory.category
            colorView.setBackgroundColor(coloredCategory.color)
        }
    }

    private inner class CategoryCheckboxHolder(view: View) : RecyclerView.ViewHolder(view) {
        // TODO
    }

    companion object {
        private const val TYPE_COLORED = 0
        private const val TYPE_CHECKBOX = 1
    }
}