package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*

import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.medix.presentation.ui.components.AppointmentInfoCard
import com.example.medix.presentation.ui.components.OpenStreetMapView
import com.example.medix.presentation.ui.components.SuccessHeader
import com.example.medix.presentation.ui.components.TopBarConfirmation

@Composable
fun ConfirmationScreen(
    onDone: () -> Unit
) {

    val lat = 4.7110
    val lon = -74.0721

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TopBarConfirmation()

        Spacer(modifier = Modifier.height(16.dp))

        SuccessHeader()

        Spacer(modifier = Modifier.height(16.dp))

        AppointmentInfoCard()

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            OpenStreetMapView(lat, lon)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Un email de conformación se envio con los detalles",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Done")
        }
    }
}