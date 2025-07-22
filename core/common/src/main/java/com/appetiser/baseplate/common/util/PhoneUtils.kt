package com.appetiser.baseplate.common.util

object PhoneUtils {

    private const val REGEX_VALID_PHONE_WITH_COUNTRY_CODE = "^\\+?[1-9]\\d{1,14}\$"
    private const val REGEX_VALID_PHONE_LOCAL = "^09\\d{0,9}$"
    private const val REGEX_VALID_EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"

    fun isValidPhoneNumber(input: String): Boolean {
        val localRegex = Regex(REGEX_VALID_PHONE_LOCAL)
        val phoneRegex = Regex(REGEX_VALID_PHONE_WITH_COUNTRY_CODE)
        return input.matches(phoneRegex) || input.matches(localRegex)
    }

    fun isValidEmail(input: String): Boolean {
        val emailRegex = Regex(REGEX_VALID_EMAIL)
        return emailRegex.matches(input)
    }

    fun formatPhoneNumberWithCountryCode(phoneNumber: String, prefix: String): String {
        val sanitizedNumber = sanitizePhoneNumber(phoneNumber, prefix)

        return if (sanitizedNumber.startsWith(prefix)) {
            "+$sanitizedNumber"
        } else {
            "+$prefix$sanitizedNumber"
        }
    }


    private fun sanitizePhoneNumber(phoneNumber: String, prefix: String): String {
        return phoneNumber.removePrefix("+$prefix")
            .replace(",", "")
            .trim()
    }
}




