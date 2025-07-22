package com.appetiser.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.appetiser.core.database.dao.ReportDao
import com.appetiser.core.database.dao.TokenDao
import com.appetiser.core.database.dao.UserDao
import com.appetiser.core.database.models.AccessTokenDB
import com.appetiser.core.database.models.ReportTypeDB
import com.appetiser.core.database.models.UserDB
import com.appetiser.core.database.utils.DatabaseHelper
import com.appetiser.core.database.utils.UserTypeConverters


@Database(entities = [UserDB::class, AccessTokenDB::class, ReportTypeDB::class], version = 1, exportSchema = true)
@TypeConverters(UserTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun tokenDao(): TokenDao
    abstract fun reportDao(): ReportDao

    companion object {

        private const val DATABASE_NAME = "baseplate.db"

        // We handle singleton implementation here via dagger, instead of manually handling it.
        fun createInstance(helper: DatabaseHelper, userTypeConverters: UserTypeConverters): AppDatabase =
            buildDatabase(helper, userTypeConverters)

        private fun buildDatabase(helper: DatabaseHelper, userTypeConverters: UserTypeConverters): AppDatabase =
            helper.createDatabase(
                name = DATABASE_NAME,
                kClass = AppDatabase::class
            ) {
                addTypeConverter(userTypeConverters)
            }
    }
}
