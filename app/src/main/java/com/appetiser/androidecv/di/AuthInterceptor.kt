package com.appetiser.androidecv.di

import com.appetiser.core.network.SessionProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(private val sessionProvider: SessionProvider) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // Check if Authorization header is present, then we skip
        if (!chain.request().header("Authorization").isNullOrBlank()) {
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build()

            return chain.proceed(request)
        }

        // Check if the request URL matches the excluded pattern
        if (chain.request().url.toString().contains(EXCLUDED_URL_PATTERN, ignoreCase = true)) {
            return chain.proceed(chain.request())
        }

        var token: String

        runBlocking {
            token = sessionProvider.getAccessToken()
        }
        val request = chain.request()
        // Check if authorization header is needed for this request
        return if (shouldAddAuthorizationHeader(token, request)) {
            val modifiedRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(modifiedRequest)
        } else {
            val modifiedRequest = request.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(modifiedRequest)
        }
    }

    // Helper function to determine if the Authorization header is needed
    private fun shouldAddAuthorizationHeader(token: String, request: Request): Boolean =
        token.isNotBlank() && !isExcludedEndpoint(request.url.encodedPath)

    // Helper function to check if the current request is excluded from authorization
    private fun isExcludedEndpoint(path: String): Boolean {
        val excludedEndpoints = listOf(
            "auth/forgot-password",
            "auth/reset-password/check",
            "auth/reset-password"
        )
        return excludedEndpoints.any { path.contains(it) }
    }

    companion object {

        private const val EXCLUDED_URL_PATTERN = "amazonaws.com"
    }
}
