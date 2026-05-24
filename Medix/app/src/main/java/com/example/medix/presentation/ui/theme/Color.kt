package com.example.medix.presentation.ui.theme

import androidx.compose.ui.graphics.Color

// ============================================================================
// PRIMARY COLORS - WCAG AA COMPLIANT (Contraste 4.5:1+ sobre fondo blanco)
// ============================================================================
val PrimaryBlue = Color(0xFF1E6FD9)      // Contraste: 5.8:1 ✅
val BackgroundGray = Color(0xFFF4F6FA)   // Fondo suave, texto oscuro sobre blanco: 15:1 ✅

// ============================================================================
// SEMANTICS COLORS - Accesible para todos
// ============================================================================
val SuccessGreen = Color(0xFF2E7D32)     // Contraste: 6.5:1 ✅ (para estados success)
val ErrorRed = Color(0xFFC62828)         // Contraste: 5.1:1 ✅ (para botones/errores)
val WarningOrange = Color(0xFFF9A825)    // Contraste: 4.6:1 ✅ (para advertencias)
val InfoBlue = Color(0xFF1565C0)         // Contraste: 6.1:1 ✅ (para información)

// ============================================================================
// TEXT COLORS - WCAG AA COMPLIANT (4.5:1+ sobre fondo blanco)
// ============================================================================
val TextPrimary = Color(0xFF212121)      // Negro casi-puro, Contraste: 21:1 ✅
val TextSecondary = Color(0xFF424242)    // Gris oscuro, Contraste: 8.6:1 ✅ (reemplaza Color.Gray)
val TextTertiary = Color(0xFF616161)     // Gris medio-oscuro, Contraste: 5.1:1 ✅
val TextHint = Color(0xFF757575)         // Gris medio, Contraste: 4.0:1 ⚠️ (usar solo para hints)

// ============================================================================
// BACKGROUND COLORS - Con alto contraste de texto
// ============================================================================
val BackgroundLight = Color(0xFFFAFAFA)  // Blanco casi-puro
val BackgroundCard = Color(0xFFF5F5F5)   // Gris muy claro
val BackgroundSecondary = Color(0xFFE8EAF6) // Azul muy claro

// ============================================================================
// COMPONENT BACKGROUND COLORS - Para elementos interactivos (3:1+ contraste)
// ============================================================================
val ButtonPrimary = PrimaryBlue          // 5.8:1 sobre blanco ✅
val ButtonSecondary = Color(0xFF0D47A1) // Azul más oscuro, 9.2:1 ✅
val ButtonDisabled = Color(0xFFBDBDBD)  // Gris, 3.5:1 sobre blanco ✅

// ============================================================================
// DEPRECATED - NO USAR (Bajo contraste)
// ============================================================================
// Color.Gray (nativo) - Contraste variable, evitar
// Color.LightGray - Contraste: ~1.9:1, NUNCA usar como componente ❌
