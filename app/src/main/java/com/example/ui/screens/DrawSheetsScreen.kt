package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.AthleticsRepository
import com.example.data.Constants
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawSheetsScreen() {
    val athletes by AthleticsRepository.athletes.collectAsState()
    val results by AthleticsRepository.results.collectAsState()

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

    val matchingAthletes = remember(athletes, selectedAge, selectedGender, selectedEvent) {
        athletes.filter {
            it.ageGroup == selectedAge &&
                    it.gender == selectedGender &&
                    it.events.any { e -> e.equals(selectedEvent, ignoreCase = true) }
        }
    }

    val existingResults = remember(results, selectedAge, selectedGender, selectedEvent) {
        results.filter {
            it.ageGroup == selectedAge &&
                    it.gender == selectedGender &&
                    it.eventName.equals(selectedEvent, ignoreCase = true)
        }.sortedBy { it.place }
    }

    var showOfficialDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "📄 Lotingslyste & Uitslae",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = GoldAccent
        )
        Text(
            text = "Kies 'n item om amptelike loting en baanplasings te bekyk",
            fontSize = 13.sp,
            color = TextMuted,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Dropdowns Row
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

        Spacer(modifier = Modifier.height(14.dp))

        // Header for selected Item
        Surface(
            color = NavySurface,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Lotingslys: O/$selectedAge $selectedGender - $selectedEvent",
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "${matchingAthletes.size} atlete geregistreer",
                        fontSize = 12.sp,
                        color = TextLight
                    )
                }
                if (matchingAthletes.isNotEmpty()) {
                    FilledTonalButton(
                        onClick = { showOfficialDialog = true },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = GoldAccent,
                            contentColor = NavyHeader
                        ),
                        modifier = Modifier.testTag("preview_official_sheet_btn")
                    ) {
                        Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Snelbesigtiging", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (matchingAthletes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Geen atlete geregistreer vir O/$selectedAge $selectedGender - $selectedEvent nie.",
                    color = TextMuted,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    // Table header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(NavySurfaceVariant, RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text("Baan/Pos", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.width(70.dp))
                        Text("Atleet Naam", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(1f))
                        Text("Skool", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(1f))
                    }
                }

                itemsIndexed(matchingAthletes) { index, athlete ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavySurface),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = GoldAccent.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.width(55.dp)
                            ) {
                                Text(
                                    text = "Baan ${index + 1}",
                                    color = GoldAccent,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(
                                text = athlete.fullName,
                                color = TextWhite,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = athlete.school,
                                color = TextLight,
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // If results exist, display results section
                if (existingResults.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "🥇 Reeds Voltooide Uitslae vir hierdie item",
                            fontWeight = FontWeight.Bold,
                            color = GoldAccent,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }

                    itemsIndexed(existingResults) { _, res ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = NavySurfaceVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${res.place}e Plek",
                                    fontWeight = FontWeight.Bold,
                                    color = if (res.place == 1) GoldAccent else TextWhite,
                                    fontSize = 13.sp,
                                    modifier = Modifier.width(70.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(res.athleteName, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(res.school, color = TextMuted, fontSize = 12.sp)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(res.resultValue, color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("${res.asaPoints} Punte", color = TextLight, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Official Draw Sheet Popup Dialog
    if (showOfficialDialog) {
        Dialog(onDismissRequest = { showOfficialDialog = false }) {
            Surface(
                color = NavyHeader,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, GoldAccent),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "LAERSKOOL SWARTLAND",
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "AMPTELIKE LOTINGSLYS",
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Item: O/$selectedAge $selectedGender - $selectedEvent",
                        color = TextLight,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    HorizontalDivider(color = GoldAccent)

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        itemsIndexed(matchingAthletes) { idx, a ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (idx % 2 == 0) NavySurface else NavySurfaceVariant)
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Baan ${idx + 1}", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.width(60.dp))
                                Text(a.fullName, color = TextWhite, fontSize = 13.sp, modifier = Modifier.weight(1f))
                                Text(a.school, color = TextLight, fontSize = 11.sp, modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Winners podium box
                    Surface(
                        color = NavySurface,
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("AMPTELIKE PRYSWENNERS", fontWeight = FontWeight.Bold, color = GoldAccent, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("🥇 1ste Plek: ____________________________________", color = TextLight, fontSize = 11.sp)
                            Text("🥈 2de Plek:  ____________________________________", color = TextLight, fontSize = 11.sp)
                            Text("🥉 3de Plek:  ____________________________________", color = TextLight, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { showOfficialDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyHeader),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Sluit", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
