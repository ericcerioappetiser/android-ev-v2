package com.appetiser.feature.auth.network

import com.appetiser.core.domain.accesstoken.AccessToken
import com.appetiser.core.domain.user.User
import com.appetiser.core.domain.usernamecheck.UsernameCheck
import com.appetiser.core.network.model.auth.ChangePasswordDTO
import com.appetiser.core.network.model.auth.CheckResetPasswordDTO
import com.appetiser.core.network.model.auth.RequestResetPasswordDTO
import com.appetiser.core.network.model.auth.ResetPasswordDTO

interface AuthRemoteSource {

    suspend fun checkUsername(username: String): UsernameCheck

    suspend fun loginWithEmail(username: String, password: String): Pair<User, AccessToken>

    suspend fun loginWithPhone(username: String, otp: String): Pair<User, AccessToken>

    suspend fun checkEmail(email: String)

    suspend fun logout()

    suspend fun getUser(): User

    suspend fun generateOtp(phoneNumber: String)

    suspend fun deleteAccount(verificationToken: String)

    suspend fun verifyOtp(otp: String): String

    suspend fun verifyPassword(password: String): String

    suspend fun changePassword(body: ChangePasswordDTO): Boolean

    suspend fun requestChangeEmail(verificationToken: String?, newEmail: String)

    /**
     * @return Returns pair of boolean true/false if verification succeeds and user session.
     */
    suspend fun verifyAccountEmail(code: String): Pair<Boolean, User>

    suspend fun verifyAccountMobilePhone(code: String): Pair<Boolean, User>

    suspend fun resendEmailVerificationCode(): Boolean

    suspend fun resendMobilePhoneVerificationCode(): Boolean

    /**
     * Verifies changing of email.
     *
     * @param verificationToken token returned from [verifyPassword]
     * @param code verification code
     */
    suspend fun verifyChangeEmail(verificationToken: String, code: String)

    suspend fun verifyChangePhone(verificationToken: String, code: String)

    suspend fun register(
        email: String = "",
        phone: String = "",
        password: String = "",
        confirmPassword: String = "",
        firstName: String = "",
        lastName: String = "",
        otp: String = ""
    ): Pair<User, AccessToken>

    suspend fun requestChangePhone(
        verificationToken: String?,
        newEmail: String
    )

    suspend fun requestResetPassword(body: RequestResetPasswordDTO)

    suspend fun checkResetPassword(body: CheckResetPasswordDTO)

    suspend fun resetPassword(body: ResetPasswordDTO)
}
