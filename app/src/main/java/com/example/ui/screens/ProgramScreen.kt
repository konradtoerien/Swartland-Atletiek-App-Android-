package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Schedule
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
import com.example.data.ProgramItem
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("📋 Tydrooster", "✍️ Voeg Item By")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "📅 Byeenkoms Program & Tydrooster",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = GoldAccent
        )
        Text(
            text = "Bestuur die dag se skedule en hou vordering dop",
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
            0 -> ProgramListTab()
            1 -> AddProgramItemTab(onItemAdded = { selectedTab = 0 })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProgramListTab() {
    val programItems by AthleticsRepository.program.collectAsState()
    var selectedVenueFilter by remember { mutableStateOf("Alle Plekke") }
    var selectedStatusFilter by remember { mutableStateOf("Alle Statusse") }

    val venues = remember(programItems) {
        listOf("Alle Plekke") + programItems.map { it.venue }.distinct().sorted()
    }

    val filteredItems = remember(programItems, selectedVenueFilter, selectedStatusFilter) {
        programItems.filter { item ->
            val matchesVenue = selectedVenueFilter == "Alle Plekke" || item.venue == selectedVenueFilter
            val matchesStatus = selectedStatusFilter == "Alle Statusse" || item.status == selectedStatusFilter
            matchesVenue && matchesStatus
        }.sortedBy { it.itemNo }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Venue
            var expandedVenue by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedVenue,
                onExpandedChange = { expandedVenue = !expandedVenue },
                modifier = Modifier.weight(1.3f)
            ) {
                OutlinedTextField(
                    value = selectedVenueFilter,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Filter Plek/Baan", fontSize = 11.sp) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedVenue) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite, unfocusedTextColor = TextWhite,
                        focusedBorderColor = GoldAccent, unfocusedBorderColor = NavyBorder
                    ),
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedVenue,
                    onDismissRequest = { expandedVenue = false },
                    modifier = Modifier.background(NavySurface)
                ) {
                    venues.forEach { v ->
                        DropdownMenuItem(
                            text = { Text(v, color = TextWhite) },
                            onClick = { selectedVenueFilter = v; expandedVenue = false }
                        )
                    }
                }
            }

            // Status
            var expandedStatus by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedStatus,
                onExpandedChange = { expandedStatus = !expandedStatus },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = selectedStatusFilter,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Status", fontSize = 11.sp) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite, unfocusedTextColor = TextWhite,
                        focusedBorderColor = GoldAccent, unfocusedBorderColor = NavyBorder
                    ),
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedStatus,
                    onDismissRequest = { expandedStatus = false },
                    modifier = Modifier.background(NavySurface)
                ) {
                    listOf("Alle Statusse", "Wagtend", "Besig", "Voltooi").forEach { st ->
                        DropdownMenuItem(
                            text = { Text(st, color = TextWhite) },
                            onClick = { selectedStatusFilter = st; expandedStatus = false }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Gelys: ${filteredItems.size} items (Tik op status om te verander)",
            color = GoldAccent,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        if (filteredItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Geen programitems vir hierdie filters nie.", color = TextMuted)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavySurface),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("program_item_${item.itemNo}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Item number pill
                            Surface(
                                color = NavySurfaceVariant,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "#${item.itemNo}",
                                        fontWeight = FontWeight.Bold,
                                        color = GoldAccent,
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${item.eventName} (O/${item.ageGroup} ${item.gender})",
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite,
                                    fontSize = 14.sp
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 2.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = TextMuted,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(item.startTime, color = TextLight, fontSize = 12.sp)
                                    Text(" • ${item.venue}", color = TextMuted, fontSize = 12.sp)
                                }
                            }

                            // Interactive status badge that cycles on tap: Wagtend -> Besig -> Voltooi
                            Surface(
                                color = when (item.status) {
                                    "Voltooi" -> AccentGreen.copy(alpha = 0.2f)
                                    "Besig" -> GoldAccent.copy(alpha = 0.25f)
                                    else -> NavyBorder
                                },
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .clickable {
                                        val nextStatus = when (item.status) {
                                            "Wagtend" -> "Besig"
                                            "Besig" -> "Voltooi"
                                            else -> "Wagtend"
                                        }
                                        AthleticsRepository.updateProgramItemStatus(item.id, nextStatus)
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = item.status,
                                    fontWeight = FontWeight.Bold,
                                    color = when (item.status) {
                                        "Voltooi" -> AccentGreen
                                        "Besig" -> GoldAccent
                                        else -> TextWhite
                                    },
                                    fontSize = 12.sp
                                )
                            }

                            IconButton(
                                onClick = { AthleticsRepository.deleteProgramItem(item.id) },
                                modifier = Modifier.testTag("delete_program_item_${item.id}")
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Skrap item", tint = AccentRed)
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
private fun AddProgramItemTab(onItemAdded: () -> Unit) {
    val currentProgram by AthleticsRepository.program.collectAsState()
    var itemNoStr by remember { mutableStateOf((currentProgram.size + 1).toString()) }
    var startTime by remember { mutableStateOf("08:00") }
    var selectedAge by remember { mutableStateOf("13") }
    var selectedGender by remember { mutableStateOf("Seuns") }
    val availableEvents = remember(selectedAge, selectedGender) {
        Constants.getEventsForAgeAndGender(selectedAge, selectedGender)
    }
    var selectedEvent by remember { mutableStateOf("100m") }
    var venue by remember { mutableStateOf("Hoofbaan") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(availableEvents) {
        if (!availableEvents.contains(selectedEvent) && availableEvents.isNotEmpty()) {
            selectedEvent = availableEvents.first()
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("✍️ Voeg Item by Program By", fontWeight = FontWeight.Bold, color = GoldAccent, fontSize = 16.sp)
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = itemNoStr,
                    onValueChange = { itemNoStr = it },
                    label = { Text("Item No") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite, unfocusedTextColor = TextWhite,
                        focusedBorderColor = GoldAccent, unfocusedBorderColor = NavyBorder
                    ),
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = { Text("Begin Tyd") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite, unfocusedTextColor = TextWhite,
                        focusedBorderColor = GoldAccent, unfocusedBorderColor = NavyBorder
                    ),
                    modifier = Modifier.weight(1f)
                )
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
                    availableEvents.forEach { ev ->
                        DropdownMenuItem(
                            text = { Text(ev, color = TextWhite) },
                            onClick = { selectedEvent = ev; expandedEvent = false }
                        )
                    }
                }
            }
        }

        item {
            OutlinedTextField(
                value = venue,
                onValueChange = { venue = it },
                label = { Text("Plek / Baan") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextWhite, unfocusedTextColor = TextWhite,
                    focusedBorderColor = GoldAccent, unfocusedBorderColor = NavyBorder
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (errorMessage != null) {
            item {
                Text(errorMessage ?: "", color = AccentRed, fontSize = 13.sp)
            }
        }

        item {
            Button(
                onClick = {
                    val no = itemNoStr.toIntOrNull()
                    if (no == null) {
                        errorMessage = "Tik 'n geldige item nommer in."
                    } else if (startTime.isBlank() || venue.isBlank()) {
                        errorMessage = "Vul asseblief alle velde in."
                    } else {
                        AthleticsRepository.addProgramItem(
                            ProgramItem(
                                itemNo = no,
                                startTime = startTime.trim(),
                                ageGroup = selectedAge,
                                gender = selectedGender,
                                eventName = selectedEvent,
                                venue = venue.trim(),
                                status = "Wagtend"
                            )
                        )
                        onItemAdded()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyHeader),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("submit_program_item_btn")
            ) {
                Text("Voeg by Program", fontWeight = FontWeight.Bold)
            }
        }
    }
}
