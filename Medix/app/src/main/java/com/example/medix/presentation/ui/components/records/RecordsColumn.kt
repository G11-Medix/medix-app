package com.example.medix.presentation.ui.components.records

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { it ->
                    if (isPast) {
                        PastAppointmentCard(
                            name = it.name,
                            specialty = it.specialty,
                            date = DateUtils.formatAppointmentDate(it.date, it.hour),
                            state = it.state,
                            logo_url = it.logo_url
                        )
                    } else {
                        AppointmentCard(
                            name = it.name,
                            specialty = it.specialty,
                            date = DateUtils.formatAppointmentDate(it.date, it.hour),
                            state = it.state,
                            logo_url = it.logo_url
                        )
                    }
                }
            }
        }
    }
}