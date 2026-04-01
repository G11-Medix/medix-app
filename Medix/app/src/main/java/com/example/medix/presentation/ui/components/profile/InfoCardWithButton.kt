package com.example.medix.presentation.ui.components.profile
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape


import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun InfoCardWithButton(
    icon: ImageVector,
    title: String,
    value: String,
    buttonText: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F3F6)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(icon, contentDescription = null)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 10.sp, color = Color.Gray)
                Text(value, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {},
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(buttonText)
            }
        }
    }
}