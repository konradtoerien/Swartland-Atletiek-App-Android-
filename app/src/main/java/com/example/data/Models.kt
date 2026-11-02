package com.example.data

data class Athlete(
    val id: Long = System.currentTimeMillis(),
    val firstName: String,
    val lastName: String,
    val school: String,
    val gender: String, // "Seuns" or "Dogters"
    val ageGroup: String, // "6".."13"
    val events: List<String>
) {
    val fullName: String get() = "$firstName $lastName"
    val eventsString: String get() = events.joinToString(", ")
}

data class EventRecord(
    val id: Long = System.currentTimeMillis(),
    val eventName: String,
    val gender: String, // "Seuns" or "Dogters"
    val ageGroup: String, // "6".."13"
    val recordValue: String, // e.g. "11.85" or "10.42m"
    val holder: String, // School or Athlete name
    val year: Int
)

data class EventResult(
    val id: Long = System.currentTimeMillis(),
    val eventName: String,
    val gender: String,
    val ageGroup: String,
    val place: Int,
    val athleteId: Long,
    val athleteName: String,
    val school: String,
    val resultValue: String,
    val asaPoints: Int,
    val timestamp: Long = System.currentTimeMillis()
)

data class ProgramItem(
    val id: Long = System.currentTimeMillis(),
    val itemNo: Int,
    val startTime: String,
    val ageGroup: String,
    val gender: String,
    val eventName: String,
    val venue: String,
    val status: String = "Wagtend" // "Wagtend", "Besig", "Voltooi"
)

data class SchoolPoints(
    val school: String,
    val totalPoints: Int,
    val rank: Int = 0
)

data class AthleteStanding(
    val athleteName: String,
    val school: String,
    val gender: String,
    val ageGroup: String,
    val totalPoints: Int,
    val rank: Int = 0
)
