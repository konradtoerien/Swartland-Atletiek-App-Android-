package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Athlete
import com.example.data.AthleticsRepository
import com.example.data.Constants
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Atlete Lys", "Nuwe Atleet", "Voorbeelddata", "⚙️ Reset")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "📋 Registrasie & Atlete",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = GoldAccent
        )
        Text(
            text = "Bestuur geregistreerde atlete vir Laerskool Swartland en gas-skole",
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
            0 -> AthleteListTab()
            1 -> NewAthleteTab(onAthleteAdded = { selectedTab = 0 })
            2 -> SampleDataTab(onDataLoaded = { selectedTab = 0 })
            3 -> ResetDataTab()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AthleteListTab() {
    val athletes by AthleticsRepository.athletes.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedSchoolFilter by remember { mutableStateOf("Alle Skole") }
    var selectedGenderFilter by remember { mutableStateOf("Alle Geslagte") }
    var selectedAgeFilter by remember { mutableStateOf("Alle Ouderdomme") }

    val schools = remember(athletes) {
        listOf("Alle Skole") + athletes.map { it.school }.distinct().sorted()
    }

    val filteredAthletes = athletes.filter { a ->
        val matchesSearch = searchQuery.isBlank() ||
                a.fullName.contains(searchQuery, ignoreCase = true) ||
                a.school.contains(searchQuery, ignoreCase = true)
        val matchesSchool = selectedSchoolFilter == "Alle Skole" || a.school == selectedSchoolFilter
        val matchesGender = selectedGenderFilter == "Alle Geslagte" || a.gender == selectedGenderFilter
        val matchesAge = selectedAgeFilter == "Alle Ouderdomme" || a.ageGroup == selectedAgeFilter
        matchesSearch && matchesSchool && matchesGender && matchesAge
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Soek volgens atleet of skool...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Soek", tint = GoldAccent) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Maak skoon", tint = TextMuted)
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GoldAccent,
                unfocusedBorderColor = NavyBorder,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                focusedLabelColor = GoldAccent,
                unfocusedLabelColor = TextMuted
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("athlete_search_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Filter row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Gender filter
            var expandedGender by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedGender,
                onExpandedChange = { expandedGender = !expandedGender },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = selectedGenderFilter,
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
                            onClick = { selectedGenderFilter = g; expandedGender = false }
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
                            onClick = { selectedAgeFilter = age; expandedAge = false }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Totaal Gevind: ${filteredAthletes.size} atlete",
            color = GoldAccent,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        if (filteredAthletes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("Geen atlete voldoen aan hierdie filters nie.", color = TextMuted)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredAthletes, key = { it.id }) { athlete ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = NavySurface),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("athlete_card_${athlete.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = athlete.fullName,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhite
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        color = if (athlete.gender == "Seuns") Color(0xFF1E3A8A) else Color(0xFF831843),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "O/${athlete.ageGroup} ${athlete.gender}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = TextWhite,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "🏫 ${athlete.school}",
                                    fontSize = 13.sp,
                                    color = GoldAccent,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                                Text(
                                    text = "Items: ${athlete.eventsString.ifBlank { "Geen items gekies" }}",
                                    fontSize = 12.sp,
                                    color = TextLight
                                )
                            }
                            IconButton(
                                onClick = { AthleticsRepository.deleteAthlete(athlete.id) },
                                modifier = Modifier.testTag("delete_athlete_${athlete.id}")
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Skrap atleet",
                                    tint = AccentRed
                                )
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
private fun NewAthleteTab(onAthleteAdded: () -> Unit) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var school by remember { mutableStateOf("Laerskool Swartland") }
    var selectedGender by remember { mutableStateOf("Seuns") }
    var selectedAge by remember { mutableStateOf("13") }
    val availableEvents = remember(selectedAge, selectedGender) {
        Constants.getEventsForAgeAndGender(selectedAge, selectedGender)
    }
    val selectedEvents = remember { mutableStateListOf<String>() }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedAge, selectedGender) {
        selectedEvents.clear()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "✍️ Enkel Atleet Inskrywing",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent
            )
        }

        item {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Voornaam") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextWhite, unfocusedTextColor = TextWhite,
                    focusedBorderColor = GoldAccent, unfocusedBorderColor = NavyBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("new_athlete_firstname")
            )
        }

        item {
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Van") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextWhite, unfocusedTextColor = TextWhite,
                    focusedBorderColor = GoldAccent, unfocusedBorderColor = NavyBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("new_athlete_lastname")
            )
        }

        item {
            OutlinedTextField(
                value = school,
                onValueChange = { school = it },
                label = { Text("Skool") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextWhite, unfocusedTextColor = TextWhite,
                    focusedBorderColor = GoldAccent, unfocusedBorderColor = NavyBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("new_athlete_school")
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Gender Dropdown
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

                // Age Dropdown
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
            Text(
                text = "Kies Items vir O/$selectedAge ($selectedGender):",
                fontWeight = FontWeight.SemiBold,
                color = GoldAccent,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(availableEvents) { event ->
            val isChecked = selectedEvents.contains(event)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { checked ->
                        if (checked) selectedEvents.add(event) else selectedEvents.remove(event)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = GoldAccent,
                        checkmarkColor = NavyHeader,
                        uncheckedColor = TextMuted
                    )
                )
                Text(
                    text = event,
                    color = if (isChecked) GoldAccent else TextWhite,
                    fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        }

        if (errorMessage != null) {
            item {
                Text(
                    text = errorMessage ?: "",
                    color = AccentRed,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        item {
            Button(
                onClick = {
                    if (firstName.isBlank() || lastName.isBlank()) {
                        errorMessage = "Vul asseblief beide voornaam en van in."
                    } else if (selectedEvents.isEmpty()) {
                        errorMessage = "Kies ten minste een item vir die atleet."
                    } else {
                        AthleticsRepository.addAthlete(
                            Athlete(
                                firstName = firstName.trim().replaceFirstChar { it.uppercase() },
                                lastName = lastName.trim().replaceFirstChar { it.uppercase() },
                                school = school.trim().ifBlank { "Laerskool Swartland" },
                                gender = selectedGender,
                                ageGroup = selectedAge,
                                events = selectedEvents.toList()
                            )
                        )
                        onAthleteAdded()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldAccent,
                    contentColor = NavyHeader
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_new_athlete_btn")
            ) {
                Text("🚀 Voeg Atleet By", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun SampleDataTab(onDataLoaded: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = NavySurface),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📦 Vinnige Kampioenskap Voorbeelddata",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Laai 'n volledige stel atlete in van Laerskool Swartland, Laerskool Vredenburg, Laerskool Moorreesburg en Laerskool Piketberg oor alle ouderdomsgroepe (O/6 tot O/13).",
                    fontSize = 13.sp,
                    color = TextLight
                )
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = {
                        AthleticsRepository.seedInitialData()
                        onDataLoaded()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = NavyHeader),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Herlaai Amptelike Voorbeelddata", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ResetDataTab() {
    var showConfirmDialog by remember { mutableStateOf(false) }
    var successMsg by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = NavySurface),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "⚠️ Vee Alle Byeenkoms Data Skoon",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentRed
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Hierdie aksie sal alle atlete, rekords, programitems en uitslae uit die stelsel verwyder.",
                    fontSize = 13.sp,
                    color = TextLight
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { showConfirmDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed, contentColor = TextWhite),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.DeleteForever, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Vee Alles Skoon (Reset)", fontWeight = FontWeight.Bold)
                }
            }
        }

        if (successMsg != null) {
            Surface(
                color = AccentGreen.copy(alpha = 0.2f),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = successMsg ?: "",
                    color = AccentGreen,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            containerColor = NavySurface,
            title = { Text("Bevestig Reset", color = GoldAccent, fontWeight = FontWeight.Bold) },
            text = { Text("Is jy seker jy wil alle data verwyder?", color = TextLight) },
            confirmButton = {
                Button(
                    onClick = {
                        AthleticsRepository.resetDatabase()
                        showConfirmDialog = false
                        successMsg = "Die stelsel is suksesvol skoongemaak."
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed, contentColor = TextWhite)
                ) {
                    Text("Ja, Vee Uit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Kanselleer", color = TextMuted)
                }
            }
        )
    }
}
