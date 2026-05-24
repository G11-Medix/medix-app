package com.example.medix.presentation.ui.components.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.medix.data.dto.UserProfileDto

@Composable
fun ProfileInfo(
    profile: UserProfileDto,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        ProfileSectionTitle(title = "Información Personal", icon = Icons.Default.PersonOutline)
        
        InfoCard(Icons.Default.Badge, "Nombres", profile.nombres)
        InfoCard(Icons.Default.Badge, "Apellidos", profile.apellidos)
        InfoCard(Icons.Default.Fingerprint, "Documento", profile.documento)
        InfoCard(Icons.Default.LocalHospital, "EPS", profile.eps)

        Spacer(modifier = Modifier.height(24.dp))

        ProfileSectionTitle(title = "Contacto y Seguridad", icon = Icons.Default.LockOpen)

        InfoCard(Icons.Default.Email, "Correo", profile.correo)
        InfoCard(Icons.Default.Phone, "Teléfono", profile.telefono)
    }
}

@Composable
fun ProfileSectionTitle(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
