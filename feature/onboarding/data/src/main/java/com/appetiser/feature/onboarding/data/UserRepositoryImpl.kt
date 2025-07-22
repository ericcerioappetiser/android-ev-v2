package com.appetiser.feature.onboarding.data

import androidx.paging.PagingSource
import com.appetiser.core.domain.media.Media
import com.appetiser.core.domain.user.UpdateUserRequest
import com.appetiser.core.domain.user.User
import com.appetiser.core.network.model.base.createPagingSource
import com.appetiser.core.network.model.user.toRequestDTO
import com.appetiser.core.user.local.feature.UserLocalSource
import com.appetiser.core.user.network.feature.UserRemoteSource
import java.io.File
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val remoteSource: UserRemoteSource,
    private val localSource: UserLocalSource
) : UserRepository {

    override suspend fun getCurrentUser(): User {
        val localUser = localSource.getUser()
        return if (localUser.isEmpty) {
            val remoteUser = remoteSource.getCurrentUser()
            localSource.saveUser(remoteUser)
            remoteUser
        } else {
            localUser
        }
    }

    override fun getCurrentUserFlow() = localSource.getUserFlow()

    override suspend fun updateCurrentUser(user: UpdateUserRequest): User {
        val updatedUser = remoteSource.updateCurrentUser(user.toRequestDTO())
        localSource.saveUser(updatedUser)
        return updatedUser
    }

    override suspend fun uploadUserAvatar(avatarFile: File): Media {
        val user = localSource.getUser()
        val media = remoteSource.uploadUserAvatar(
            userId = user.id,
            avatarFile = avatarFile
        )
        localSource.saveUser(
            user.copy(
                avatar = media
            )
        )
        return media
    }

    override suspend fun onBoardingCompleted(): User {
        val currentUser = remoteSource.onBoardingCompleted()
        return localSource.saveUser(currentUser)
    }

    override fun searchUsers(query: String): PagingSource<Int, User> {
        return createPagingSource(
            { pagingOption ->
                remoteSource.searchUser(pagingOption = pagingOption, query = query)
                    .map { dto ->
                        dto.toDomain()
                    }
            }
        )
    }
}
