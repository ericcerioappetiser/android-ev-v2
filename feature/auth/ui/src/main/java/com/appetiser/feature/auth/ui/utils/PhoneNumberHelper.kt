package com.appetiser.feature.auth.ui.utils

import android.text.TextUtils
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import java.util.Locale
import javax.inject.Inject

class PhoneNumberHelper @Inject constructor() {

    /**
     * Returns phone number in international format.
     *
     * @param phoneNumber phone number to format
     * @param countryNameCode country name code. (i.e. "au", "ph", "cn")
     */
    fun getInternationalFormattedPhoneNumber(
        phoneNumber: String,
        countryNameCode: String,
        format: PhoneNumberUtil.PhoneNumberFormat = PhoneNumberUtil.PhoneNumberFormat.E164
    ): String = try {
        val phoneNumberUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()
        val numberProto: Phonenumber.PhoneNumber = phoneNumberUtil.parse(
            phoneNumber,
            countryNameCode.uppercase(Locale.getDefault())
        )

        phoneNumberUtil
            .format(
                numberProto,
                format
            )
    } catch (e: Exception) {
        phoneNumber
    }

    /**
     * Returns pair of phone country code and the number.
     *
     * @param phoneNumber phone number to format
     * @param countryNameCode country name code. (i.e. "au", "ph", "cn")
     */
    fun getCountryCodeAndNumber(phoneNumber: String, countryNameCode: String): Pair<String, String> = try {
        val phoneNumberUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance()
        val numberProto: Phonenumber.PhoneNumber = phoneNumberUtil.parse(
            phoneNumber,
            countryNameCode.uppercase(Locale.getDefault())
        )

        numberProto.countryCode.toString() to numberProto.nationalNumber.toString()
    } catch (e: Exception) {
        "" to phoneNumber
    }

    fun getDeviceCountryDialingCode(countryCode: String): String? = try {
        val phoneUtil = PhoneNumberUtil.getInstance()
        val dialingCode = phoneUtil.getCountryCodeForRegion(countryCode.uppercase(Locale.getDefault()))
        dialingCode.toString()
    } catch (e: Exception) {
        null // Handle invalid country codes
    }


    /**
     * Validates phone number format as per [country].
     */
    fun validatePhoneNumber(phoneNumber: String, country: String): Boolean {
        return try {
            val phoneNumberUtils = PhoneNumberUtil.getInstance()
            val numberProto =
                phoneNumberUtils
                    .parse(
                        phoneNumber,
                        country.uppercase(Locale.getDefault())
                    )
            phoneNumberUtils.isValidNumber(numberProto)
        } catch (e: Exception) {
            return false
        }
    }

    /**
     * Returns true if [value] contains digits only and "+" character.
     */
    fun isNumberDigitsOnly(value: CharSequence): Boolean {
        var trimmed = value.trim()

        if (trimmed.isEmpty()) {
            return false
        }

        trimmed = trimmed.replace(Regex("\\s"), "")

        if (trimmed.startsWith("+")) {
            trimmed = trimmed.subSequence(1, trimmed.length)
        }

        return TextUtils.isDigitsOnly(trimmed)
    }

    fun formatPhoneNumber(phoneNumber: String): String {
        val numericPhoneNumber = phoneNumber.replace(Regex("[^0-9]"), "")
        if (numericPhoneNumber.length >= 12) {
            return numericPhoneNumber
        }

        val formattedNumber = buildString {
            append(numericPhoneNumber.take(4))
            if (numericPhoneNumber.length > 4) {
                append(" ")

                append(numericPhoneNumber.substring(4, minOf(7, numericPhoneNumber.length)))
            }
            if (numericPhoneNumber.length > 7) {
                append(" ")
                append(numericPhoneNumber.substring(7))
            }
        }
        return formattedNumber
    }
}
