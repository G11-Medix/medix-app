package com.example.medix.presentation.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

import com.example.medix.core.auth.SessionManager
import com.example.medix.presentation.ui.components.BottomNavigationBar
import com.example.medix.presentation.ui.components.SectionTitle
import com.example.medix.presentation.ui.components.profile.InfoCard
import com.example.medix.presentation.ui.components.profile.TopBarProfile
import com.example.medix.presentation.ui.state.UiState
import com.example.medix.presentation.viewmodels.profile.ProfileViewModel
import com.example.medix.data.dto.UserProfileDto
import com.example.medix.presentation.ui.components.profile.ProfileHeader
import com.example.medix.presentation.ui.components.profile.ProfileInfo
@Composable
fun ProfileScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {

    val viewModel: ProfileViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()


    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(bottom = 80.dp)
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            TopBarProfile(
                onLogout = onLogout
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (state) {

                is UiState.Loading -> {
                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Error -> {
                    val error = state as UiState.Error

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = error.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { viewModel.loadProfile() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Reintentar")
                        }
                    }
                }

                is UiState.Success<*> -> {

                    val profile =
                        (state as UiState.Success<*>).data as? UserProfileDto
                            ?: return@Column

                    if (isLandscape) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            ProfileHeader(profile, Modifier.weight(1f))

                            ProfileInfo(profile, Modifier.weight(1f))
                        }

                    } else {

                        Column {
                            ProfileHeader(profile)
                            Spacer(modifier = Modifier.height(16.dp))
                            ProfileInfo(profile)
                        }
                    }
                }
            }
        }


        BottomNavigationBar(
            currentRoute = currentRoute,
            onNavigate = onNavigate,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}