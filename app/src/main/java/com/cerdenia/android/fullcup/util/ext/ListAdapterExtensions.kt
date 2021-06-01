package com.cerdenia.android.fullcup.util.ext

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Update the [RecyclerView]'s [ListAdapter] with the provided list of items.
 * See https://stackoverflow.com/questions/49726385/listadapter-not-updating-item-in-reyclerview
 * for an overview of this (annoying) issue.
 *
 * This is slight modification of a solution by 'Bojan P.'
 * Added call to notifyDataSetChanged to ensure the RecyclerView reflects
 * all changes to data, in case the ListAdapter does not catch them.
 */
fun <T, VH : RecyclerView.ViewHolder> ListAdapter<T, VH>.updateList(list: List<T>?) {
    val areListsTheSame = list == this.currentList
    this.submitList(if (areListsTheSame) list?.toList() else list)
    if (areListsTheSame) notifyDataSetChanged()
}