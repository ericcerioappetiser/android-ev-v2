package com.appetiser.feature.report.data

import com.appetiser.core.domain.user.User


interface BlockRepository {

    suspend fun blockUser(userId: Long): Boolean

    suspend fun unblockUser(userId: Long): Boolean

    suspend fun getAllBlockedUsers(): List<User>
}
