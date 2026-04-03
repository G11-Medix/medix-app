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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun AppointmentCard(
    name: String,
    specialty: String,
    date: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.LightGray, CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(name, fontWeight = FontWeight.Bold)

                Text(
                    specialty,
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color(0xFF1E88E5),
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        date,
                        color = Color(0xFF1E88E5),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}