package com.appetiser.feature.auth.data

import com.appetiser.core.domain.user.User
import com.appetiser.core.domain.usernamecheck.UsernameCheck
import com.appetiser.feature.auth.data.features.session.Session

interface AuthRepository {

    suspend fun checkUsername(username: String): Result<UsernameCheck>

    suspend fun loginWithEmail(username: String, password: String): Session

    suspend fun loginWithPhone(username: String, otp: String): Session

    suspend fun checkEmail(email: String)

    suspend fun logout()

    suspend fun getUser(): User

    suspend fun generateOtp(phoneNumber: String)

    suspend fun deleteAccount(verificationToken: String)

    suspend fun verifyOtp(
        otp: String
    ): String

    suspend fun verifyPassword(
        password: String
    ): String

    suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        newPasswordConfirmation: String
    ): Boolean

    suspend fun requestChangeEmail(
        verificationToken: String?,
        newEmail: String
    )

    suspend fun verifyAccountEmail(code: String): Boolean

    suspend fun verifyAccountMobilePhone(code: String): Boolean

    suspend fun resendEmailVerificationCode(): Boolean

    suspend fun resendMobilePhoneVerificationCode(): Boolean

    /**
     * Verifies changing of email.
     *
     * @param verificationToken token returned from [verifyPassword]
     * @param code verification code
     */
    suspend fun verifyChangeEmail(
        verificationToken: String,
        code: String
    )

    suspend fun verifyChangePhone(
        verificationToken: String,
        code: String
    )

    suspend fun registerWithEmail(
        email: String = "",
        password: String = "",
        confirmPassword: String = "",
        firstName: String = "",
        lastName: String = ""
    ): Session

    /**
     * If user uses phone number for logging in. Pass only [phone].
     */
    suspend fun registerWithMobile(
        phone: String = "",
        otp: String = ""
    ): Session

    suspend fun requestPhoneNumber(
        verificationToken: String?,
        phoneNumber: String
    )

    suspend fun requestResetPassword(
        username: String
    )

    suspend fun checkResetPassword(
        username: String,
        token: String
    )

    suspend fun resetPassword(
        username: String,
        token: String,
        password: String,
        passwordConfirmation: String
    )
}
