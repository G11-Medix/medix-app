package com.example.medix.presentation.ui.components.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.example.medix.domain.entities.Notification
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NotificationCard(notification: Notification) {
    val structuredData = remember(notification.body) { parseNotificationBody(notification.body) }
    
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .semantics {
                contentDescription = "Notificación: ${notification.title}"
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (notification.title.contains("cita", ignoreCase = true)) 
                            Icons.Default.CalendarMonth else Icons.Default.Notifications,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = notification.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
                            .format(Date(notification.timestamp)),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (structuredData.isEmpty()) {
                Text(
                    text = notification.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            } else {
                structuredData.forEach { (label, value) ->
                    if (label.equals("recomendaciones", ignoreCase = true)) {
                        RecommendationSection(value)
                    } else {
                        NotificationRow(label, value)
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1.5f),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
    }
}

@Composable
private fun RecommendationSection(text: String) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Recomendaciones",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

private fun parseNotificationBody(body: String): Map<String, String> {
    val sections = mutableMapOf<String, String>()
    
    // Check if it's formatted like "Key: Value\nKey: Value"
    val lines = body.lines()
    var currentKey = ""
    var currentValue = ""

    for (line in lines) {
        if (line.contains(":")) {
            if (currentKey.isNotEmpty()) {
                sections[currentKey] = currentValue.trim()
            }
            val parts = line.split(":", limit = 2)
            currentKey = parts[0].trim().lowercase()
            currentValue = parts[1].trim()
        } else if (currentKey.isNotEmpty()) {
            currentValue += " " + line.trim()
        }
    }
    
    if (currentKey.isNotEmpty()) {
        sections[currentKey] = currentValue.trim()
    }
    
    // Mapping specific keys if they don't match exactly but are similar
    val mappedSections = mutableMapOf<String, String>()
    sections.forEach { (k, v) ->
        val newKey = when {
            k.contains("fecha") || k.contains("dia") -> "fecha"
            k.contains("hora") -> "hora"
            k.contains("institucion") || k.contains("clinica") || k.contains("lugar") -> "institución"
            k.contains("especialidad") -> "especialidad"
            k.contains("recomendacion") -> "recomendaciones"
            else -> k
        }
        mappedSections[newKey] = v
    }

    return mappedSections
}
