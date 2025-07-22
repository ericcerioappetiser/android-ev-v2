package com.appetiser.baseplate.common.util

object PasswordValidatorUtil {

    fun validatePassword(password: String): Pair<PasswordErrorType, Boolean> {
        return when {
            password.isEmpty() -> PasswordErrorType.PASSWORD_IS_EMPTY to false
            password.length < PASSWORD_MIN_LENGTH -> PasswordErrorType.PASSWORD_BELOW_MIN_LENGTH to false
            password.length > PASSWORD_MAX_LENGTH -> PasswordErrorType.PASSWORD_EXCEEDS_MAX_LENGTH to false
            else -> PasswordErrorType.PASSWORD_VALID to true
        }
    }

    private fun validatePasswordNotEmpty(password: String): Boolean {
        return password.isNotEmpty() && password.length >= PASSWORD_MIN_LENGTH
    }
}

enum class PasswordErrorType {
    PASSWORD_BELOW_MIN_LENGTH,
    PASSWORD_EXCEEDS_MAX_LENGTH,
    PASSWORD_IS_EMPTY,
    PASSWORD_VALID,
}
