package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
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
import com.example.data.Constants
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveFeedScreen() {
    val results by AthleticsRepository.results.collectAsState()

    var selectedSchoolFilter by remember { mutableStateOf("Alle Skole") }
    var selectedAgeFilter by remember { mutableStateOf("Alle Ouderdomme") }

    val schools = remember(results) {
        listOf("Alle Skole") + results.map { it.school }.distinct().sorted()
    }

    val filteredResults = remember(results, selectedSchoolFilter, selectedAgeFilter) {
        results.filter { r ->
            val matchesSchool = selectedSchoolFilter == "Alle Skole" || r.school == selectedSchoolFilter
            val matchesAge = selectedAgeFilter == "Alle Ouderdomme" || r.ageGroup == selectedAgeFilter
            matchesSchool && matchesAge
        }.sortedByDescending { it.timestamp }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "📊 Regstreekse Uitslae",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent
                )
                Text(
                    text = "Intydse voer van voltooide items vir ouers en beamptes",
                    fontSize = 13.sp,
                    color = TextMuted
                )
            }

            // Live Pulse Pill
            Surface(
                color = AccentRed.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(AccentRed, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("INTYDS", color = AccentRed, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Filters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // School filter
            var expandedSchool by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedSchool,
                onExpandedChange = { expandedSchool = !expandedSchool },
                modifier = Modifier.weight(1.3f)
            ) {
                OutlinedTextField(
                    value = selectedSchoolFilter,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Filter Skool", fontSize = 11.sp) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSchool) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite, unfocusedTextColor = TextWhite,
                        focusedBorderColor = GoldAccent, unfocusedBorderColor = NavyBorder
                    ),
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedSchool,
                    onDismissRequest = { expandedSchool = false },
                    modifier = Modifier.background(NavySurface)
                ) {
                    schools.forEach { s ->
                        DropdownMenuItem(
                            text = { Text(s, color = TextWhite) },
                            onClick = { selectedSchoolFilter = s; expandedSchool = false }
                        )
                    }
                }
            }

            // Age filter
            var expandedAge by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedAge,
                onExpandedChange = { expandedAge = !expandedAge },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = if (selectedAgeFilter == "Alle Ouderdomme") "Alle Oud." else "O/$selectedAgeFilter",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Filter Ouderdom", fontSize = 11.sp) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAge) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite, unfocusedTextColor = TextWhite,
                        focusedBorderColor = GoldAccent, unfocusedBorderColor = NavyBorder
                    ),
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedAge,
                    onDismissRequest = { expandedAge = false },
                    modifier = Modifier.background(NavySurface)
                ) {
                    (listOf("Alle Ouderdomme") + Constants.AGE_GROUPS).forEach { a ->
                        DropdownMenuItem(
                            text = { Text(if (a == "Alle Ouderdomme") a else "O/$a", color = TextWhite) },
                            onClick = { selectedAgeFilter = a; expandedAge = false }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Vasgelegde Uitslae: ${filteredResults.size}",
            color = GoldAccent,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        if (filteredResults.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Geen uitslae gevind nie. Uitslae verskyn hier sodra beamptes dit vaslê.",
                    color = TextMuted,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredResults, key = { it.id }) { item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavySurface),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("live_result_card_${item.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Place badge
                            Surface(
                                color = when (item.place) {
                                    1 -> GoldAccent
                                    2 -> Color(0xFFC0C0C0)
                                    3 -> Color(0xFFCD7F32)
                                    else -> NavyBorder
                                },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${item.place}",
                                        color = if (item.place <= 3) NavyHeader else TextWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.athleteName,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "🏫 ${item.school}",
                                    color = TextMuted,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "${item.eventName} • O/${item.ageGroup} ${item.gender}",
                                    color = GoldAccent,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = item.resultValue,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldAccent,
                                    fontSize = 16.sp
                                )
                                Surface(
                                    color = GoldAccent.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "${item.asaPoints} ASA Punte",
                                        fontWeight = FontWeight.SemiBold,
                                        color = GoldAccent,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
