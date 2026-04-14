package com.example.medix.presentation.ui.components.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.medix.data.dto.UserProfileDto
import com.example.medix.presentation.ui.components.SectionTitle

import androidx.compose.material.icons.filled.*
import androidx.compose.ui.unit.dp


@Composable
fun ProfileInfo(
    profile: UserProfileDto,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {

        SectionTitle("Información Personal")

        InfoCard(Icons.Default.Person, "Nombres", profile.nombres)
        InfoCard(Icons.Default.Badge, "Apellidos", profile.apellidos)
        InfoCard(Icons.Default.Fingerprint, "Documento", profile.documento)
        InfoCard(Icons.Default.LocalHospital, "EPS", profile.eps)

        Spacer(modifier = Modifier.height(12.dp))

        SectionTitle("Contacto y Seguridad")

        InfoCard(Icons.Default.Email, "Correo", profile.correo)
        InfoCard(Icons.Default.Phone, "Teléfono", profile.telefono)
    }
}