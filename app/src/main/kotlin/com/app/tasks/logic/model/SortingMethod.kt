package com.app.tasks.logic.model

import android.content.Context
import com.app.tasks.R

enum class SortingMethod(val id: Int) {
    Sequential(1),
    Category(2),
    Priority(3),
    Completion(4),
    AlphabeticalAscending(5),
    AlphabeticalDescending(6);

    fun getDisplayName(context: Context): String {
        val resId = when (this) {
            Sequential -> R.string.sorting_sequential
            Category -> R.string.sorting_category
            Priority -> R.string.sorting_priority
            Completion -> R.string.sorting_completion
            AlphabeticalAscending -> R.string.sorting_alphabetical_ascending
            AlphabeticalDescending -> R.string.sorting_alphabetical_descending
        }
        return context.getString(resId)
    }

    companion object {
        fun fromId(id: Int) = entries.find { it.id == id } ?: Sequential
    }
}