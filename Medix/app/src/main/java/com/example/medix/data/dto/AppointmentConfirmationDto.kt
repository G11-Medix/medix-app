package com.example.medix.data.dto

data class AppointmentConfirmationDto(
    val doctorName: String,
    val date: String,
    val clinicName: String,
    val address: String,
    val lat: Double,
    val lon: Double,
    val title: String,
    val message: String,
    val status: String // "SUCCESS", "PENDING", "CANCELLED"
)

/*

{
  "doctorName": "Dr. Juan Pérez",
  "date": "2026-04-10T10:00:00",
  "clinicName": "Medix Health Center",
  "address": "Bogotá, Colombia",
  "lat": 4.7110,
  "lon": -74.0721,
  "title": "¡Cita Confirmada!",
  "message": "Tu cita fue agendada exitosamente"
}
 */