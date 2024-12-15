package com.afonsoingles.iwant2025.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

object TimeUtils {
    fun getTimeComponents(zoneId: ZoneId): List<Long> {
        val now = LocalDateTime.now(zoneId)
        val year2025 = LocalDateTime.of(2025, 1, 1, 0, 0)
        
        val days = ChronoUnit.DAYS.between(now, year2025)
        val hours = ChronoUnit.HOURS.between(now, year2025) % 24
        val minutes = ChronoUnit.MINUTES.between(now, year2025) % 60
        val seconds = ChronoUnit.SECONDS.between(now, year2025) % 60
        
        return listOf(days, hours, minutes, seconds)
    }

    fun getTimeUntil2025(zoneId: ZoneId): String {
        val (days, hours, minutes, seconds) = getTimeComponents(zoneId)
        return "$days days, $hours hours, $minutes minutes, $seconds seconds"
    }
} 