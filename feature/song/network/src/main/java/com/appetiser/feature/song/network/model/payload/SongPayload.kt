package com.appetiser.feature.song.network.model.payload

class SongPayload(
    private val search: String? = null,
    private val isFavorite: Int? = null,
    private val limit: Int? = null,
    private val page: Int? = null
) {
    fun toQueryMap(): Map<String, Any> {
        return buildMap {
            search?.let { put("search", it) }
            isFavorite?.let { put("filter[only_favorites]", it.toString()) }
            limit?.let { put("per_page", it) }
            page?.let { put("page", it) }
        }
    }
}
