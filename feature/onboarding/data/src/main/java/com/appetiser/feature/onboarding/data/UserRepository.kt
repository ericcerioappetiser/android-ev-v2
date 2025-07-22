package com.appetiser.feature.onboarding.data

import androidx.paging.PagingSource
import com.appetiser.core.domain.media.Media
import com.appetiser.core.domain.user.UpdateUserRequest
import com.appetiser.core.domain.user.User
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository {

    suspend fun getCurrentUser(): User

    fun getCurrentUserFlow(): Flow<User>

    suspend fun updateCurrentUser(
        user: UpdateUserRequest
    ): User

    suspend fun uploadUserAvatar(
        avatarFile: File
    ): Media

    suspend fun onBoardingCompleted(): User

    fun searchUsers(query: String): PagingSource<Int, User>
}
