package com.example.siloli.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Navy Blue Theme
val NavyPrimary = Color(0xFF1B264F) // Deep Navy
val NavySecondary = Color(0xFF274690) // Lighter Blue
val NavyTertiary = Color(0xFF576CA8) // Muted Blue

val AccentGold = Color(0xFFC7B163) // Optional Accent

val BackgroundLight = Color(0xFFF5F7FA) // Very light grey/white for modern dashboard
val SurfaceWhite = Color(0xFFFFFFFF)

// Gradient for Banner
val BannerGradient = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFF1B264F),
        Color(0xFF303F70)
    )
)

val ErrorColor = Color(0xFFBA1A1A)
