package com.appetiser.core.user.network.feature

import com.appetiser.core.domain.media.Media
import com.appetiser.core.domain.user.User
import com.appetiser.core.network.model.base.BasePagedResponse
import com.appetiser.core.network.model.base.PagingOption
import com.appetiser.core.network.model.user.UpdateUserRequestDTO
import com.appetiser.core.network.model.user.UserDTO
import com.appetiser.core.user.network.OnboardingApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UserRemoteSourceImpl @Inject constructor(
    private val apiService: OnboardingApiService,
    private val json: Json
) : UserRemoteSource {

    override suspend fun getCurrentUser(): User {
        val response = apiService.getCurrentUser()
        return response.data.toDomain()
    }

    override suspend fun updateCurrentUser(user: UpdateUserRequestDTO): User {
        val response = apiService.updateCurrentUser(body = user)
        return response.data.toDomain()
    }

    override suspend fun uploadUserAvatar(userId: Int, avatarFile: File): Media {
        val body =
            MultipartBody.Part
                .createFormData(
                    "avatar",
                    avatarFile.name,
                    avatarFile.asRequestBody("image/*".toMediaTypeOrNull())
                )
        val response = apiService.uploadUserAvatar(id = userId, body = body)
        return response.data.toDomain()
    }

    override suspend fun onBoardingCompleted(): User = apiService.onboardingComplete().data.toDomain()

    override suspend fun searchUser(query: String, pagingOption: PagingOption): BasePagedResponse<UserDTO> =
        apiService.searchUser(query, pagingOption = pagingOption)
}
