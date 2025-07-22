package com.appetiser.core.user.local.feature

import com.appetiser.core.database.AppDatabase
import com.appetiser.core.database.models.UserDB
import com.appetiser.core.domain.user.User
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserLocalSourceImpl @Inject constructor(private val userDatabase: AppDatabase) : UserLocalSource {

    override suspend fun saveUser(user: User): User {
        userDatabase.userDao().insert(UserDB.toDB(user))
        return user
    }

    override suspend fun getUser(): User {
        val userDb = userDatabase.userDao().getUser() ?: UserDB.empty()
        return userDb.toDomain()
    }

    override fun getUserFlow() =
        userDatabase
            .userDao()
            .getUserFlow()
            .map {
                val user = it ?: UserDB.empty()
                user.toDomain()
            }

    override suspend fun deleteUser() {
        userDatabase.userDao().deleteUser()
    }
}
