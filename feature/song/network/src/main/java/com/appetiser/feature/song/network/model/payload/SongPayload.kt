package com.appetiser.feature.song.network.model.payload

import com.appetiser.feature.song.domain.models.Constants.DEFAULT_LIMIT
import com.appetiser.feature.song.domain.models.Constants.DEFAULT_PAGE

data class SongPayload(
    private val search: String = "star",
    private val isFavorite: Int? = 1,
    private val limit: Int? = DEFAULT_LIMIT,
    private val page: Int? = DEFAULT_PAGE
) {
    fun toQueryMap(): Map<String, Any> {
        return buildMap {
            search?.let { put("search", it) }
            isFavorite?.let { put("filter%5Bonly_favorites%5D", it) }
            limit?.let { put("per_page", it) }
            page?.let { put("page", it) }
        }
    }
}
