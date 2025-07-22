package com.appetiser.core.network.model.user

import com.appetiser.core.domain.accesstoken.AccessToken
import com.appetiser.core.domain.user.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class UserDataResponse(
    @SerialName("user")
    val user: UserDTO,
    @SerialName("access_token")
    val accessToken: String = "",
    @SerialName("token_type")
    val tokenType: String? = "",
    @SerialName("expires_in")
    val expiresIn: String? = ""
) {

    fun toUserAndAccessTokenPair(): Pair<User, AccessToken> = Pair(
        user.toDomain(),
        AccessToken(
            token = accessToken,
            refresh = "",
            tokenType = tokenType.orEmpty(),
            expiresIn = expiresIn.orEmpty()
        )
    )
}
