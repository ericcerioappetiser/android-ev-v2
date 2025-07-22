package com.appetiser.core.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.appetiser.core.domain.accesstoken.AccessToken
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = AccessTokenDB.TOKEN_TABLE_NAME)
data class AccessTokenDB(
    @PrimaryKey
    var token: String = "",
    var refresh: String = "",
    @ColumnInfo(name = "token_type")
    var tokenType: String = "",
    @ColumnInfo(name = "expires_in")
    var expiresIn: String = ""
) {
    companion object {
        const val TOKEN_TABLE_NAME = "token"

        fun toDomain(accessTokenDB: AccessTokenDB): AccessToken = with(accessTokenDB) {
            AccessToken(
                token = token,
                refresh = "",
                tokenType = tokenType,
                expiresIn = expiresIn
            )
        }

        fun fromDomain(accessToken: AccessToken): AccessTokenDB = with(accessToken) {
            AccessTokenDB(
                token = token,
                refresh = refresh,
                tokenType = tokenType,
                expiresIn = expiresIn
            )
        }
    }

    constructor() : this("", "", "", "")

    val bearerToken: String get() = "Bearer $token"
}
