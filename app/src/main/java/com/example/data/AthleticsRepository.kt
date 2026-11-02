package com.example.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object AthleticsRepository {

    private val _athletes = MutableStateFlow<List<Athlete>>(emptyList())
    val athletes: StateFlow<List<Athlete>> = _athletes.asStateFlow()

    private val _records = MutableStateFlow<List<EventRecord>>(emptyList())
    val records: StateFlow<List<EventRecord>> = _records.asStateFlow()

    private val _results = MutableStateFlow<List<EventResult>>(emptyList())
    val results: StateFlow<List<EventResult>> = _results.asStateFlow()

    private val _program = MutableStateFlow<List<ProgramItem>>(emptyList())
    val program: StateFlow<List<ProgramItem>> = _program.asStateFlow()

    init {
        seedInitialData()
    }

    fun seedInitialData() {
        val initialAthletes = listOf(
            Athlete(1L, "Dirk", "van der Merwe", "Laerskool Swartland", "Seuns", "13", listOf("100m", "200m", "Long Jump")),
            Athlete(2L, "Anika", "Smit", "Laerskool Swartland", "Dogters", "13", listOf("100m", "75m Hurdles", "High Jump")),
            Athlete(3L, "Johan", "Botha", "Laerskool Vredenburg", "Seuns", "13", listOf("100m", "Shot Put", "Discus")),
            Athlete(4L, "Mieke", "Louw", "Laerskool Vredenburg", "Dogters", "13", listOf("100m", "200m", "Long Jump")),
            Athlete(5L, "Pieter", "de Villiers", "Laerskool Moorreesburg", "Seuns", "12", listOf("100m", "Shot Put", "Long Jump")),
            Athlete(6L, "Lize", "Bester", "Laerskool Swartland", "Dogters", "12", listOf("100m", "High Jump", "Shot Put")),
            Athlete(7L, "Willem", "Kotze", "Laerskool Piketberg", "Seuns", "12", listOf("100m", "150m", "Javelin")),
            Athlete(8L, "Chloe", "du Plessis", "Laerskool Piketberg", "Dogters", "12", listOf("100m", "75m Hurdles", "Discus")),
            Athlete(9L, "Liam", "Steenkamp", "Laerskool Swartland", "Seuns", "11", listOf("80m", "100m", "Long Jump")),
            Athlete(10L, "Elzaan", "Visser", "Laerskool Moorreesburg", "Dogters", "11", listOf("80m", "100m", "Shot Put")),
            Athlete(11L, "Ruan", "Marais", "Laerskool Swartland", "Seuns", "10", listOf("80m", "100m", "High Jump")),
            Athlete(12L, "Mia", "Theron", "Laerskool Vredenburg", "Dogters", "10", listOf("70m Hurdles", "80m", "Long Jump")),
            Athlete(13L, "Kobus", "Nel", "Laerskool Swartland", "Seuns", "9", listOf("60m", "80m", "Shot Put")),
            Athlete(14L, "Nadia", "Fourie", "Laerskool Piketberg", "Dogters", "9", listOf("60m", "80m", "Long Jump")),
            Athlete(15L, "Francois", "Burger", "Laerskool Swartland", "Seuns", "8", listOf("60m", "80m")),
            Athlete(16L, "Carla", "Brand", "Laerskool Moorreesburg", "Dogters", "8", listOf("60m", "80m")),
            Athlete(17L, "Hendrik", "Basson", "Laerskool Swartland", "Seuns", "7", listOf("60m", "80m")),
            Athlete(18L, "Emma", "Coetzee", "Laerskool Vredenburg", "Dogters", "7", listOf("60m", "80m")),
            Athlete(19L, "Braam", "Myburgh", "Laerskool Swartland", "Seuns", "6", listOf("50m")),
            Athlete(20L, "Zani", "Kruger", "Laerskool Swartland", "Dogters", "6", listOf("50m"))
        )

        val initialRecords = listOf(
            EventRecord(1L, "100m", "Seuns", "13", "11.85", "Laerskool Swartland", 2024),
            EventRecord(2L, "100m", "Dogters", "13", "12.30", "Laerskool Vredenburg", 2023),
            EventRecord(3L, "200m", "Seuns", "13", "24.60", "Laerskool Moorreesburg", 2022),
            EventRecord(4L, "Shot Put", "Dogters", "12", "10.42m", "Laerskool Vredenburg", 2023),
            EventRecord(5L, "High Jump", "Seuns", "13", "1.65m", "Laerskool Swartland", 2024),
            EventRecord(6L, "Long Jump", "Dogters", "13", "4.88m", "Laerskool Piketberg", 2021),
            EventRecord(7L, "80m", "Seuns", "11", "10.40", "Laerskool Swartland", 2023),
            EventRecord(8L, "70m Hurdles", "Dogters", "10", "12.10", "Laerskool Swartland", 2024)
        )

        val initialProgram = listOf(
            ProgramItem(1L, 1, "08:00", "13", "Seuns", "100m", "Hoofbaan", "Voltooi"),
            ProgramItem(2L, 2, "08:10", "13", "Dogters", "100m", "Hoofbaan", "Voltooi"),
            ProgramItem(3L, 3, "08:20", "12", "Dogters", "High Jump", "Hoogspring Mat A", "Besig"),
            ProgramItem(4L, 4, "08:35", "12", "Seuns", "Shot Put", "Gewigstoot C", "Besig"),
            ProgramItem(5L, 5, "08:50", "11", "Seuns", "80m", "Hoofbaan", "Wagtend"),
            ProgramItem(6L, 6, "09:05", "11", "Dogters", "80m", "Hoofbaan", "Wagtend"),
            ProgramItem(7L, 7, "09:20", "13", "Seuns", "Long Jump", "Verspring Put B", "Wagtend"),
            ProgramItem(8L, 8, "09:40", "10", "Dogters", "70m Hurdles", "Hoofbaan", "Wagtend"),
            ProgramItem(9L, 9, "10:00", "12", "Seuns", "150m", "Hoofbaan", "Wagtend"),
            ProgramItem(10L, 10, "10:30", "13", "Seuns", "4x100m Relay", "Hoofbaan", "Wagtend")
        )

        val initialResults = listOf(
            EventResult(101L, "100m", "Seuns", "13", 1, 1L, "Dirk van der Merwe", "Laerskool Swartland", "11.75", 940),
            EventResult(102L, "100m", "Seuns", "13", 2, 3L, "Johan Botha", "Laerskool Vredenburg", "12.10", 870),
            EventResult(103L, "100m", "Dogters", "13", 1, 2L, "Anika Smit", "Laerskool Swartland", "12.45", 895),
            EventResult(104L, "100m", "Dogters", "13", 2, 4L, "Mieke Louw", "Laerskool Vredenburg", "12.80", 820)
        )

        _athletes.value = initialAthletes
        _records.value = initialRecords
        _program.value = initialProgram
        _results.value = initialResults
    }

    // Athletes
    fun insertAthletes(newAthletes: List<Athlete>) {
        _athletes.update { current ->
            current + newAthletes
        }
    }

    fun addAthlete(athlete: Athlete) {
        _athletes.update { current ->
            current + athlete
        }
    }

    fun deleteAthlete(id: Long) {
        _athletes.update { current -> current.filterNot { it.id == id } }
    }

    // Records
    fun addRecord(record: EventRecord) {
        _records.update { current ->
            current.filterNot {
                it.eventName.equals(record.eventName, ignoreCase = true) &&
                        it.gender.equals(record.gender, ignoreCase = true) &&
                        it.ageGroup == record.ageGroup
            } + record
        }
    }

    fun deleteRecord(id: Long) {
        _records.update { current -> current.filterNot { it.id == id } }
    }

    fun fetchEventRecord(eventName: String, gender: String, ageGroup: String): EventRecord? {
        return _records.value.firstOrNull {
            it.eventName.equals(eventName, ignoreCase = true) &&
                    it.gender.equals(gender, ignoreCase = true) &&
                    it.ageGroup == ageGroup
        }
    }

    // Results & Officiating
    fun saveEventResults(
        eventName: String,
        gender: String,
        ageGroup: String,
        newResults: List<EventResult>
    ) {
        _results.update { current ->
            val filtered = current.filterNot {
                it.eventName.equals(eventName, ignoreCase = true) &&
                        it.gender.equals(gender, ignoreCase = true) &&
                        it.ageGroup == ageGroup
            }
            newResults + filtered
        }
    }

    fun deleteSingleAthleteResult(
        eventName: String,
        gender: String,
        ageGroup: String,
        athleteId: Long
    ) {
        _results.update { current ->
            current.filterNot {
                it.eventName.equals(eventName, ignoreCase = true) &&
                        it.gender.equals(gender, ignoreCase = true) &&
                        it.ageGroup == ageGroup &&
                        it.athleteId == athleteId
            }
        }
    }

    // Program
    fun addProgramItem(item: ProgramItem) {
        _program.update { current ->
            (current + item).sortedBy { it.itemNo }
        }
    }

    fun updateProgramItemStatus(id: Long, newStatus: String) {
        _program.update { current ->
            current.map {
                if (it.id == id) it.copy(status = newStatus) else it
            }
        }
    }

    fun deleteProgramItem(id: Long) {
        _program.update { current -> current.filterNot { it.id == id } }
    }

    // Reports calculations
    fun getSchoolPoints(): List<SchoolPoints> {
        val all = _results.value
        val map = mutableMapOf<String, Int>()
        for (res in all) {
            val cur = map.getOrDefault(res.school, 0)
            map[res.school] = cur + res.asaPoints
        }
        return map.entries
            .map { SchoolPoints(it.key, it.value) }
            .sortedByDescending { it.totalPoints }
            .mapIndexed { index, sp -> sp.copy(rank = index + 1) }
    }

    fun getTopAthletes(): List<AthleteStanding> {
        val all = _results.value
        val map = mutableMapOf<String, AthleteStanding>()
        for (res in all) {
            val key = "${res.athleteName}|${res.school}|${res.gender}|${res.ageGroup}"
            val existing = map[key]
            if (existing != null) {
                map[key] = existing.copy(totalPoints = existing.totalPoints + res.asaPoints)
            } else {
                map[key] = AthleteStanding(
                    athleteName = res.athleteName,
                    school = res.school,
                    gender = res.gender,
                    ageGroup = res.ageGroup,
                    totalPoints = res.asaPoints
                )
            }
        }
        return map.values
            .sortedByDescending { it.totalPoints }
            .mapIndexed { index, standing -> standing.copy(rank = index + 1) }
    }

    // Reset All Data
    fun resetDatabase() {
        _athletes.value = emptyList()
        _records.value = emptyList()
        _results.value = emptyList()
        _program.value = emptyList()
    }
}
