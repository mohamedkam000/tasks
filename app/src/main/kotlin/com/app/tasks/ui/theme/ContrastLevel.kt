package com.app.tasks.ui.theme

import android.content.Context
import com.app.tasks.R

enum class ContrastLevel(val value: Float) {
    VeryLow(-1f),
    Low(-0.5f),
    Default(0f),
    Medium(0.5f),
    High(1f);

    fun getDisplayName(context: Context): String {
        val resId = when (this) {
            VeryLow -> R.string.contrast_very_low
            Low -> R.string.contrast_low
            Default -> R.string.contrast_default
            Medium -> R.string.contrast_high
            High -> R.string.contrast_very_high
        }
        return context.getString(resId)
    }

    companion object {
        fun fromFloat(float: Float) = entries.find { it.value == float } ?: Default
    }
}