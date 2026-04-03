package com.example.medix.presentation.ui.components.confirmation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


data class Quad<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

@Composable
fun SuccessHeader(
    title: String,
    message: String,
    status: String
) {
    val (bgColor, iconColor, textColor, icon) = when (status) {

        "SUCCESS" -> Quad(
            Color(0xFFC8E6C9),
            Color(0xFF2E7D32),
            Color(0xFF1565C0),
            Icons.Default.Check
        )

        "PENDING" -> Quad(
            Color(0xFFFFF9C4),
            Color(0xFFF9A825),
            Color(0xFFF9A825),
            Icons.Default.Schedule
        )

        "CANCELLED" -> Quad(
            Color(0xFFFFCDD2),
            Color(0xFFC62828),
            Color(0xFFC62828),
            Icons.Default.Close
        )

        else -> Quad(
            Color.LightGray,
            Color.DarkGray,
            Color.DarkGray,
            Icons.Default.Info
        )
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Box(
            modifier = Modifier
                .size(100.dp)
                .background(bgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(50.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = title,
            color = textColor,
            fontWeight = FontWeight.Bold
        )

        Text(text = message)
    }
}

