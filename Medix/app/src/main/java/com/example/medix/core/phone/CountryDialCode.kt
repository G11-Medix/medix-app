package com.example.medix.core.phone

data class CountryDialCode(
    val iso2: String,
    val name: String,
    val dialCode: String,
    val flagEmoji: String,
)

object CountryDialCodes {
    const val DEFAULT_DIAL_CODE = "+57"

    val all: List<CountryDialCode> = listOf(
        CountryDialCode("CO", "Colombia", "+57", "🇨🇴"),
        CountryDialCode("AR", "Argentina", "+54", "🇦🇷"),
        CountryDialCode("BO", "Bolivia", "+591", "🇧🇴"),
        CountryDialCode("BR", "Brasil", "+55", "🇧🇷"),
        CountryDialCode("CL", "Chile", "+56", "🇨🇱"),
        CountryDialCode("CR", "Costa Rica", "+506", "🇨🇷"),
        CountryDialCode("DO", "Republica Dominicana", "+1", "🇩🇴"),
        CountryDialCode("EC", "Ecuador", "+593", "🇪🇨"),
        CountryDialCode("ES", "Espana", "+34", "🇪🇸"),
        CountryDialCode("GT", "Guatemala", "+502", "🇬🇹"),
        CountryDialCode("HN", "Honduras", "+504", "🇭🇳"),
        CountryDialCode("MX", "Mexico", "+52", "🇲🇽"),
        CountryDialCode("PA", "Panama", "+507", "🇵🇦"),
        CountryDialCode("PE", "Peru", "+51", "🇵🇪"),
        CountryDialCode("PY", "Paraguay", "+595", "🇵🇾"),
        CountryDialCode("SV", "El Salvador", "+503", "🇸🇻"),
        CountryDialCode("US", "Estados Unidos", "+1", "🇺🇸"),
        CountryDialCode("UY", "Uruguay", "+598", "🇺🇾"),
        CountryDialCode("VE", "Venezuela", "+58", "🇻🇪"),
    )

    fun sanitizeDialCode(code: String): String {
        val digits = code.filter(Char::isDigit)
        return if (digits.isBlank()) DEFAULT_DIAL_CODE else "+$digits"
    }

    fun findByDialCode(code: String): CountryDialCode? {
        val normalizedCode = sanitizeDialCode(code)
        return all.firstOrNull { it.dialCode == normalizedCode }
    }

    fun matchByInternationalPhone(rawPhone: String): Pair<String, String> {
        val digits = rawPhone.filter(Char::isDigit)
        if (digits.isBlank()) {
            return DEFAULT_DIAL_CODE to ""
        }

        val bestMatch = all
            .sortedByDescending { it.dialCode.length }
            .firstOrNull { country -> digits.startsWith(country.dialCode.removePrefix("+")) }

        if (bestMatch == null) {
            return DEFAULT_DIAL_CODE to digits
        }

        val dialDigits = bestMatch.dialCode.removePrefix("+")
        val nationalNumber = digits.removePrefix(dialDigits)
        return bestMatch.dialCode to nationalNumber
    }
}

