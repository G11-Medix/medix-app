package com.example.medix.presentation.ui.components.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    Row(modifier = modifier.fillMaxWidth()) {
        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text("${selectedCountry.flagEmoji} ${selectedCountry.dialCode}")
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Seleccionar codigo de pais",
                )
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
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

        Spacer(modifier = Modifier.width(8.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = phoneNumber,
            onValueChange = { input ->
                onPhoneNumberChange(input.filter(Char::isDigit))
            },
            label = { Text(label) },
            singleLine = true,
        )
    }
}

