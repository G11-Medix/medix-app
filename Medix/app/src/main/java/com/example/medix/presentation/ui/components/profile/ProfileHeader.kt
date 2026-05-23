package com.example.medix.presentation.ui.components.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.medix.data.dto.UserProfileDto

@Composable
fun ProfileHeader(
    profile: UserProfileDto,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "${profile.nombres} ${profile.apellidos}",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Paciente Medix",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}