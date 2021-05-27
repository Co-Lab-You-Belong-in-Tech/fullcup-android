package com.cerdenia.android.fullcup.util.ext

import android.text.Editable

fun String?.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)