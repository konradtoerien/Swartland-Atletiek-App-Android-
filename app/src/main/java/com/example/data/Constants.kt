package com.example.data

object Constants {
    val AGE_GROUPS = listOf("6", "7", "8", "9", "10", "11", "12", "13")
    val GENDERS = listOf("Seuns", "Dogters")

    val ALL_DISCIPLINES = listOf(
        "50m", "60m", "80m", "100m", "150m", "200m",
        "1200m", "1500m", "1500m Walk", "4x100m Relay",
        "70m Hurdles", "75m Hurdles", "80m Hurdles", "150m Hurdles", "200m Hurdles",
        "Long Jump", "High Jump", "Shot Put", "Discus", "Javelin"
    )

    fun normalizeAgeGroup(raw: String): String {
        val s = raw.trim().lowercase().replace("u/", "").replace("u", "").replace("o/", "").replace("o", "")
        return if (s in AGE_GROUPS) s else "13"
    }

    fun getEventsForAgeAndGender(ageGroup: String, gender: String): List<String> {
        val age = normalizeAgeGroup(ageGroup)
        val isGirls = gender.trim().lowercase().startsWith("d") ||
                gender.trim().lowercase().startsWith("g") ||
                gender.trim().lowercase().startsWith("f")

        val events = mutableListOf<String>()
        when (age) {
            "6" -> events.addAll(listOf("50m"))
            "7", "8" -> events.addAll(listOf("60m", "80m"))
            "9" -> events.addAll(listOf("60m", "80m", "100m", "1200m", "Long Jump", "High Jump", "Shot Put"))
            "10", "11" -> events.addAll(listOf("60m", "80m", "100m", "1200m", "70m Hurdles", "Long Jump", "High Jump", "Shot Put"))
            "12" -> events.addAll(listOf(
                "60m", "80m", "100m", "150m", "1200m",
                "75m Hurdles", "150m Hurdles",
                "Long Jump", "High Jump", "Shot Put", "Discus", "Javelin", "1500m Walk"
            ))
            "13" -> {
                val hurdle = if (isGirls) "75m Hurdles" else "80m Hurdles"
                events.addAll(listOf(
                    "80m", "100m", "200m", "1500m",
                    hurdle, "200m Hurdles",
                    "Long Jump", "High Jump", "Shot Put", "Discus", "Javelin", "1500m Walk"
                ))
            }
            else -> events.addAll(listOf("100m", "Long Jump", "High Jump"))
        }

        events.add("4x100m Relay")
        return events
    }

    fun isTrackEvent(eventName: String): Boolean {
        val hasTrackKeywords = listOf("m", "Hurdles", "Walk", "Relay").any { it in eventName }
        val hasFieldKeywords = listOf("Jump", "Put", "Discus", "Javelin").any { it in eventName }
        return hasTrackKeywords && !hasFieldKeywords
    }

    fun parseResultToNumeric(rawVal: String, eventName: String): Double {
        if (rawVal.isBlank()) return 0.0
        val cleanStr = rawVal.trim().replace(" ", "").replace("m", "").replace("s", "")
        val parts = cleanStr.split(",", ".")
        return try {
            when (parts.size) {
                1 -> parts[0].toDoubleOrNull() ?: 0.0
                2 -> "${parts[0]}.${parts[1]}".toDoubleOrNull() ?: 0.0
                else -> {
                    val mins = parts[0].toDoubleOrNull() ?: 0.0
                    val secs = parts[1].toDoubleOrNull() ?: 0.0
                    val msRaw = parts[2]
                    val ms = (msRaw.toDoubleOrNull() ?: 0.0) / (if (msRaw.length <= 2) 100.0 else 1000.0)
                    (mins * 60.0) + secs + ms
                }
            }
        } catch (_: Exception) {
            0.0
        }
    }

    fun calculateAsaPoints(eventName: String, parsedVal: Double): Int {
        if (parsedVal <= 0.0) return 0
        val isTrack = isTrackEvent(eventName)
        val pts = if (isTrack) {
            (1000 - ((parsedVal - 10.0) * 35)).toInt()
        } else {
            (parsedVal * 180).toInt()
        }
        return pts.coerceIn(10, 1200)
    }
}
