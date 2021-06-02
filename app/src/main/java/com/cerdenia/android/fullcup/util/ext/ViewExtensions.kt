package com.cerdenia.android.fullcup.util.ext

import android.util.TypedValue
import android.view.View

fun View.addRipple() = with(TypedValue()) {
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
    setBackgroundResource(resourceId)
}

fun View.hide() = run { this.visibility = View.GONE }

fun View.show() = run { this.visibility = View.VISIBLE }