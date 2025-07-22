package com.appetiser.feature.auth.ui.utils

import com.appetiser.baseplate.common.util.PASSWORD_MAX_LENGTH
import com.appetiser.baseplate.common.util.PASSWORD_MIN_LENGTH
import com.appetiser.core.domain.user.User

object AuthenticationUtils {

    fun validatePasswordReachesMinLength(password: String): Boolean = password.isNotEmpty() && password.length >= PASSWORD_MIN_LENGTH

    fun validatePasswordNotExceedsMaxLength(password: String): Boolean = password.isNotEmpty() && password.length <= PASSWORD_MAX_LENGTH

    /**
     * Used to check for user auth state after login or forgot password flow.
     */
    fun getUserAuthState(user: User): AuthState {
        if (!user.verified) {
            return AuthState.NotVerified
        }
        return when {
            user.hasFullName.not() -> {
                AuthState.NoFullName
            }

            user.hasBirthdate.not() -> {
                AuthState.NoBirthDate
            }

            user.hasProfilePhoto.not() -> {
                AuthState.NoProfilePhoto
            }



            user.hasFullName.not() -> {
                AuthState.NoBirthDate
            }

            else -> {
                AuthState.Completed
            }
        }
    }
}

sealed class AuthState {
    data object NotVerified : AuthState()

    data object NoFullName : AuthState()

    data object NoProfilePhoto : AuthState()

    // skip all auth state if user is done onboarding (when user clicked the skip button in onboarding upload photo)
    data object Completed : AuthState()
    data object NoBirthDate : AuthState()
}
