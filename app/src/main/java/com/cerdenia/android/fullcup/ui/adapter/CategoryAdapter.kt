package com.cerdenia.android.fullcup.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cerdenia.android.fullcup.R
import com.cerdenia.android.fullcup.data.model.ColoredCategory
import com.cerdenia.android.fullcup.data.model.SelectableCategory
import com.google.android.material.imageview.ShapeableImageView

class CategoryAdapter(
    var categories: List<Any>,
    private val listener: Listener? = null
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    interface Listener {
        fun onCheckboxItemClicked(category: String, isChecked: Boolean)
    }

    override fun getItemViewType(position: Int): Int {
        return when (categories[position]) {
            is ColoredCategory -> TYPE_COLORED
            is SelectableCategory -> TYPE_CHECKBOX
            else -> throw Exception("Unexpected item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_COLORED -> {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.list_item_colored_category, parent, false)
                ColoredCategoryHolder(view)
            }
            TYPE_CHECKBOX -> {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.list_item_category_checkbox, parent, false)
                CategoryCheckboxHolder(view)
            }
            else -> throw Exception("Unexpected item type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val category = categories[position]
        when (holder) {
            is ColoredCategoryHolder -> holder.bind(category as ColoredCategory)
            is CategoryCheckboxHolder -> holder.bind(category as SelectableCategory)
            else -> throw Exception("Unexpected item type")
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    private inner class ColoredCategoryHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryTextView: TextView? = view.findViewById(R.id.category_text_view)
        val colorView: ShapeableImageView? = view.findViewById(R.id.color_view)

        fun bind(coloredCategory: ColoredCategory) {
            categoryTextView?.text = coloredCategory.category
            colorView?.setBackgroundColor(coloredCategory.color)
        }
    }

    private inner class CategoryCheckboxHolder(view: View) :
        RecyclerView.ViewHolder(view),
        View.OnClickListener
    {
        private lateinit var category: String
        val categoryCheckBox: CheckBox? = view.findViewById(R.id.category_checkbox)

        init { categoryCheckBox?.setOnClickListener(this) }

        fun bind(selectableCategory: SelectableCategory) {
            category = selectableCategory.category
            categoryCheckBox?.text = category
            categoryCheckBox?.isChecked = selectableCategory.isSelected
        }

        override fun onClick(p0: View?) {
            Log.d(TAG, "click!")
            val isChecked = categoryCheckBox?.isChecked ?: false
            listener?.onCheckboxItemClicked(category, isChecked)
        }
    }

    companion object {
        private const val TAG = "CategoryAdapter"
        private const val TYPE_COLORED = 0
        private const val TYPE_CHECKBOX = 1
    }
}