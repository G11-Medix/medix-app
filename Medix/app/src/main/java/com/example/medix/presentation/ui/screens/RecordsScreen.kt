package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController
import com.example.medix.presentation.ui.components.AppointmentCard
import com.example.medix.presentation.ui.components.BottomNavigationBar
import com.example.medix.presentation.ui.components.HeaderSection
import com.example.medix.presentation.ui.components.PastAppointmentCard
import com.example.medix.presentation.ui.components.SectionTitle

@Composable
fun RecordsScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onNotificationsClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(16.dp)
    ) {

        HeaderSection(
            onNotificationsClick = onNotificationsClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Mis registros",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle("Próximas citas")

        AppointmentCard(
            name = "Dr. Sarah Jenkins",
            specialty = "Cardiologist • Video Call",
            date = "Tomorrow, 10:30 AM"
        )

        Spacer(modifier = Modifier.height(8.dp))

        AppointmentCard(
            name = "Dr. Michael Chen",
            specialty = "General Practitioner • Clinic",
            date = "Friday, 2:15 PM"
        )

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle("Citas Pasadas")

        PastAppointmentCard(
            name = "Dr. John Doe",
            specialty = "Dermatologist",
            date = "Sept 10, 9:00 AM"
        )

        Spacer(modifier = Modifier.height(8.dp))

        PastAppointmentCard(
            name = "Dr. Emily Clark",
            specialty = "Pediatrician",
            date = "Aug 21, 11:00 AM"
        )

        Spacer(modifier = Modifier.weight(1f))

        BottomNavigationBar(
            currentRoute = currentRoute,
            onNavigate = onNavigate
        )
    }
}