package com.appetiser.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appetiser.core.database.models.AccessTokenDB
import kotlinx.coroutines.flow.Flow

@Dao
interface TokenDao {

    @Query("SELECT * FROM ${AccessTokenDB.TOKEN_TABLE_NAME} LIMIT 1")
    suspend fun getToken(): AccessTokenDB?

    @Query("SELECT * FROM ${AccessTokenDB.TOKEN_TABLE_NAME} LIMIT 1")
    fun getTokenFlow(): Flow<AccessTokenDB?>

    @Query("DELETE FROM ${AccessTokenDB.TOKEN_TABLE_NAME}")
    suspend fun logoutToken()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveToken(tokenDb: AccessTokenDB): Long
}
