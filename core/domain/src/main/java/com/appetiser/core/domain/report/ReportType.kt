package com.appetiser.core.domain.report


// comments,jobs,posts,products,users
enum class ReportType(val type: String) {

    COMMENTS("comments"),
    POSTS("posts"),
    PRODUCTS("products"),
    USERS("users"),
    CHANNELS("channels"),
    ISSUES("issues");

    companion object {

        fun fromValue(value: String): ReportType? = entries.firstOrNull { type ->
            type.type.equals(value, ignoreCase = true)
        }
    }
}
