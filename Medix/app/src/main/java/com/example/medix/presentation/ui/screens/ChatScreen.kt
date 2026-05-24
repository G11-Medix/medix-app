package com.example.medix.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.medix.presentation.ui.components.BottomNavigationBar
import com.example.medix.presentation.viewmodels.chat.ChatMessage
import com.example.medix.presentation.viewmodels.chat.ChatOptionUi
import com.example.medix.presentation.viewmodels.chat.ChatViewModel
// removed SharedNotificationViewModel import

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(bottom = 80.dp)
        ) {
            Text(
                text = "Chat de citas",
                style = MaterialTheme.typography.titleLarge,
            )

            Text(
                text = "Asistente Medix",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                // ensure enough bottom padding so options are not obscured by the input/keyboard
                contentPadding = PaddingValues(bottom = 200.dp),
            ) {
                items(state.messages, key = { it.id }) { message ->
                    ChatMessageItem(
                        message = message,
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .height(16.dp)
                                            .width(16.dp),
                                        strokeWidth = 2.dp,
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Procesando...",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            state.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { viewModel.retry() },
                    enabled = !state.isLoading,
                ) {
                    Text("Reintentar")
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedTextField(
                    value = state.input,
                    onValueChange = viewModel::onInputChanged,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Escribe tu mensaje") },
                    singleLine = true,
                    enabled = !state.isLoading,
                )

                Button(
                    onClick = { viewModel.sendInputText() },
                    enabled = !state.isLoading && state.input.isNotBlank(),
                ) {
                    Text("Enviar")
                }
            }
        }

        BottomNavigationBar(
            currentRoute = currentRoute,
            onNavigate = onNavigate,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
        )
    }
}

@Composable
private fun ChatMessageItem(
    message: ChatMessage,
    onOptionTap: (indexVisible: Int, optionId: String?, optionLabel: String, optionPayloadText: String?, enabled: Boolean) -> Unit,
) {
    val arrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    val bubbleColor = if (message.isUser) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val textColor = if (message.isUser) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    // Detect if this is a confirmation message from the assistant
    val isConfirmationMessage = !message.isUser &&
        (message.text.contains(Regex("""(?i)(desea confirmar|confirmar cita|confirmar)""")) ||
         message.text.contains(Regex("""(?i)voy a agendar""")))

    val confirmationOptions: List<ChatOptionUi> = if (isConfirmationMessage && message.options.isEmpty()) {
        listOf(
            ChatOptionUi(
                id = null,
                label = "Confirmar cita",
                enabled = true,
                payloadText = "confirmar",
                displayIndex = 1
            ),
            ChatOptionUi(
                id = null,
                label = "No, cambiar algo",
                enabled = true,
                payloadText = "no",
                displayIndex = 2
            )
        )
    } else {
        emptyList<ChatOptionUi>()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = arrangement,
        ) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = bubbleColor,
                tonalElevation = 1.dp,
                modifier = Modifier.widthIn(max = 320.dp),
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = if (message.isUser) "Tú" else "Medix",
                        style = MaterialTheme.typography.labelSmall,
                        color = textColor.copy(alpha = 0.75f),
                        fontWeight = FontWeight.SemiBold,
                    )

                    if (message.text.isNotBlank()) {
                        Text(
                            text = message.text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = textColor,
                        )
                    }

                    val optionsToShow = if (confirmationOptions.isNotEmpty()) confirmationOptions else message.options

                    if (!message.isUser && optionsToShow.isNotEmpty()) {
                        optionsToShow.forEachIndexed { index, option ->
                            val visibleIndex = option.displayIndex ?: (index + 1)
                            OutlinedButton(
                                onClick = {
                                    onOptionTap(visibleIndex, option.id, option.label, option.payloadText, option.enabled)
                                },
                                enabled = option.enabled,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 48.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                            ) {
                                Text(text = "${visibleIndex}. ${option.label}")
                            }
                        }
                    }
                }
            }
        }
    }
}






