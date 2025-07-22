package com.appetiser.feature.report.network

import com.appetiser.core.domain.user.User

interface BlockRemoteSource {
    suspend fun blockUser(
        userId: Long
    ): Boolean

    suspend fun unblockUser(
        userId: Long
    ): Boolean

    suspend fun getAllBlockedUsers(): List<User>
}
