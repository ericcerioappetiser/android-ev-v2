package com.appetiser.core.network

/**
 * Interface for providing session-related information such as user ID and access token.
 */
interface SessionProvider {
    /**
     * Retrieves the user ID associated with the current session.
     *
     * @return The user ID as a [Long].
     */
    suspend fun getUserId(): Long

    /**
     * Retrieves the access token associated with the current session.
     *
     * @return The access token as a [String].
     */
    suspend fun getAccessToken(): String
}
