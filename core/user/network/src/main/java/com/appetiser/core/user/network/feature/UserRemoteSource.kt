package com.appetiser.core.user.network.feature

import com.appetiser.core.domain.media.Media
import com.appetiser.core.domain.user.User
import com.appetiser.core.network.model.base.BasePagedResponse
import com.appetiser.core.network.model.base.PagingOption
import com.appetiser.core.network.model.user.UpdateUserRequestDTO
import com.appetiser.core.network.model.user.UserDTO
import java.io.File

interface UserRemoteSource {

    suspend fun getCurrentUser(): User

    suspend fun updateCurrentUser(
        user: UpdateUserRequestDTO
    ): User


    suspend fun uploadUserAvatar(
        userId: Int,
        avatarFile: File
    ): Media

    suspend fun onBoardingCompleted(): User
    suspend fun searchUser(query: String, pagingOption: PagingOption): BasePagedResponse<UserDTO>
}
