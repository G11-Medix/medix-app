package com.example.medix.presentation.ui.components.voice

import java.text.Normalizer

private val supportedListTitles = listOf(
    "tengo estos dias disponibles",
    "tengo estas horas disponibles",
    "estas son las instituciones disponibles",
    "estas son las citas encontradas",
)

private val optionStartRegex = Regex("""(\d+)\.\s*""")
private val optionChunkRegex = Regex(
    pattern = """(\d+)\.\s*(.*?)(?=(?:,\s*\d+\.\s*)|(?:\s+\d+\.\s*)|$)""",
    options = setOf(RegexOption.DOT_MATCHES_ALL),
)
private val footerMarkerRegex = Regex(
    pattern = """(?i)(¿|\?\s*si desea|si desea|cual prefiere|cu[aá]l prefiere|cual elige|cu[aá]l elige|puede decir|puede escoger)""",
)

fun parseAssistantVoiceResponse(raw: String): AssistantVoiceContent {
    val normalized = normalizeRawAssistantText(raw)
    if (normalized.isBlank()) {
        return AssistantVoiceContent.PlainText("")
    }

    val optionMarkers = optionStartRegex.findAll(normalized).toList()
    if (optionMarkers.isEmpty()) {
        return AssistantVoiceContent.PlainText(normalized)
    }

    val colonIndex = normalized.indexOf(':')
    val titleCandidate = normalized.substringBefore(':').trim().trimEnd('.')
    val hasKnownTitle = colonIndex > 0 && isKnownListTitle(titleCandidate)

    val options = optionChunkRegex.findAll(normalized)
        .map { match ->
            val number = match.groupValues[1].toIntOrNull() ?: return@map null
            val label = cleanupOptionLabel(match.groupValues[2])
            if (label.isBlank()) null else OptionItem(number = number, label = label)
        }
        .filterNotNull()
        .toList()

    if (options.isEmpty() || (!hasKnownTitle && options.size < 2)) {
        return AssistantVoiceContent.PlainText(normalized)
    }

    var footer: String? = null
    val mutableOptions = options.toMutableList()

    val lastOption = mutableOptions.last()
    val split = splitFooterFromLastOption(lastOption.label)
    if (split != null) {
        mutableOptions[mutableOptions.lastIndex] = lastOption.copy(label = split.first)
        footer = split.second
    }

    val filteredOptions = mutableOptions.filter { it.label.isNotBlank() }
    if (filteredOptions.isEmpty()) {
        return AssistantVoiceContent.PlainText(normalized)
    }

    val title = when {
        hasKnownTitle -> titleCandidate
        colonIndex > 0 && titleCandidate.isNotBlank() -> titleCandidate
        else -> "Opciones disponibles"
    }

    return AssistantVoiceContent.NumberedOptions(
        title = title,
        options = filteredOptions,
        footer = footer,
    )
}

private fun normalizeRawAssistantText(raw: String): String {
    var result = raw
        .replace("\r\n", "\n")
        .replace('\r', '\n')

    // Corrige horas cortadas como "08:\n00".
    result = result.replace(Regex("""\b(\d{1,2})\s*:\s*\n+\s*(\d{2})\b"""), "$1:$2")

    // Corrige fechas partidas en múltiples líneas.
    result = result.replace(Regex("""\b(\d{4})\s*-\s*\n+\s*(\d{2})\b"""), "$1-$2")
    result = result.replace(Regex("""\b(\d{4}-\d{2})\s*-\s*\n+\s*(\d{2})\b"""), "$1-$2")

    // Normaliza separaciones extrañas para no romper labels.
    result = result.replace(Regex("""\n+"""), " ")
    result = result.replace(Regex("""\s+"""), " ")

    return result.trim()
}

private fun cleanupOptionLabel(input: String): String {
    return input
        .trim()
        .trimStart(',', ';')
        .trimEnd(',', ';', '.', ':')
        .replace(Regex("""\s+"""), " ")
}

private fun splitFooterFromLastOption(lastLabel: String): Pair<String, String>? {
    val marker = footerMarkerRegex.find(lastLabel) ?: return null
    if (marker.range.first <= 0) return null

    val optionText = cleanupOptionLabel(lastLabel.substring(0, marker.range.first))
    val footerText = lastLabel.substring(marker.range.first)
        .trim()
        .trimEnd(',', ';', '.')
        .replace(Regex("""\s+"""), " ")

    if (optionText.isBlank() || footerText.isBlank()) return null
    return optionText to footerText
}

private fun isKnownListTitle(title: String): Boolean {
    val normalizedTitle = normalizeForCompare(title)
    return supportedListTitles.any { normalizedTitle.contains(it) }
}

private fun normalizeForCompare(value: String): String {
    return Normalizer.normalize(value, Normalizer.Form.NFD)
        .replace(Regex("""\p{M}+"""), "")
        .lowercase()
        .replace(Regex("""\s+"""), " ")
        .trim()
}

