package com.cerdenia.android.fullcup.util.ext

fun <K, V> Map<K, V>.getKey(value: V): K? {
    this.forEach { pair -> if (pair.value == value) return pair.key }
    return null
}