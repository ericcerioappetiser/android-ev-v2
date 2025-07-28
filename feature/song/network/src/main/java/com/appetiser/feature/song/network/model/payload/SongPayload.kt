package com.appetiser.feature.song.network.model.payload

data class SongPayload(
    private val search: String = "star",
    private val isFavorite: Int? = 1,
    private val limit: Int? = 50,
    private val page: Int? = 1
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
