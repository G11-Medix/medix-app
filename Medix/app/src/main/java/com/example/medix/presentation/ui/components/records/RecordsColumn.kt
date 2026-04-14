package com.example.medix.presentation.ui.components.records
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
import com.example.medix.core.utils.DateUtils
import com.example.medix.presentation.ui.components.SectionTitle

@Composable
fun RecordsColumn(
    title: String,
    emptyText: String,
    items: List<com.example.medix.domain.entities.Appointment>,
    isPast: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {

        SectionTitle(title)

        if (items.isEmpty()) {
            Text(
                text = emptyText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            items.take(5).forEach {
                if (isPast) {
                    PastAppointmentCard(
                        name = it.name,
                        specialty = it.specialty,
                        date = DateUtils.formatAppointmentDate(it.date)
                    )
                } else {
                    AppointmentCard(
                        name = it.name,
                        specialty = it.specialty,
                        date = DateUtils.formatAppointmentDate(it.date)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}