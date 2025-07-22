package com.appetiser.feature.report.data

import com.appetiser.core.domain.user.User
import com.appetiser.feature.report.network.BlockRemoteSource
import javax.inject.Inject

class BlockRepositoryImpl @Inject constructor(
    private val blockRemoteSource: BlockRemoteSource,
) : BlockRepository {

    override suspend fun blockUser(userId: Long): Boolean = blockRemoteSource
        .blockUser(
            userId = userId
        )

    override suspend fun unblockUser(userId: Long): Boolean = blockRemoteSource
        .unblockUser(
            userId = userId
        )

    override suspend fun getAllBlockedUsers(): List<User> = blockRemoteSource
        .getAllBlockedUsers()
}
