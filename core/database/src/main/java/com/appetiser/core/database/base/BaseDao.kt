package com.appetiser.core.database.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Update

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg obj: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: MutableList<T>): List<Long>

    @Update
    suspend fun update(obj: T)

    @Transaction
    suspend fun insertOrUpdate(obj: T) {
        val id = insert(obj)
        if (id == -1L) update(obj)
    }

    @Delete
    suspend fun delete(obj: T)
}
