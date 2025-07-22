package com.appetiser.core.network

import com.appetiser.core.network.model.auth.ChangePasswordDTO
import com.appetiser.core.network.model.auth.CheckResetPasswordDTO
import com.appetiser.core.network.model.auth.RequestResetPasswordDTO
import com.appetiser.core.network.model.auth.ResetPasswordDTO
import com.appetiser.core.network.model.base.BaseResponse
import com.appetiser.core.network.model.base.BaseResponseNew
import com.appetiser.core.network.model.user.AuthDataResponse
import com.appetiser.core.network.model.user.UserDTO
import com.appetiser.core.network.model.user.UserDataResponse
import com.appetiser.core.network.model.usernamecheck.UsernameCheckDTO
import com.appetiser.core.network.model.verification.GetVerificationTokenResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApiService {

    /**
     * The TODOs here are BP endpoints location in Bruno.
     * You can refer to this link to proceed with creating the endpoints
     * @see <a href="https://gitlab.com/appetiser/baseplate/backend/baseplate-backend-spec">Bruno Spec</a>
     */

    // region Authentication
    // TODO POST     Social Login - Register
    // TODO POST     Social Login - Register - JWT
    // TODO POST     Ephemeral Key

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(@FieldMap fields: Map<String, @JvmSuppressWildcards Any>): AuthDataResponse

    @FormUrlEncoded
    @POST("auth/otp/generate")
    suspend fun generateOTP(
        @Field("phone_number") phoneNumber: String
    )

    @Multipart
    @POST("auth/check-username")
    suspend fun checkUsernameIfExists(
        @Part("username") username: String
    ): BaseResponseNew<UsernameCheckDTO>

    @Multipart
    @POST("auth/check-email")
    suspend fun checkEmail(@Part("email") email: String)
    // endregion

    @POST("auth/logout")
    suspend fun logout()

    // region Registration
    @FormUrlEncoded
    @POST("auth/onboarding/email")
    suspend fun setEmail(
        @Header("Authorization") token: String, @Field("email") email: String
    ): BaseResponse

    @POST("auth/onboarding/complete")
    suspend fun onboardingComplete(@Header("Authorization") token: String): BaseResponseNew<UserDTO>

    @POST("auth/register")
    suspend fun register(@Body registerBody: RequestBody): BaseResponseNew<UserDataResponse>
    // endregion

    // region Users

    // TODO GET      User List
    // TODO GET      Search User

    // TODO Fix me, add return value
    // TODO POST     Block a User
    // TODO DELETE   Unblock a User

    // endregion

    // region Verification

    // TODO POST     Verify Email
    // TODO POST     Verify Phone Number
    // TODO POST     Resend Verification via Email
    // TODO POST     Resend Verification via Phone Number

    // endregion

    @POST("auth/change/email/verify")
    suspend fun verifyChangeEmail(
        @Header("Verification-Token") verificationToken: String,
        @Body requestBody: RequestBody
    )

    @POST("auth/change/phone-number/verify")
    suspend fun verifyChangePhone(
        @Header("Verification-Token") verificationToken: String,
        @Body requestBody: RequestBody
    )

    @POST("auth/verification/resend")
    suspend fun resendVerificationCode(
        @Body registerBody: RequestBody
    ): BaseResponse

    @GET("auth/me")
    suspend fun getUser(): BaseResponseNew<UserDTO>

    @DELETE("auth/account")
    suspend fun deleteAccount(
        @Header("Verification-Token") verificationToken: String
    )

    @POST("auth/account/verification-token")
    suspend fun getVerificationToken(
        @Body requestBody: RequestBody? = null
    ): BaseResponseNew<GetVerificationTokenResponse>

    @POST("auth/change/password")
    suspend fun changePassword(
        @Body body: ChangePasswordDTO
    ): BaseResponse

    @POST("auth/change/email")
    suspend fun requestChangeEmail(
        @Header("Verification-Token") verificationToken: String? = null,
        @Body requestBody: RequestBody
    )

    @POST("auth/verification/verify")
    suspend fun verifyAccount(
        @Body registerBody: RequestBody
    ): BaseResponseNew<UserDTO>

    @POST("auth/change/phone-number")
    suspend fun requestChangePhone(
        @Header("Verification-Token") verificationToken: String? = null,
        @Body requestBody: RequestBody
    )

    @POST("auth/forgot-password")
    suspend fun requestResetPassword(
        @Body body: RequestResetPasswordDTO
    )

    @POST("auth/reset-password/check")
    suspend fun checkResetPassword(
        @Body body: CheckResetPasswordDTO
    )

    @POST("auth/reset-password")
    suspend fun resetPassword(
        @Body body: ResetPasswordDTO
    )
}
