package com.jetbrains.kmpapp.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

// 1. Define the structure for your custom color scheme
class AppColors(
    val background: Color,
    val foreground: Color,
    val card: Color,
    val cardForeground: Color,
    val popover: Color,
    val popoverForeground: Color,
    val primary: Color,
    val primaryForeground: Color,
    val secondary: Color,
    val secondaryForeground: Color,
    val muted: Color,
    val mutedForeground: Color,
    val accent: Color,
    val accentForeground: Color,
    val destructive: Color,
    val border: Color,
    val input: Color,
    val ring: Color,
    val sidebar: Color,
    val sidebarForeground: Color,
    val sidebarPrimary: Color,
    val sidebarPrimaryForeground: Color,
    val sidebarAccent: Color,
    val sidebarAccentForeground: Color,
    val sidebarBorder: Color
)

// 2. Define your specific color palettes for light and dark themes
val lightPalette = AppColors(
    background = Color(0xFFFFFFFF),
    foreground = Color(0xFF25262B),
    card = Color(0xFFFFFFFF),
    cardForeground = Color(0xFF25262B),
    popover = Color(0xFFFFFFFF),
    popoverForeground = Color(0xFF25262B),
    primary = Color(0xFF373A40),
    primaryForeground = Color(0xFFFAFAFA),
    secondary = Color(0xFFF7F7F7),
    secondaryForeground = Color(0xFF373A40),
    muted = Color(0xFFF7F7F7),
    mutedForeground = Color(0xFF8D8D92),
    accent = Color(0xFFF7F7F7),
    accentForeground = Color(0xFF373A40),
    destructive = Color(0xFF932F2F),
    border = Color(0xFFEBEBEB),
    input = Color(0xFFEBEBEB),
    ring = Color(0xFFB3B3B7),
    sidebar = Color(0xFFFAFAFA),
    sidebarForeground = Color(0xFF25262B),
    sidebarPrimary = Color(0xFF373A40),
    sidebarPrimaryForeground = Color(0xFFFAFAFA),
    sidebarAccent = Color(0xFFF7F7F7),
    sidebarAccentForeground = Color(0xFF373A40),
    sidebarBorder = Color(0xFFEBEBEB),
)

val darkPalette = AppColors(
    background = Color(0xFF202125),
    foreground = Color(0xFFFAFAFA),
    card = Color(0xFF202125),
    cardForeground = Color(0xFFFAFAFA),
    popover = Color(0xFF202125),
    popoverForeground = Color(0xFFFAFAFA),
    primary = Color(0xFFFAFAFA),
    primaryForeground = Color(0xFF373A40),
    secondary = Color(0xFF444449),
    secondaryForeground = Color(0xFFFAFAFA),
    muted = Color(0xFF444449),
    mutedForeground = Color(0xFFB3B3B7),
    accent = Color(0xFF444449),
    accentForeground = Color(0xFFFAFAFA),
    destructive = Color(0xFF9B2C2C),
    border = Color(0xFF444449),
    input = Color(0xFF444449),
    ring = Color(0xFF8D8D92),
    sidebar = Color(0xFF373A40),
    sidebarForeground = Color(0xFFFAFAFA),
    sidebarPrimary = Color(0xFF7C3AED),
    sidebarPrimaryForeground = Color(0xFFFAFAFA),
    sidebarAccent = Color(0xFF444449),
    sidebarAccentForeground = Color(0xFFFAFAFA),
    sidebarBorder = Color(0xFF444449),
)

// 3. Create a CompositionLocal to provide the colors down the tree
val LocalAppColors = staticCompositionLocalOf { darkPalette }

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // 1. Select the correct palette
//    val colors = if (useDarkTheme) darkPalette else lightPalette
    val colors = darkPalette

    // 2. Provide the selected palette to the LocalAppColors object
    CompositionLocalProvider(LocalAppColors provides colors) {
        content()
    }
}

// 3. Create an object to easily access the colors
object AppThemeObject {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
}
