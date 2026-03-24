package com.example.medix.presentation.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medix.presentation.ui.components.BottomNavigationBar
import com.example.medix.presentation.ui.components.InfoCard
import com.example.medix.presentation.ui.components.InfoCardWithButton
import com.example.medix.presentation.ui.components.InfoCardWithStatus
import com.example.medix.presentation.ui.components.SectionTitle
import com.example.medix.presentation.ui.components.TopBarProfile

@Composable
fun ProfileScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(16.dp)
    ) {

        TopBarProfile()

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Ana María García",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Text(
                text = "Paciente Medix",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle("Información Personal")

        InfoCard(icon = Icons.Default.Person, title = "Nombres", value = "Ana María")
        InfoCard(icon = Icons.Default.Badge, title = "Apellidos", value = "García Rodríguez")
        InfoCard(icon = Icons.Default.Fingerprint, title = "Documento", value = "CC 52.483.921")
        InfoCard(icon = Icons.Default.LocalHospital, title = "EPS", value = "Sura Medicina Prepagada")

        Spacer(modifier = Modifier.height(12.dp))

        SectionTitle("Contacto y Seguridad")

        InfoCardWithStatus(
            icon = Icons.Default.Email,
            title = "Correo",
            value = "ana.garcia@email.com",
            status = "Verified"
        )

        InfoCardWithButton(
            icon = Icons.Default.Phone,
            title = "Telefono",
            value = "310 456 7890",
            buttonText = "Verify"
        )

        Spacer(modifier = Modifier.weight(1f))

        BottomNavigationBar(
            currentRoute = currentRoute,
            onNavigate = onNavigate
        )
    }
}