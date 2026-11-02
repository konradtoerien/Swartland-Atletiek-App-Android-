package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AthleticsRepository
import com.example.data.Constants
import com.example.data.EventRecord
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("📊 Huidige Rekords", "✍️ Nuwe Rekord")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "🏆 Rekordbestuur",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = GoldAccent
        )
        Text(
            text = "Bekyk en bestuur amptelike byeenkomsrekords",
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
            0 -> RecordsListTab()
            1 -> AddRecordTab(onRecordAdded = { selectedTab = 0 })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecordsListTab() {
    val records by AthleticsRepository.records.collectAsState()
    var selectedGender by remember { mutableStateOf("Alle Geslagte") }
    var selectedAge by remember { mutableStateOf("Alle Ouderdomme") }

    val filteredRecords = records.filter { r ->
        val matchesGender = selectedGender == "Alle Geslagte" || r.gender == selectedGender
        val matchesAge = selectedAge == "Alle Ouderdomme" || r.ageGroup == selectedAge
        matchesGender && matchesAge
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Gender
            var expandedGender by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedGender,
                onExpandedChange = { expandedGender = !expandedGender },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = selectedGender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Geslag", fontSize = 11.sp) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGender) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite, unfocusedTextColor = TextWhite,
                        focusedBorderColor = GoldAccent, unfocusedBorderColor = NavyBorder
                    ),
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedGender,
                    onDismissRequest = { expandedGender = false },
                    modifier = Modifier.background(NavySurface)
                ) {
                    listOf("Alle Geslagte", "Seuns", "Dogters").forEach { g ->
                        DropdownMenuItem(
                            text = { Text(g, color = TextWhite) },
                            onClick = { selectedGender = g; expandedGender = false }
                        )
                    }
                }
            }

            // Age
            var expandedAge by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedAge,
                onExpandedChange = { expandedAge = !expandedAge },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = if (selectedAge == "Alle Ouderdomme") "Alle Oud." else "O/$selectedAge",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Ouderdom", fontSize = 11.sp) },
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
                    (listOf("Alle Ouderdomme") + Constants.AGE_GROUPS).forEach { age ->
                        DropdownMenuItem(
                            text = { Text(if (age == "Alle Ouderdomme") age else "O/$age", color = TextWhite) },
                            onClick = { selectedAge = age; expandedAge = false }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Totaal Aangetekende Rekords: ${filteredRecords.size}",
            color = GoldAccent,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        if (filteredRecords.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Geen rekords gevind vir hierdie filters nie.", color = TextMuted)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredRecords, key = { it.id }) { record ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavySurface),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("record_card_${record.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${record.eventName} (O/${record.ageGroup} ${record.gender})",
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "Rekord: ${record.recordValue}",
                                    fontWeight = FontWeight.Bold,
                                    color = GoldAccent,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "${record.holder} (${record.year})",
                                    color = TextMuted,
                                    fontSize = 12.sp
                                )
                            }
                            IconButton(
                                onClick = { AthleticsRepository.deleteRecord(record.id) },
                                modifier = Modifier.testTag("delete_record_${record.id}")
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Skrap rekord", tint = AccentRed)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddRecordTab(onRecordAdded: () -> Unit) {
    var selectedEvent by remember { mutableStateOf(Constants.ALL_DISCIPLINES.first()) }
    var selectedGender by remember { mutableStateOf("Seuns") }
    var selectedAge by remember { mutableStateOf("13") }
    var recordValue by remember { mutableStateOf("") }
    var holder by remember { mutableStateOf("Laerskool Swartland") }
    var yearStr by remember { mutableStateOf("2026") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("✍️ Voeg Enkele Rekord By", fontWeight = FontWeight.Bold, color = GoldAccent, fontSize = 16.sp)
        }

        item {
            // Event
            var expandedEvent by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedEvent,
                onExpandedChange = { expandedEvent = !expandedEvent },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedEvent,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Item / Dissipline") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEvent) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite, unfocusedTextColor = TextWhite,
                        focusedBorderColor = GoldAccent, unfocusedBorderColor = NavyBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedEvent,
                    onDismissRequest = { expandedEvent = false },
                    modifier = Modifier.background(NavySurface)
                ) {
                    Constants.ALL_DISCIPLINES.forEach { disc ->
                        DropdownMenuItem(
                            text = { Text(disc, color = TextWhite) },
                            onClick = { selectedEvent = disc; expandedEvent = false }
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Gender
                var expandedGender by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedGender,
                    onExpandedChange = { expandedGender = !expandedGender },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedGender,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Geslag") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGender) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite, unfocusedTextColor = TextWhite,
                            focusedBorderColor = GoldAccent, unfocusedBorderColor = NavyBorder
                        ),
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedGender,
                        onDismissRequest = { expandedGender = false },
                        modifier = Modifier.background(NavySurface)
                    ) {
                        listOf("Seuns", "Dogters").forEach { g ->
                            DropdownMenuItem(
                                text = { Text(g, color = TextWhite) },
                                onClick = { selectedGender = g; expandedGender = false }
                            )
                        }
                    }
                }

                // Age
                var expandedAge by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedAge,
                    onExpandedChange = { expandedAge = !expandedAge },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = "O/$selectedAge",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Ouderdom") },
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
                        Constants.AGE_GROUPS.forEach { age ->
                            DropdownMenuItem(
                                text = { Text("O/$age", color = TextWhite) },
                                onClick = { selectedAge = age; expandedAge = false }
                            )
                        }
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = recordValue,
                onValueChange = { recordValue = it },
                label = { Text("Tyd of Afstand (bv. 11.85s of 10.42m)") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextWhite, unfocusedTextColor = TextWhite,
                    focusedBorderColor = GoldAccent, unfocusedBorderColor = NavyBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("record_value_input")
            )
        }

        item {
            OutlinedTextField(
                value = holder,
                onValueChange = { holder = it },
                label = { Text("Skool of Atleet Naam") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextWhite, unfocusedTextColor = TextWhite,
                    focusedBorderColor = GoldAccent, unfocusedBorderColor = NavyBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("record_holder_input")
            )
        }

        item {
            OutlinedTextField(
                value = yearStr,
                onValueChange = { yearStr = it },
                label = { Text("Jaar") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextWhite, unfocusedTextColor = TextWhite,
                    focusedBorderColor = GoldAccent, unfocusedBorderColor = NavyBorder
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (errorMsg != null) {
            item {
                Text(errorMsg ?: "", color = AccentRed, fontSize = 13.sp)
            }
        }

        item {
            Button(
                onClick = {
                    if (recordValue.isBlank() || holder.isBlank()) {
                        errorMsg = "Vul asseblief alle velde in."
                    } else {
                        val year = yearStr.toIntOrNull() ?: 2026
                        AthleticsRepository.addRecord(
                            EventRecord(
                                eventName = selectedEvent,
                                gender = selectedGender,
                                ageGroup = selectedAge,
                                recordValue = recordValue.trim(),
                                holder = holder.trim(),
                                year = year
                            )
                        )
                        onRecordAdded()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyHeader),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_record_btn")
            ) {
                Text("Stoor Rekord", fontWeight = FontWeight.Bold)
            }
        }
    }
}
