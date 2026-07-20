package com.rhyan57.rnce.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object AppColors {
    val Background    = Color(0xFF0A0A0F)
    val Surface       = Color(0xFF13131A)
    val SurfaceVariant= Color(0xFF1C1C26)
    val Primary       = Color(0xFF7C3AED)
    val PrimaryVariant= Color(0xFF9D5CF6)
    val Secondary     = Color(0xFF06B6D4)
    val Accent        = Color(0xFFEC4899)
    val Success       = Color(0xFF22C55E)
    val Error         = Color(0xFFEF4444)
    val Warning       = Color(0xFFF59E0B)
    val TextPrimary   = Color(0xFFF1F5F9)
    val TextSecondary = Color(0xFF94A3B8)
    val TextMuted     = Color(0xFF475569)
    val Divider       = Color(0xFF1E293B)
    val OnPrimary     = Color(0xFFFFFFFF)
    val OnError       = Color(0xFFFFE4E4)
    val ErrorContainer= Color(0xFF2D0A0A)
    val NfcActive     = Color(0xFF7C3AED)
    val NfcInactive   = Color(0xFFEF4444)
}

object Radius {
    val Card   = RoundedCornerShape(16.dp)
    val Button = RoundedCornerShape(12.dp)
    val Small  = RoundedCornerShape(8.dp)
    val Badge  = RoundedCornerShape(6.dp)
    val Preset = RoundedCornerShape(20.dp)
}
