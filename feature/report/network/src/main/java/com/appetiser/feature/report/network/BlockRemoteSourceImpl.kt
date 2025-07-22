package com.appetiser.feature.report.network


import com.appetiser.core.domain.user.User
import javax.inject.Inject

class BlockRemoteSourceImpl @Inject constructor(
    private val apiService: BlockApiServices
) : BlockRemoteSource {

    override suspend fun blockUser(userId: Long): Boolean = apiService.blockUser(
        userId = userId
    ).isSuccessful()

    override suspend fun unblockUser(userId: Long): Boolean = apiService.unblockUser(
        userId = userId
    ).isSuccessful()

    override suspend fun getAllBlockedUsers(): List<User> = apiService.getAllBlockedUsers().data.map {
        it.blocked.toDomain()
    }
}
