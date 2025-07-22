package com.appetiser.core.user.local.feature

import com.appetiser.core.domain.user.User
import kotlinx.coroutines.flow.Flow

interface UserLocalSource {

    suspend fun saveUser(user: User): User

    suspend fun getUser(): User

    fun getUserFlow(): Flow<User>

    suspend fun deleteUser()
}
