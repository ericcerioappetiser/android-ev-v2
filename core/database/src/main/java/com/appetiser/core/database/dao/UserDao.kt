package com.appetiser.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.appetiser.core.database.base.BaseDao
import com.appetiser.core.database.models.UserDB
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao : BaseDao<UserDB> {

    @Query("SELECT * FROM ${UserDB.USER_TABLE} LIMIT 1")
    suspend fun getUser(): UserDB?

    @Query("SELECT * FROM ${UserDB.USER_TABLE} LIMIT 1")
    fun getUserFlow(): Flow<UserDB?>

    @Query("DELETE FROM ${UserDB.USER_TABLE}")
    suspend fun deleteUser()
}
