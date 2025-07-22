package com.appetiser.feature.auth.network

import com.appetiser.core.domain.accesstoken.AccessToken
import com.appetiser.core.domain.user.User
import com.appetiser.core.domain.usernamecheck.UsernameCheck
import com.appetiser.core.network.AuthApiService
import com.appetiser.core.network.model.auth.ChangePasswordDTO
import com.appetiser.core.network.model.auth.CheckResetPasswordDTO
import com.appetiser.core.network.model.auth.RequestResetPasswordDTO
import com.appetiser.core.network.model.auth.ResetPasswordDTO
import com.appetiser.core.network.model.base.BaseRemoteSource
import com.appetiser.core.network.model.usernamecheck.toDomain
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRemoteSourceImpl @Inject constructor(
    private val json: Json,
    private val apiServices: AuthApiService
) : BaseRemoteSource(),
    AuthRemoteSource {

    companion object {

        private const val VIA = "via"
        private const val EMAIL = "email"
        private const val TOKEN = "token"
        private const val USERNAME = "username"
        private const val FIRST_NAME = "first_name"
        private const val LAST_NAME = "last_name"
        private const val PASSWORD = "password"
        private const val PASSWORD_CONFIRMATION = "password_confirmation"
        private const val PHONE_NUMBER = "phone_number"
        private const val OTP = "otp"
    }

    override suspend fun checkUsername(username: String): UsernameCheck =
        apiServices.checkUsernameIfExists(username = username).data.toDomain()

    override suspend fun loginWithEmail(username: String, password: String): Pair<User, AccessToken> {
        val fields = mapOf(
            USERNAME to username,
            PASSWORD to password
        )
        return apiServices
            .login(fields = fields)
            .data.toUserAndAccessTokenPair()
    }

    override suspend fun loginWithPhone(username: String, otp: String): Pair<User, AccessToken> {
        val fields = mapOf(
            "username" to username,
            "otp" to otp
        )
        return apiServices.login(fields = fields).data.toUserAndAccessTokenPair()
    }

    override suspend fun checkEmail(email: String) {
        apiServices.checkEmail(email = email)
    }

    override suspend fun logout() {
        apiServices.logout()
    }

    override suspend fun getUser(): User {
        val response = apiServices.getUser()
        return response.data.toDomain()
    }

    override suspend fun generateOtp(phoneNumber: String) {
        apiServices.generateOTP(phoneNumber = phoneNumber)
    }

    override suspend fun deleteAccount(verificationToken: String) = apiServices.deleteAccount(verificationToken)

    override suspend fun verifyOtp(otp: String): String {
        val jsonObject = mapOf(OTP to otp)
        val jsonBody = json.mapToJson(jsonObject)
        return apiServices
            .getVerificationToken(getJsonRequestBody(jsonBody))
            .data
            .token
    }

    override suspend fun verifyPassword(password: String): String {
        val jsonBody = mapOf(PASSWORD to password)
        val jsonString = json.mapToJson(jsonBody)
        return apiServices
            .getVerificationToken(getJsonRequestBody(jsonString))
            .data
            .token
    }

    override suspend fun changePassword(
        body: ChangePasswordDTO
    ) = apiServices.changePassword(body).success

    override suspend fun requestChangeEmail(verificationToken: String?, newEmail: String) {
        val jsonBody = mapOf(EMAIL to newEmail)
        val jsonString = json.mapToJson(jsonBody)
        apiServices.requestChangeEmail(
            verificationToken = verificationToken,
            requestBody = getJsonRequestBody(jsonString)
        )
    }

    override suspend fun verifyAccountEmail(code: String): Pair<Boolean, User> {
        val jsonBody = mapOf(
            TOKEN to code,
            VIA to EMAIL
        )
        val jsonString = json.mapToJson(jsonBody)
        val response = apiServices
            .verifyAccount(
                getJsonRequestBody(jsonString)
            )
        val user = response.data.toDomain()
        return user.emailVerified to user
    }

    override suspend fun verifyAccountMobilePhone(code: String): Pair<Boolean, User> {
        val jsonBody = mapOf(
            TOKEN to code,
            VIA to PHONE_NUMBER
        )
        val jsonString = json.mapToJson(jsonBody)
        val response = apiServices
            .verifyAccount(
                getJsonRequestBody(jsonString)
            )
        val user = response.data.toDomain()
        return user.phoneNumberVerified to user
    }

    override suspend fun resendEmailVerificationCode(): Boolean {
        val jsonBody = mapOf(VIA to EMAIL)
        val jsonString = json.mapToJson(jsonBody)
        return apiServices
            .resendVerificationCode(getJsonRequestBody(jsonString))
            .success
    }

    override suspend fun resendMobilePhoneVerificationCode(): Boolean {
        val jsonBody = mapOf(VIA to PHONE_NUMBER)
        val jsonString = json.mapToJson(jsonBody)
        return apiServices
            .resendVerificationCode(getJsonRequestBody(jsonString))
            .success
    }

    override suspend fun verifyChangeEmail(verificationToken: String, code: String) {
        val jsonBody = mapOf(TOKEN to code)
        val jsonString = json.mapToJson(jsonBody)
        return apiServices.verifyChangeEmail(
            verificationToken = verificationToken,
            requestBody = getJsonRequestBody(jsonString)
        )
    }

    override suspend fun verifyChangePhone(verificationToken: String, code: String) {
        val jsonBody = mapOf(TOKEN to code)
        val jsonString = json.mapToJson(jsonBody)
        return apiServices.verifyChangePhone(
            verificationToken = verificationToken,
            requestBody = getJsonRequestBody(jsonString)
        )
    }

    override suspend fun register(
        email: String,
        phone: String,
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String,
        otp: String
    ): Pair<User, AccessToken> {
        val registerBody = mutableMapOf<String, String>()
        if (email.isNotEmpty()) registerBody[EMAIL] = email
        if (phone.isNotEmpty()) registerBody[PHONE_NUMBER] = phone
        if (firstName.isNotEmpty()) registerBody[FIRST_NAME] = firstName
        if (lastName.isNotEmpty()) registerBody[LAST_NAME] = lastName
        if (password.isNotEmpty()) registerBody[PASSWORD] = password
        if (confirmPassword.isNotEmpty()) registerBody[PASSWORD_CONFIRMATION] = confirmPassword
        if (otp.isNotEmpty()) registerBody[OTP] = otp
        val jsonString = json.mapToJson(registerBody)
        return apiServices
            .register(getJsonRequestBody(jsonString))
            .data.toUserAndAccessTokenPair()
    }

    override suspend fun requestChangePhone(verificationToken: String?, phoneNumber: String) {
        val jsonBody = mapOf(
            "phone_number" to phoneNumber
        )
        val jsonString = json.mapToJson(jsonBody)
        apiServices.requestChangePhone(verificationToken, getJsonRequestBody(jsonString = jsonString))
    }

    override suspend fun requestResetPassword(
        body: RequestResetPasswordDTO
    ) = apiServices.requestResetPassword(body)

    override suspend fun checkResetPassword(
        body: CheckResetPasswordDTO
    ) = apiServices.checkResetPassword(body)

    override suspend fun resetPassword(
        body: ResetPasswordDTO
    ) = apiServices.resetPassword(body)
}
