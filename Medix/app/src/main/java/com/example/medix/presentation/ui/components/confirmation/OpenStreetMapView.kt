package com.example.medix.presentation.ui.components.confirmation

import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

@Composable
fun OpenStreetMapView(
    lat: Double,
    lon: Double
) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->

            MapView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                setMultiTouchControls(true)

                val mapController = controller
                val startPoint = GeoPoint(lat, lon)

                mapController.setZoom(15.0)
                mapController.setCenter(startPoint)


                val marker = Marker(this)
                marker.position = startPoint
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.title = "Medix Location"

                overlays.add(marker)
            }
        }
    )
}