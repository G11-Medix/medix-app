package com.example.medix.presentation.ui.components.voice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TranscriptCard(title: String, text: String, modifier: Modifier = Modifier) {
    val content = remember(text) { parseAssistantVoiceResponse(text) }

    Card(
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF2FB)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = title,
                fontSize = 14.sp,
                color = Color(0xFF1565C0)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (content) {
                    is AssistantVoiceContent.PlainText -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = content.text,
                                fontSize = 16.sp,
                                lineHeight = 22.sp,
                            )
                        }
                    }

                    is AssistantVoiceContent.NumberedOptions -> {
                        NumberedOptionsContent(content = content)
                    }
                }
            }
        }
    }
}

@Composable
private fun NumberedOptionsContent(content: AssistantVoiceContent.NumberedOptions) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = content.title.trimEnd(':') + ":",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 220.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                )
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(content.options, key = { it.number }) { option ->
                OptionRow(option)
            }

            if (!content.footer.isNullOrBlank()) {
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = content.footer,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp,
                    )
                }
            }
        }
    }
}

@Composable
private fun OptionRow(option: OptionItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    shape = CircleShape,
                ),
            contentAlignment = androidx.compose.ui.Alignment.Center,
        ) {
            Text(
                text = option.number.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
        }

        Text(
            text = option.label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 20.sp,
        )
    }
}