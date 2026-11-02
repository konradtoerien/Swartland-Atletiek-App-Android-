package com.example.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppDestination(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val isPrimaryBottom: Boolean = false
) {
    SETUP("setup", "Inskrywings", Icons.Default.ListAlt, true),
    DRAWS("draws", "Lotingslyste", Icons.Default.Description, true),
    OFFICIATING("officiating", "Beoordeling", Icons.Default.Timer, true),
    LIVE_FEED("live_feed", "Live Feed", Icons.Default.BarChart, true),
    PROGRAM("program", "Program", Icons.Default.CalendarToday, true),
    RECORDS("records", "Rekords", Icons.Default.EmojiEvents, false),
    REPORTS("reports", "Verslae", Icons.Default.Print, false)
}
