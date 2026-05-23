package com.example.medix.presentation.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.medix.core.phone.CountryDialCodes

@Composable
fun PhoneNumberInput(
    modifier: Modifier = Modifier,
    countryCode: String,
    phoneNumber: String,
    onCountryCodeChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    label: String,
) {
    var expanded by remember { mutableStateOf(false) }

    val countries = CountryDialCodes.all
    val selectedCountry = CountryDialCodes.findByDialCode(countryCode)
        ?: countries.first { it.dialCode == CountryDialCodes.DEFAULT_DIAL_CODE }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        // 🔥 BOTÓN COMPACTO
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                contentPadding = PaddingValues(
                    horizontal = 8.dp,  // 👈 menos espacio lateral
                    vertical = 4.dp     // 👈 menos altura interna
                ),
                modifier = Modifier.height(56.dp) // 👈 igual altura que TextField
            ) {
                Text(
                    text = "${selectedCountry.flagEmoji} ${selectedCountry.dialCode}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp) // 👈 icono más pequeño
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                countries.forEach { country ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "${country.flagEmoji} ${country.name} (${country.dialCode})",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        onClick = {
                            onCountryCodeChange(country.dialCode)
                            expanded = false
                        },
                    )
                }
            }
        }

        // 📱 CAMPO TELÉFONO (más espacio)
        OutlinedTextField(
            modifier = Modifier
                .weight(1f) // 👈 ocupa todo el espacio restante
                .height(56.dp),
            value = phoneNumber,
            onValueChange = { input ->
                onPhoneNumberChange(input.filter(Char::isDigit))
            },
            label = { Text(label) },
            singleLine = true,
        )
    }
}