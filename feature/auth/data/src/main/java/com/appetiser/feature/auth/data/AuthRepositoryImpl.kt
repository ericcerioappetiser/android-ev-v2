package com.appetiser.feature.auth.data

import com.appetiser.core.domain.accesstoken.AccessToken
import com.appetiser.core.domain.misc.NoInternetException
import com.appetiser.core.domain.user.User
import com.appetiser.core.domain.usernamecheck.UsernameCheck
import com.appetiser.core.domain.usernamecheck.UsernameNotFoundException
import com.appetiser.core.network.model.auth.ChangePasswordDTO
import com.appetiser.core.network.model.auth.CheckResetPasswordDTO
import com.appetiser.core.network.model.auth.RequestResetPasswordDTO
import com.appetiser.core.network.model.auth.ResetPasswordDTO
import com.appetiser.core.network.model.base.BaseErrorResponse
import com.appetiser.core.network.model.usernamecheck.UserNameCheckErrorMetaDTO
import com.appetiser.core.network.utils.ErrorMessages.ERROR_NO_INTERNET_CONNECTION
import com.appetiser.core.network.utils.ErrorMessages.ERROR_USER_NOT_FOUND
import com.appetiser.feature.auth.data.features.session.Session
import com.appetiser.feature.auth.data.features.session.SessionLocalSource
import com.appetiser.feature.auth.network.AuthRemoteSource
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteSource: AuthRemoteSource,
    private val sessionLocalSource: SessionLocalSource,
    private val json: Json
) : AuthRepository {

    override suspend fun checkUsername(username: String): Result<UsernameCheck> = try {
        val response = remoteSource.checkUsername(username)
        Result.success(response)
    } catch (e: HttpException) {
        if (e.code() == 404) {
            // Handle the 404 error specifically for "user not found"
            val errorBody = e.response()?.errorBody()?.string()
            val usernameCheck = parseErrorBody(errorBody)
            Result.failure(
                UsernameNotFoundException(
                    message = usernameCheck?.message ?: ERROR_USER_NOT_FOUND,
                    usernameType = usernameCheck?.meta?.usernameType,
                    authType = usernameCheck?.meta?.authType
                )
            )
        } else {
            Result.failure(e)
        }
    } catch (e: UnknownHostException) {
        Result.failure(NoInternetException(ERROR_NO_INTERNET_CONNECTION))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun loginWithEmail(username: String, password: String): Session {
        val response = remoteSource
            .loginWithEmail(username, password)
        return saveAuthDataToSession(response)
    }

    override suspend fun loginWithPhone(username: String, otp: String): Session {
        val response = remoteSource
            .loginWithPhone(username, otp)
        return saveAuthDataToSession(response)
    }

    override suspend fun checkEmail(email: String) {
        remoteSource.checkEmail(email)
    }

    override suspend fun logout() {
        remoteSource.logout()
        sessionLocalSource.clearSession()
    }

    override suspend fun getUser(): User {
        val session = sessionLocalSource.getSession()
        val user = remoteSource.getUser()
        session.user = user
        sessionLocalSource.saveSession(session)
        return user
    }

    override suspend fun generateOtp(phoneNumber: String) = remoteSource.generateOtp(phoneNumber)

    override suspend fun deleteAccount(verificationToken: String) {
        remoteSource.deleteAccount(verificationToken)
        sessionLocalSource.clearSession()
    }

    override suspend fun verifyOtp(otp: String) =
        remoteSource.verifyOtp(otp = otp)

    override suspend fun verifyPassword(password: String): String =
        remoteSource.verifyPassword(
            password = password
        )

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        newPasswordConfirmation: String
    ) = remoteSource.changePassword(
        ChangePasswordDTO(
            oldPassword = oldPassword,
            newPassword = newPassword,
            newPasswordConfirmation = newPasswordConfirmation
        )
    )

    override suspend fun requestChangeEmail(verificationToken: String?, newEmail: String) {
        remoteSource.requestChangeEmail(verificationToken, newEmail)
    }

    override suspend fun verifyAccountEmail(code: String): Boolean {
        val session = sessionLocalSource.getSession()
        val (isSuccessful, user) = remoteSource.verifyAccountEmail(
            code = code
        )
        return if (isSuccessful) {
            val newSession = sessionLocalSource.getSession()
            newSession.user = user
            sessionLocalSource.saveSession(session = newSession)
            isSuccessful
        } else {
            isSuccessful
        }
    }

    override suspend fun verifyAccountMobilePhone(code: String): Boolean {
        val session = sessionLocalSource.getSession()
        val (isSuccessful, user) = remoteSource.verifyAccountMobilePhone(
            code = code
        )
        return if (isSuccessful) {
            val newSession = sessionLocalSource.getSession()
            newSession.user = user
            sessionLocalSource.saveSession(session = newSession)
            isSuccessful
        } else {
            isSuccessful
        }
    }

    override suspend fun resendEmailVerificationCode(): Boolean = remoteSource.resendEmailVerificationCode()

    override suspend fun resendMobilePhoneVerificationCode(): Boolean = remoteSource.resendMobilePhoneVerificationCode()

    override suspend fun verifyChangeEmail(verificationToken: String, code: String) {
        updateSessionAfterVerification {
            remoteSource.verifyChangeEmail(verificationToken, code)
        }
    }

    override suspend fun verifyChangePhone(verificationToken: String, code: String) {
        updateSessionAfterVerification {
            remoteSource.verifyChangePhone(verificationToken, code)
        }
    }

    private suspend fun updateSessionWithCurrentUser(session: Session) {
        val user = remoteSource.getUser()
        session.user = user
        sessionLocalSource.saveSession(session)
    }

    override suspend fun requestPhoneNumber(verificationToken: String?, phoneNumber: String) {
        remoteSource.requestChangePhone(verificationToken, phoneNumber)
    }

    override suspend fun requestResetPassword(username: String) {
        remoteSource.requestResetPassword(
            RequestResetPasswordDTO(username = username)
        )
    }

    override suspend fun checkResetPassword(username: String, token: String) {
        remoteSource.checkResetPassword(
            CheckResetPasswordDTO(
                username = username,
                token = token
            )
        )
    }

    override suspend fun resetPassword(
        username: String,
        token: String,
        password: String,
        passwordConfirmation: String
    ) {
        remoteSource.resetPassword(
            ResetPasswordDTO(
                username = username,
                token = token,
                password = password,
                passwordConfirmation = passwordConfirmation
            )
        )
    }

    override suspend fun registerWithEmail(
        email: String,
        password: String,
        confirmPassword: String,
        firstName: String,
        lastName: String
    ): Session {
        val response = remoteSource
            .register(
                email = email,
                password = password,
                confirmPassword = confirmPassword,
                firstName = firstName,
                lastName = lastName
            )
        return saveAuthDataToSession(pair = response)
    }

    override suspend fun registerWithMobile(phone: String, otp: String): Session {
        val response = remoteSource
            .register(
                phone = phone,
                otp = otp
            )
        return saveAuthDataToSession(pair = response)
    }

    private suspend fun saveAuthDataToSession(pair: Pair<User, AccessToken>): Session {
        val (user, accessToken) = pair

        val session = sessionLocalSource.getSession()

        session.user = user
        session.accessToken = accessToken

        return sessionLocalSource.saveSession(session)
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun parseErrorBody(errorBody: String?): BaseErrorResponse<UserNameCheckErrorMetaDTO>? = try {
        errorBody?.let {
            json.decodeFromString(it)
        }
    } catch (e: Exception) {
        null
    }

    private suspend fun updateSessionAfterVerification(
        verificationAction: suspend () -> Unit
    ) {
        val session = sessionLocalSource.getSession()
        verificationAction()
        updateSessionWithCurrentUser(session)
    }
}
