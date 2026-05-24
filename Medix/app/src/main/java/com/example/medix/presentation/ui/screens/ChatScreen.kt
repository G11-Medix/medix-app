package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.medix.presentation.ui.components.BottomNavigationBar
import com.example.medix.presentation.viewmodels.chat.ChatMessage
import com.example.medix.presentation.viewmodels.chat.ChatOptionUi
import com.example.medix.presentation.viewmodels.chat.ChatViewModel

@Composable
fun ChatScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.lastIndex)
        }
    }

    Scaffold(
        topBar = {
            ChatHeader()
        },
        bottomBar = {
            Column(modifier = Modifier.navigationBarsPadding()) {
                ChatInputArea(
                    input = state.input,
                    onInputChange = viewModel::onInputChanged,
                    onSend = viewModel::sendInputText,
                    isLoading = state.isLoading,
                    isConsentAccepted = state.isConsentAccepted
                )
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = onNavigate
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            val lastAssistantMessageIndex = state.messages.indexOfLast { !it.isUser }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(state.messages, key = { _, msg -> msg.id }) { index, message ->
                    ChatMessageItem(
                        message = message,
                        isLastAssistantMessage = index == lastAssistantMessageIndex,
                        onOptionTap = { indexVisible, optionId, optionLabel, optionPayloadText, enabled ->
                            viewModel.sendOption(
                                indexVisible = indexVisible,
                                optionId = optionId,
                                optionLabel = optionLabel,
                                optionPayloadText = optionPayloadText,
                                enabled = enabled,
                            )
                        },
                    )
                }

                if (state.isLoading) {
                    item {
                        AssistantTypingIndicator()
                    }
                }

                state.errorMessage?.let { error ->
                    item {
                        ChatErrorMessage(
                            message = error,
                            onRetry = { viewModel.retry() },
                            isLoading = state.isLoading
                        )
                    }
                }
                
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun ChatHeader() {
    Surface(
        tonalElevation = 4.dp,
        shadowElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Chat de citas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Asistente Medix",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ChatInputArea(
    input: String,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
    isLoading: Boolean,
    isConsentAccepted: Boolean
) {
    Surface(
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.imePadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = onInputChange,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(max = 120.dp),
                placeholder = { Text("Escribe tu solicitud...") },
                shape = RoundedCornerShape(24.dp),
                enabled = !isLoading && isConsentAccepted,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                ),
                maxLines = 4
            )

            FloatingActionButton(
                onClick = onSend,
                containerColor = if (isLoading || input.isBlank() || !isConsentAccepted) 
                    MaterialTheme.colorScheme.surfaceVariant 
                else 
                    MaterialTheme.colorScheme.primary,
                contentColor = if (isLoading || input.isBlank() || !isConsentAccepted)
                    MaterialTheme.colorScheme.onSurfaceVariant
                else
                    MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Enviar",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ChatMessageItem(
    message: ChatMessage,
    isLastAssistantMessage: Boolean,
    onOptionTap: (indexVisible: Int, optionId: String?, optionLabel: String, optionPayloadText: String?, enabled: Boolean) -> Unit,
) {
    val isUser = message.isUser
    
    val shape = if (isUser) {
        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 20.dp, bottomEnd = 4.dp)
    } else {
        RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 4.dp, bottomEnd = 20.dp)
    }

    val bubbleColor = if (isUser) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    }
    
    val textColor = if (isUser) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val isConfirmationMessage = !isUser && 
        (message.text.contains(Regex("""(?i)(desea confirmar|confirmar cita|confirmar)""")) ||
         message.text.contains(Regex("""(?i)voy a agendar""")))

    val isCancelFlow = !isUser && message.text.contains(Regex("""(?i)(cancelar|cancelación)"""))

    val optionsToShow = if (isConfirmationMessage && message.options.isEmpty()) {
        if (isCancelFlow) {
            listOf(
                ChatOptionUi(label = "Confirmar", enabled = true, payloadText = "confirmar"),
                ChatOptionUi(label = "No", enabled = true, payloadText = "no")
            )
        } else {
            listOf(
                ChatOptionUi(label = "Confirmar cita", enabled = true, payloadText = "confirmar"),
                ChatOptionUi(label = "No, cambiar algo", enabled = true, payloadText = "no")
            )
        }
    } else {
        message.options
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        if (!isUser) {
            Text(
                text = "Medix",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 8.dp, bottom = 4.dp),
                fontWeight = FontWeight.Bold
            )
        }

        Surface(
            color = bubbleColor,
            shape = shape,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (message.text.isNotBlank()) {
                    Text(
                        text = message.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor,
                        lineHeight = 20.sp
                    )
                }

                if (!isUser && optionsToShow.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    optionsToShow.forEachIndexed { index, option ->
                        val visibleIndex = option.displayIndex ?: (index + 1)
                        val canClick = option.enabled && isLastAssistantMessage
                        
                        OutlinedButton(
                            onClick = {
                                onOptionTap(visibleIndex, option.id, option.label, option.payloadText, canClick)
                            },
                            enabled = canClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .heightIn(min = 44.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(
                                width = 1.dp, 
                                color = if (canClick) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary,
                                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            )
                        ) {
                            Text(
                                text = option.label,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AssistantTypingIndicator() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 4.dp, bottomEnd = 20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Asistente pensando...",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ChatErrorMessage(
    message: String,
    onRetry: () -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(
            onClick = onRetry,
            enabled = !isLoading
        ) {
            Text("Reintentar")
        }
    }
}
