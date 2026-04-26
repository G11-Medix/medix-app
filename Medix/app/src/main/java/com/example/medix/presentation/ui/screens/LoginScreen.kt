package com.example.medix.presentation.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.medix.R
import com.example.medix.presentation.viewmodels.auth.AuthViewModel
import com.example.medix.presentation.ui.components.login.LoginCard

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit, // 👈 ya no se usa
    onNavigateToRegister: () -> Unit, // 👈 ya no se usa
) {
    val state by viewModel.uiState.collectAsState()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val layoutSpec = rememberLoginLayoutSpec(isLandscape = isLandscape)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.iniciarapp_medix),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(layoutSpec.backgroundHeightFactor)
                .align(Alignment.TopCenter)
                .offset(y = layoutSpec.backgroundOffsetY),
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = layoutSpec.overlayAlpha))
        )

        if (isLandscape) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = layoutSpec.horizontalPadding, vertical = layoutSpec.verticalPadding),
                contentAlignment = Alignment.CenterEnd
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(if (layoutSpec.isExpanded) 0.5f else 0.58f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    if (layoutSpec.enableLandscapeCardScroll) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Spacer(modifier = Modifier.height(8.dp))
                            LoginCard(
                                state = state,
                                viewModel = viewModel,
                                maxWidth = layoutSpec.cardMaxWidth,
                                compactMode = layoutSpec.compactLandscapeMode,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    } else {
                        LoginCard(
                            state = state,
                            viewModel = viewModel,
                            maxWidth = layoutSpec.cardMaxWidth,
                            compactMode = layoutSpec.compactLandscapeMode,
                        )
                    }
                }
            }

        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = layoutSpec.horizontalPadding, vertical = layoutSpec.verticalPadding),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = layoutSpec.containerMaxWidth)
                        .padding(bottom = layoutSpec.cardBottomPadding),
                    contentAlignment = Alignment.Center
                ) {
                    LoginCard(
                        state = state,
                        viewModel = viewModel,
                        maxWidth = layoutSpec.cardMaxWidth,
                        compactMode = false,
                    )
                }
            }
        }
    }
}

private data class LoginLayoutSpec(
    val isExpanded: Boolean,
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val containerMaxWidth: Dp,
    val cardMaxWidth: Dp,
    val cardBottomPadding: Dp,
    val backgroundOffsetY: Dp,
    val backgroundHeightFactor: Float,
    val overlayAlpha: Float,
    val compactLandscapeMode: Boolean,
    val enableLandscapeCardScroll: Boolean,
)

@Composable
private fun rememberLoginLayoutSpec(
    isLandscape: Boolean,
): LoginLayoutSpec {
    val density = LocalDensity.current
    val containerSize = LocalWindowInfo.current.containerSize
    val widthDp = with(density) { containerSize.width.toDp().value.toInt() }
    val heightDp = with(density) { containerSize.height.toDp().value.toInt() }
    val isCompact = widthDp < 360
    val isExpanded = widthDp >= 840
    val isMedium = !isCompact && !isExpanded
    val isShort = heightDp < 700
    val isVeryShort = heightDp < 600

    val horizontalPadding = when {
        isExpanded -> 40.dp
        isMedium -> 22.dp
        else -> 14.dp
    }

    val verticalPadding = when {
        isLandscape -> 16.dp
        isShort -> 12.dp
        else -> 24.dp
    }

    val cardMaxWidth = when {
        isExpanded -> 500.dp
        isMedium -> 420.dp
        else -> 360.dp
    }

    val containerMaxWidth = when {
        isExpanded -> 700.dp
        isMedium -> 560.dp
        else -> 460.dp
    }

    val cardBottomPadding = when {
        isShort -> 6.dp
        isExpanded -> 20.dp
        else -> 14.dp
    }

    val backgroundOffsetY = if (isLandscape) (-28).dp else 0.dp

    val backgroundHeightFactor = if (isLandscape) 1.22f else 1f

    val overlayAlpha = when {
        isLandscape -> 0.16f
        isShort -> 0.2f
        else -> 0.14f
    }

    val compactLandscapeMode = isLandscape && isShort
    val enableLandscapeCardScroll = isLandscape && isVeryShort

    return LoginLayoutSpec(
        isExpanded = isExpanded,
        horizontalPadding = horizontalPadding,
        verticalPadding = verticalPadding,
        containerMaxWidth = containerMaxWidth,
        cardMaxWidth = cardMaxWidth,
        cardBottomPadding = cardBottomPadding,
        backgroundOffsetY = backgroundOffsetY,
        backgroundHeightFactor = backgroundHeightFactor,
        overlayAlpha = overlayAlpha,
        compactLandscapeMode = compactLandscapeMode,
        enableLandscapeCardScroll = enableLandscapeCardScroll,
    )
}
