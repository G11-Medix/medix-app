package com.example.medix.core.utils


import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*


object DateUtils {
    fun formatAppointmentDate(isoDate: String): String {
        return try {
            val dateTime = LocalDateTime.parse(isoDate)

            val now = LocalDate.now()
            val appointmentDate = dateTime.toLocalDate()

            val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())
            val time = dateTime.format(timeFormatter)

            when (appointmentDate) {
                now -> "Hoy, $time"
                now.plusDays(1) -> "Mañana, $time"
                else -> {
                    val formatter = DateTimeFormatter.ofPattern("EEEE, h:mm a", Locale("es"))
                    dateTime.format(formatter)
                }
            }

        } catch (e: Exception) {
            isoDate
        }
    }
}