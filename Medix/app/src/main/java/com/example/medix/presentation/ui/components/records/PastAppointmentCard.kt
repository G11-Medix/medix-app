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


@Composable
fun PastAppointmentCard(
    name: String,
    specialty: String,
    date: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE0E0E0)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Gray, CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {

                Text(
                    name,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )

                Text(
                    specialty,
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.History,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        date,
                        color = Color.DarkGray,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}