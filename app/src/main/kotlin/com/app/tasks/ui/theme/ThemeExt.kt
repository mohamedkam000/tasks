package com.app.tasks.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.kyant.m3color.hct.Hct
import com.kyant.m3color.scheme.SchemeContent
import com.kyant.m3color.scheme.SchemeExpressive
import com.kyant.m3color.scheme.SchemeFidelity
import com.kyant.m3color.scheme.SchemeFruitSalad
import com.kyant.m3color.scheme.SchemeMonochrome
import com.kyant.m3color.scheme.SchemeNeutral
import com.kyant.m3color.scheme.SchemeRainbow
import com.kyant.m3color.scheme.SchemeTonalSpot
import com.kyant.m3color.scheme.SchemeVibrant

@Composable
@Stable
fun dynamicColorScheme(
    keyColor: Color,
    isDark: Boolean,
    style: PaletteStyle = PaletteStyle.TonalSpot,
    contrastLevel: Double = 0.0,
    animationSpec: AnimationSpec<Color> = spring()
): ColorScheme {
    val hct = Hct.fromInt(keyColor.toArgb())
    val scheme = when (style) {
        PaletteStyle.TonalSpot -> SchemeTonalSpot(hct, isDark, contrastLevel)
        PaletteStyle.Neutral -> SchemeNeutral(hct, isDark, contrastLevel)
        PaletteStyle.Vibrant -> SchemeVibrant(hct, isDark, contrastLevel)
        PaletteStyle.Expressive -> SchemeExpressive(hct, isDark, contrastLevel)
        PaletteStyle.Rainbow -> SchemeRainbow(hct, isDark, contrastLevel)
        PaletteStyle.FruitSalad -> SchemeFruitSalad(hct, isDark, contrastLevel)
        PaletteStyle.Monochrome -> SchemeMonochrome(hct, isDark, contrastLevel)
        PaletteStyle.Fidelity -> SchemeFidelity(hct, isDark, contrastLevel)
        PaletteStyle.Content -> SchemeContent(hct, isDark, contrastLevel)
    }

    return ColorScheme(
        primary = scheme.primary.toColor().animate(animationSpec),
        onPrimary = scheme.onPrimary.toColor().animate(animationSpec),
        primaryContainer = scheme.primaryContainer.toColor().animate(animationSpec),
        onPrimaryContainer = scheme.onPrimaryContainer.toColor().animate(animationSpec),
        inversePrimary = scheme.inversePrimary.toColor().animate(animationSpec),
        secondary = scheme.secondary.toColor().animate(animationSpec),
        onSecondary = scheme.onSecondary.toColor().animate(animationSpec),
        secondaryContainer = scheme.secondaryContainer.toColor().animate(animationSpec),
        onSecondaryContainer = scheme.onSecondaryContainer.toColor().animate(animationSpec),
        tertiary = scheme.tertiary.toColor().animate(animationSpec),
        onTertiary = scheme.onTertiary.toColor().animate(animationSpec),
        tertiaryContainer = scheme.tertiaryContainer.toColor().animate(animationSpec),
        onTertiaryContainer = scheme.onTertiaryContainer.toColor().animate(animationSpec),
        background = scheme.background.toColor().animate(animationSpec),
        onBackground = scheme.onBackground.toColor().animate(animationSpec),
        surface = scheme.surface.toColor().animate(animationSpec),
        onSurface = scheme.onSurface.toColor().animate(animationSpec),
        surfaceVariant = scheme.surfaceVariant.toColor().animate(animationSpec),
        onSurfaceVariant = scheme.onSurfaceVariant.toColor().animate(animationSpec),
        surfaceTint = scheme.surfaceTint.toColor().animate(animationSpec),
        inverseSurface = scheme.inverseSurface.toColor().animate(animationSpec),
        inverseOnSurface = scheme.inverseOnSurface.toColor().animate(animationSpec),
        error = scheme.error.toColor().animate(animationSpec),
        onError = scheme.onError.toColor().animate(animationSpec),
        errorContainer = scheme.errorContainer.toColor().animate(animationSpec),
        onErrorContainer = scheme.onErrorContainer.toColor().animate(animationSpec),
        outline = scheme.outline.toColor().animate(animationSpec),
        outlineVariant = scheme.outlineVariant.toColor().animate(animationSpec),
        scrim = scheme.scrim.toColor().animate(animationSpec),
        surfaceBright = scheme.surfaceBright.toColor().animate(animationSpec),
        surfaceDim = scheme.surfaceDim.toColor().animate(animationSpec),
        surfaceContainer = scheme.surfaceContainer.toColor().animate(animationSpec),
        surfaceContainerHigh = scheme.surfaceContainerHigh.toColor().animate(animationSpec),
        surfaceContainerHighest = scheme.surfaceContainerHighest.toColor().animate(animationSpec),
        surfaceContainerLow = scheme.surfaceContainerLow.toColor().animate(animationSpec),
        surfaceContainerLowest = scheme.surfaceContainerLowest.toColor().animate(animationSpec),
        primaryFixed = scheme.primaryFixed.toColor().animate(animationSpec),
        primaryFixedDim = scheme.primaryFixedDim.toColor().animate(animationSpec),
        onPrimaryFixed = scheme.onPrimaryFixed.toColor().animate(animationSpec),
        onPrimaryFixedVariant = scheme.onPrimaryFixedVariant.toColor().animate(animationSpec),
        secondaryFixed = scheme.secondaryFixed.toColor().animate(animationSpec),
        secondaryFixedDim = scheme.secondaryFixedDim.toColor().animate(animationSpec),
        onSecondaryFixed = scheme.onSecondaryFixed.toColor().animate(animationSpec),
        onSecondaryFixedVariant = scheme.onSecondaryFixedVariant.toColor().animate(animationSpec),
        tertiaryFixed = scheme.tertiaryFixed.toColor().animate(animationSpec),
        tertiaryFixedDim = scheme.tertiaryFixedDim.toColor().animate(animationSpec),
        onTertiaryFixed = scheme.onTertiaryFixed.toColor().animate(animationSpec),
        onTertiaryFixedVariant = scheme.onTertiaryFixedVariant.toColor().animate(animationSpec),
    )
}

@Suppress("NOTHING_TO_INLINE")
private inline fun Int.toColor(): Color = Color(this)

@Composable
private fun Color.animate(animationSpec: AnimationSpec<Color> = spring()): Color {
    return animateColorAsState(this, animationSpec).value
}