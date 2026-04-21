package com.example.medix.core.utils


import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*


object DateUtils {
    fun formatAppointmentDateConfirmation(isoDate: String): String {
        return try {
            val dateTime = LocalDateTime.parse(isoDate)

            val now = LocalDate.now()
            val appointmentDate = dateTime.toLocalDate()

            val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale("es"))
            val formattedTime = dateTime.format(timeFormatter)

            when {
                appointmentDate == now -> "Hoy, $formattedTime"
                appointmentDate == now.plusDays(1) -> "Mañana, $formattedTime"

                appointmentDate.month == now.month && appointmentDate.year == now.year -> {
                    val formatter = DateTimeFormatter.ofPattern("EEEE, h:mm a", Locale("es"))
                    dateTime.format(formatter)
                }

                appointmentDate.year == now.year -> {
                    val formatter = DateTimeFormatter.ofPattern("d 'de' MMMM, h:mm a", Locale("es"))
                    dateTime.format(formatter)
                }

                else -> {
                    val formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy, h:mm a", Locale("es"))
                    dateTime.format(formatter)
                }
            }

        } catch (e: Exception) {
            isoDate
        }
    }

    fun formatAppointmentDate(fecha: String, hora: String): String {
        return try {
            val date = LocalDate.parse(fecha)
            val time = LocalTime.parse(hora)
            val dateTime = LocalDateTime.of(date, time)

            val now = LocalDate.now()
            val appointmentDate = dateTime.toLocalDate()

            val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale("es"))
            val formattedTime = dateTime.format(timeFormatter)

            when {
                appointmentDate == now ->
                    "Hoy, $formattedTime"

                appointmentDate == now.plusDays(1) ->
                    "Mañana, $formattedTime"

                appointmentDate.year == now.year && appointmentDate.month == now.month -> {
                    // mismo mes
                    val formatter = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM, h:mm a", Locale("es"))
                    dateTime.format(formatter)
                }

                appointmentDate.year == now.year -> {
                    // mismo año, otro mes
                    val formatter = DateTimeFormatter.ofPattern("d 'de' MMMM, h:mm a", Locale("es"))
                    dateTime.format(formatter)
                }

                else -> {
                    // otro año
                    val formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy, h:mm a", Locale("es"))
                    dateTime.format(formatter)
                }
            }

        } catch (e: Exception) {
            "$fecha $hora"
        }
    }
}