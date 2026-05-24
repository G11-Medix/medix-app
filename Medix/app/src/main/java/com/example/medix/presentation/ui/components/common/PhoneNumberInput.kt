package com.example.medix.presentation.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
        Box {
            OutlinedButton(
                onClick = { expanded = true },
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier.height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "${selectedCountry.flagEmoji} ${selectedCountry.dialCode}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Desplegar selector de país",
                    modifier = Modifier.size(20.dp)
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

        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = phoneNumber,
            onValueChange = { input ->
                onPhoneNumberChange(input.filter(Char::isDigit))
            },
            label = { Text(label) },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}
