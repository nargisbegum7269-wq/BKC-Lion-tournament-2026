package com.example.ui.theme

import androidx.compose.ui.graphics.Color

val GamingBg = Color(0xFF0A0D14)
val GamingCard = Color(0xFF121824)
val GamingCardElevated = Color(0xFF1B2436)
val GamingSurfaceBorder = Color(0xFF222F45)

val GamingAccentRed = Color(0xFFFF4655)
val GamingAccentOrange = Color(0xFFFF6B35)
val GamingGold = Color(0xFFFFB703)
val NagadRed = Color(0xFFD62828)
val RocketPurple = Color(0xFF8B5CF6)
val GamingEmerald = Color(0xFF10B981)

val TextPrimary = Color(0xFFF1F5F9)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

val DarkColorScheme = androidx.compose.material3.darkColorScheme(
    primary = GamingAccentRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF3B1217),
    onPrimaryContainer = Color(0xFFFFD9DD),
    secondary = GamingGold,
    onSecondary = Color(0xFF2E1C00),
    secondaryContainer = Color(0xFF3D2D00),
    onSecondaryContainer = Color(0xFFFFE082),
    tertiary = RocketPurple,
    onTertiary = Color.White,
    background = GamingBg,
    onBackground = TextPrimary,
    surface = GamingCard,
    onSurface = TextPrimary,
    surfaceVariant = GamingCardElevated,
    onSurfaceVariant = TextSecondary,
    outline = GamingSurfaceBorder
)
