package com.example.medix.presentation.ui.components.voice

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AssistantVoiceResponseParserTest {

    @Test
    fun parseDaysList_withFooter() {
        val input = "Tengo estos dias disponibles: 1. 2026-04-27, 2. 2026-04-28, 3. 2026-04-29. ¿Cual prefiere? Si desea mas fechas, puede decir mas fechas."

        val parsed = parseAssistantVoiceResponse(input)

        assertTrue(parsed is AssistantVoiceContent.NumberedOptions)
        val optionsContent = parsed as AssistantVoiceContent.NumberedOptions

        assertEquals("Tengo estos dias disponibles", optionsContent.title)
        assertEquals(3, optionsContent.options.size)
        assertEquals("2026-04-27", optionsContent.options[0].label)
        assertEquals("2026-04-28", optionsContent.options[1].label)
        assertEquals("2026-04-29", optionsContent.options[2].label)
        assertEquals("¿Cual prefiere? Si desea mas fechas, puede decir mas fechas", optionsContent.footer)
    }

    @Test
    fun parseHoursList_withManyOptions() {
        val input = "Tengo estas horas disponibles: 1. 08:00, 2. 08:30, 3. 09:00, 4. 10:00, 5. 10:30, 6. 11:00, 7. 11:30, 8. 14:00."

        val parsed = parseAssistantVoiceResponse(input)

        assertTrue(parsed is AssistantVoiceContent.NumberedOptions)
        val optionsContent = parsed as AssistantVoiceContent.NumberedOptions

        assertEquals("Tengo estas horas disponibles", optionsContent.title)
        assertEquals(8, optionsContent.options.size)
        assertEquals("08:00", optionsContent.options[0].label)
        assertEquals("14:00", optionsContent.options.last().label)
    }

    @Test
    fun parseInstitutionsList() {
        val input = "Estas son las instituciones disponibles: 1. Clinica Central, 2. Centro Medico Norte. ¿Cual elige?"

        val parsed = parseAssistantVoiceResponse(input)

        assertTrue(parsed is AssistantVoiceContent.NumberedOptions)
        val optionsContent = parsed as AssistantVoiceContent.NumberedOptions

        assertEquals("Estas son las instituciones disponibles", optionsContent.title)
        assertEquals(2, optionsContent.options.size)
        assertEquals("Clinica Central", optionsContent.options[0].label)
        assertEquals("Centro Medico Norte", optionsContent.options[1].label)
        assertEquals("¿Cual elige?", optionsContent.footer)
    }

    @Test
    fun parsePlainText_whenNoList() {
        val input = "¿Para que especialidad necesita la cita medica?"

        val parsed = parseAssistantVoiceResponse(input)

        assertTrue(parsed is AssistantVoiceContent.PlainText)
        val plainText = parsed as AssistantVoiceContent.PlainText
        assertEquals(input, plainText.text)
    }

    @Test
    fun normalizeBrokenHourTokens() {
        val input = "Tengo estas horas disponibles: 1. 08:\n00, 2. 08:\n30, 3. 09:\n00."

        val parsed = parseAssistantVoiceResponse(input)

        assertTrue(parsed is AssistantVoiceContent.NumberedOptions)
        val optionsContent = parsed as AssistantVoiceContent.NumberedOptions
        assertEquals("08:00", optionsContent.options[0].label)
        assertEquals("08:30", optionsContent.options[1].label)
        assertEquals("09:00", optionsContent.options[2].label)
    }
}

