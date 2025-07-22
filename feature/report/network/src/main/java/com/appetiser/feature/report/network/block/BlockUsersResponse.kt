package com.appetiser.feature.report.network.block

import com.appetiser.core.network.model.user.UserDTO


data class BlockUsersResponse(val id: Long, val type: String, val blocked: UserDTO)
