package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AthleticsRepository
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("🏆 Totale Spanpunte", "🌟 Victor/Victrix", "📄 Uitslagopsomming")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "🖨️ Byeenkoms Verslae & Punte",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = GoldAccent
        )
        Text(
            text = "Totale skoolpunte, Victor & Victrix Ludorum en amptelike opsommings",
            fontSize = 13.sp,
            color = TextMuted,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        SecondaryTabRow(
            selectedTabIndex = selectedTab,
            containerColor = NavySurface,
            contentColor = GoldAccent
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) GoldAccent else TextMuted
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
            0 -> SchoolPointsTab()
            1 -> VictorVictrixTab()
            2 -> SummaryTab()
        }
    }
}

@Composable
private fun SchoolPointsTab() {
    val results by AthleticsRepository.results.collectAsState()
    val schoolPoints = remember(results) { AthleticsRepository.getSchoolPoints() }
    val maxPoints = remember(schoolPoints) { schoolPoints.maxOfOrNull { it.totalPoints } ?: 1 }

    if (schoolPoints.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Nog geen uitslae om spanpunte te bereken nie.", color = TextMuted)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "Amptelike Skole Puntetabel (Totale ASA Punte)",
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            items(schoolPoints) { sp ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavySurface),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("school_points_row_${sp.rank}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = when (sp.rank) {
                                    1 -> GoldAccent
                                    2 -> Color(0xFFC0C0C0)
                                    3 -> Color(0xFFCD7F32)
                                    else -> NavyBorder
                                },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.size(34.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${sp.rank}",
                                        fontWeight = FontWeight.Bold,
                                        color = if (sp.rank <= 3) NavyHeader else TextWhite,
                                        fontSize = 16.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = sp.school,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f)
                            )

                            Text(
                                text = "${sp.totalPoints} Punte",
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent,
                                fontSize = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Visual progress bar of points
                        val progress = (sp.totalPoints.toFloat() / maxPoints.toFloat()).coerceIn(0f, 1f)
                        LinearProgressIndicator(
                            progress = { progress },
                            color = when (sp.rank) {
                                1 -> GoldAccent
                                2 -> Color(0xFFC0C0C0)
                                3 -> Color(0xFFCD7F32)
                                else -> NavySurfaceVariant
                            },
                            trackColor = NavyBorder,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VictorVictrixTab() {
    val results by AthleticsRepository.results.collectAsState()
    val topAthletes = remember(results) { AthleticsRepository.getTopAthletes() }
    var genderFilter by remember { mutableStateOf("Almal") }

    val filtered = remember(topAthletes, genderFilter) {
        when (genderFilter) {
            "Seuns" -> topAthletes.filter { it.gender == "Seuns" }
            "Dogters" -> topAthletes.filter { it.gender == "Dogters" }
            else -> topAthletes
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Toggle Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Almal" to "Almal", "Seuns" to "Victor (Seuns)", "Dogters" to "Victrix (Dogters)").forEach { (code, label) ->
                FilterChip(
                    selected = genderFilter == code,
                    onClick = { genderFilter = code },
                    label = { Text(label, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GoldAccent,
                        selectedLabelColor = NavyHeader,
                        containerColor = NavySurface,
                        labelColor = TextWhite
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Nog geen uitslae om prestasies te rangskik nie.", color = TextMuted)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filtered) { athlete ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavySurface),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("victor_athlete_${athlete.athleteName}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                if (athlete.rank == 1) Icons.Default.EmojiEvents else Icons.Default.Star,
                                contentDescription = null,
                                tint = if (athlete.rank <= 3) GoldAccent else TextMuted,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${athlete.athleteName} (O/${athlete.ageGroup} ${athlete.gender})",
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "🏫 ${athlete.school}",
                                    color = TextMuted,
                                    fontSize = 12.sp
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${athlete.totalPoints}",
                                    fontWeight = FontWeight.Bold,
                                    color = GoldAccent,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "ASA Punte",
                                    color = TextLight,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryTab() {
    val results by AthleticsRepository.results.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "📄 Amptelike Byeenkoms Uitslagboekie",
            fontWeight = FontWeight.Bold,
            color = GoldAccent,
            fontSize = 16.sp
        )
        Text(
            text = "Opsomming van al die voltooide items van vandag se byeenkoms.",
            color = TextLight,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (results.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Geen uitslae beskikbaar vir opsomming nie.", color = TextMuted)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(results) { res ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavySurface),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${res.place}e",
                                fontWeight = FontWeight.Bold,
                                color = if (res.place == 1) GoldAccent else TextWhite,
                                fontSize = 14.sp,
                                modifier = Modifier.width(36.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(res.athleteName, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("${res.school} • ${res.eventName} (O/${res.ageGroup} ${res.gender})", color = TextMuted, fontSize = 11.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(res.resultValue, color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("${res.asaPoints} pt", color = TextLight, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
