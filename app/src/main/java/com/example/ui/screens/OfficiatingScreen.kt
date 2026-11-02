package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfficiatingScreen() {
    val athletes by AthleticsRepository.athletes.collectAsState()
    val records by AthleticsRepository.records.collectAsState()
    val allResults by AthleticsRepository.results.collectAsState()

    var selectedAge by remember { mutableStateOf("13") }
    var selectedGender by remember { mutableStateOf("Seuns") }
    val availableEvents = remember(selectedAge, selectedGender) {
        Constants.getEventsForAgeAndGender(selectedAge, selectedGender)
    }
    var selectedEvent by remember { mutableStateOf("100m") }

    LaunchedEffect(availableEvents) {
        if (!availableEvents.contains(selectedEvent) && availableEvents.isNotEmpty()) {
            selectedEvent = availableEvents.first()
        }
    }

    val currentRecord = remember(records, selectedAge, selectedGender, selectedEvent) {
        AthleticsRepository.fetchEventRecord(selectedEvent, selectedGender, selectedAge)
    }

    val eventAthletes = remember(athletes, selectedAge, selectedGender, selectedEvent) {
        athletes.filter {
            it.ageGroup == selectedAge &&
                    it.gender == selectedGender &&
                    it.events.any { e -> e.equals(selectedEvent, ignoreCase = true) }
        }
    }

    val currentEventResults = remember(allResults, selectedAge, selectedGender, selectedEvent) {
        allResults.filter {
            it.ageGroup == selectedAge &&
                    it.gender == selectedGender &&
                    it.eventName.equals(selectedEvent, ignoreCase = true)
        }.sortedBy { it.place }
    }

    // Input state per athlete
    val inputValues = remember { mutableStateMapOf<Long, String>() }
    var submitMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "⏱️ Beoordeling & Uitslae",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = GoldAccent
        )
        Text(
            text = "Voer tye of afstande in; ASA punte en plekke word intyds bereken",
            fontSize = 13.sp,
            color = TextMuted,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Dropdown Selectors
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
                    label = { Text("Oud.", fontSize = 11.sp) },
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

            // Gender
            var expandedGender by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedGender,
                onExpandedChange = { expandedGender = !expandedGender },
                modifier = Modifier.weight(1.2f)
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
                    listOf("Seuns", "Dogters").forEach { g ->
                        DropdownMenuItem(
                            text = { Text(g, color = TextWhite) },
                            onClick = { selectedGender = g; expandedGender = false }
                        )
                    }
                }
            }

            // Event
            var expandedEvent by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedEvent,
                onExpandedChange = { expandedEvent = !expandedEvent },
                modifier = Modifier.weight(1.5f)
            ) {
                OutlinedTextField(
                    value = selectedEvent,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Item", fontSize = 11.sp) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEvent) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite, unfocusedTextColor = TextWhite,
                        focusedBorderColor = GoldAccent, unfocusedBorderColor = NavyBorder
                    ),
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedEvent,
                    onDismissRequest = { expandedEvent = false },
                    modifier = Modifier.background(NavySurface)
                ) {
                    availableEvents.forEach { ev ->
                        DropdownMenuItem(
                            text = { Text(ev, color = TextWhite) },
                            onClick = { selectedEvent = ev; expandedEvent = false }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Record Banner
        Card(
            colors = CardDefaults.cardColors(containerColor = NavySurface),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = GoldAccent)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    if (currentRecord != null) {
                        Text(
                            text = "Huidige Rekord: ${currentRecord.recordValue}",
                            fontWeight = FontWeight.Bold,
                            color = GoldAccent,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Gehou deur ${currentRecord.holder} (${currentRecord.year})",
                            color = TextLight,
                            fontSize = 11.sp
                        )
                    } else {
                        Text(
                            text = "Geen amptelike rekord aangeteken vir hierdie item nie",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (submitMessage != null) {
            Surface(
                color = AccentGreen.copy(alpha = 0.2f),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = submitMessage ?: "",
                    color = AccentGreen,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "Voer Uitslae In vir O/$selectedAge $selectedGender - $selectedEvent",
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent,
                    fontSize = 15.sp
                )
                Text(
                    text = "bv. '11.85' vir baan of '4.52' vir veld (of '3,45,55' vir langafstand)",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            if (eventAthletes.isEmpty()) {
                item {
                    Text(
                        text = "Geen atlete geregistreer vir hierdie item nie. Registreer eers atlete in 'Inskrywings'.",
                        color = TextMuted,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }
            } else {
                items(eventAthletes, key = { it.id }) { athlete ->
                    val rawVal = inputValues[athlete.id] ?: ""
                    val parsedVal = remember(rawVal) { Constants.parseResultToNumeric(rawVal, selectedEvent) }
                    val points = remember(parsedVal) { Constants.calculateAsaPoints(selectedEvent, parsedVal) }

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
                            Column(modifier = Modifier.weight(1.3f)) {
                                Text(
                                    text = athlete.fullName,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = athlete.school,
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }

                            // Text field for result
                            OutlinedTextField(
                                value = rawVal,
                                onValueChange = { inputValues[athlete.id] = it },
                                placeholder = { Text("0.00", fontSize = 12.sp, color = TextMuted) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite,
                                    focusedBorderColor = GoldAccent,
                                    unfocusedBorderColor = NavyBorder
                                ),
                                modifier = Modifier
                                    .width(100.dp)
                                    .testTag("result_input_${athlete.id}")
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            // Points display
                            Column(
                                modifier = Modifier.width(70.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = if (points > 0) "$points" else "-",
                                    fontWeight = FontWeight.Bold,
                                    color = if (points > 0) GoldAccent else TextMuted,
                                    fontSize = 14.sp
                                )
                                Text("punte", color = TextMuted, fontSize = 10.sp)
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Button(
                        onClick = {
                            val isTrack = Constants.isTrackEvent(selectedEvent)
                            val validEntries = eventAthletes.mapNotNull { athlete ->
                                val raw = inputValues[athlete.id] ?: ""
                                val parsed = Constants.parseResultToNumeric(raw, selectedEvent)
                                if (parsed > 0.0) {
                                    val pts = Constants.calculateAsaPoints(selectedEvent, parsed)
                                    athlete to (parsed to (raw to pts))
                                } else null
                            }

                            if (validEntries.isNotEmpty()) {
                                // Track: lowest time = 1st. Field: highest distance = 1st
                                val sorted = if (isTrack) {
                                    validEntries.sortedBy { it.second.first }
                                } else {
                                    validEntries.sortedByDescending { it.second.first }
                                }

                                val eventResults = sorted.mapIndexed { index, (ath, data) ->
                                    val (_, pair) = data
                                    val (rawStr, pts) = pair
                                    EventResult(
                                        eventName = selectedEvent,
                                        gender = selectedGender,
                                        ageGroup = selectedAge,
                                        place = index + 1,
                                        athleteId = ath.id,
                                        athleteName = ath.fullName,
                                        school = ath.school,
                                        resultValue = rawStr,
                                        asaPoints = pts
                                    )
                                }

                                AthleticsRepository.saveEventResults(
                                    eventName = selectedEvent,
                                    gender = selectedGender,
                                    ageGroup = selectedAge,
                                    newResults = eventResults
                                )
                                submitMessage = "${eventResults.size} uitslae suksesvol gesinkroniseer en gerangskik!"
                            } else {
                                submitMessage = "Voer ten minste een geldige uitslag in voor indiening."
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyHeader),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("submit_ranked_results_btn")
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("🔒 Stuur & Rangskik Uitslae", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }

            // Results Management Section
            if (currentEventResults.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "🏆 Amptelike Rangorde & Uitslae Bestuur",
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent,
                        fontSize = 15.sp
                    )
                }

                itemsIndexed(currentEventResults) { _, res ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavySurfaceVariant),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = when (res.place) {
                                    1 -> GoldAccent
                                    2 -> Color(0xFFC0C0C0)
                                    3 -> Color(0xFFCD7F32)
                                    else -> NavyBorder
                                },
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.width(60.dp)
                            ) {
                                Text(
                                    text = "${res.place}e Plek",
                                    color = if (res.place <= 3) NavyHeader else TextWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(res.athleteName, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(res.school, color = TextMuted, fontSize = 11.sp)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(res.resultValue, color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("${res.asaPoints} Punte", color = TextLight, fontSize = 11.sp)
                            }

                            IconButton(
                                onClick = {
                                    AthleticsRepository.deleteSingleAthleteResult(
                                        eventName = res.eventName,
                                        gender = res.gender,
                                        ageGroup = res.ageGroup,
                                        athleteId = res.athleteId
                                    )
                                },
                                modifier = Modifier.testTag("delete_result_${res.id}")
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Skrap uitslag", tint = AccentRed)
                            }
                        }
                    }
                }
            }
        }
    }
}
