package com.appetiser.core.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.appetiser.core.domain.user.User
import com.appetiser.core.domain.user.UsernameType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = UserDB.USER_TABLE)
data class UserDB(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "birthdate") val birthdate: LocalDate? = null,
    @ColumnInfo(name = "gender") val gender: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "phone_number") val phoneNumber: String,
    @ColumnInfo(name = "created_at") val createdAt: Instant?,
    @ColumnInfo(name = "updated_at") val updatedAt: Instant?,
    @ColumnInfo(name = "blocked_at") val blockedAt: Instant?,
    @ColumnInfo(name = "onboarded_at") val onboardedAt: Instant?,
    @ColumnInfo(name = "primary_username") val primaryUsername: UsernameType,
    @ColumnInfo(name = "email_verified") val emailVerified: Boolean = false,
    @ColumnInfo(name = "phone_number_verified") val phoneNumberVerified: Boolean = false,
    @ColumnInfo(name = "verified") val verified: Boolean = false,
    @ColumnInfo(name = "avatar_permanent_url") val avatarPermanentUrl: String,
    @ColumnInfo(name = "avatar_permanent_thumb_url") val avatarPermanentThumbUrl: String,
    @ColumnInfo(name = "mine") val mine: Boolean = false,
    @ColumnInfo(name = "avatar") val avatar: MediaDB,
    @ColumnInfo(name = "stripe_customer_id") val stripeCustomerId: String
) {

    companion object {

        const val USER_TABLE = "user_table"
        private const val EMPTY_USER_ID = 0
        private const val EMPTY_USER_FULL_NAME = "empty"

        fun empty(): UserDB = UserDB(
            id = EMPTY_USER_ID,
            fullName = EMPTY_USER_FULL_NAME,
            firstName = "",
            lastName = "",
            description = "",
            birthdate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
            gender = "",
            email = "",
            phoneNumber = "",
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now(),
            blockedAt = Clock.System.now(),
            onboardedAt = null,
            primaryUsername = UsernameType.UNKNOWN,
            emailVerified = false,
            phoneNumberVerified = false,
            verified = false,
            avatarPermanentUrl = "",
            avatarPermanentThumbUrl = "",
            mine = false,
            avatar = MediaDB.empty(),
            stripeCustomerId = ""
        )

        fun toDB(user: User): UserDB = UserDB(
            id = user.id,
            fullName = user.fullName,
            firstName = user.firstName,
            lastName = user.lastName,
            description = user.description,
            birthdate = user.birthdate,
            gender = user.gender,
            email = user.email,
            phoneNumber = user.phoneNumber,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt,
            blockedAt = user.blockedAt,
            onboardedAt = user.onboardedAt,
            primaryUsername = user.primaryUsername,
            emailVerified = user.emailVerified,
            phoneNumberVerified = user.phoneNumberVerified,
            verified = user.verified,
            avatarPermanentUrl = user.avatarPermanentUrl,
            avatarPermanentThumbUrl = user.avatarPermanentThumbUrl,
            mine = user.mine,
            avatar = MediaDB.toDB(user.avatar), // Assuming MediaDB also has a toDomain() method
            stripeCustomerId = user.stripeCustomerId.orEmpty()
        )
    }

    fun toDomain(): User = User(
        id = id,
        fullName = fullName,
        firstName = firstName,
        lastName = lastName,
        description = description,
        birthdate = birthdate,
        gender = gender,
        email = email,
        phoneNumber = phoneNumber,
        createdAt = createdAt,
        updatedAt = updatedAt,
        blockedAt = blockedAt,
        onboardedAt = onboardedAt,
        primaryUsername = primaryUsername,
        emailVerified = emailVerified,
        phoneNumberVerified = phoneNumberVerified,
        verified = verified,
        avatarPermanentUrl = avatarPermanentUrl,
        avatarPermanentThumbUrl = avatarPermanentThumbUrl,
        mine = mine,
        avatar = avatar.toDomain(), // Assuming MediaDB also has a toDomain() method
        stripeCustomerId = stripeCustomerId,
    )
}
