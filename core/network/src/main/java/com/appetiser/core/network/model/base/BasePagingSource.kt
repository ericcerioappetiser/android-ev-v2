package com.appetiser.core.network.model.base

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.net.URL

@Serializable
data class BasePagedResponse<T>(
    @SerialName("data") val data: List<T>,
    @SerialName("meta") val meta: PagingMeta,
    @SerialName("links") val links: PagingLinks
) : BaseResponse() {

    fun <R> map(transform: (T) -> R): BasePagedResponse<R> = BasePagedResponse(
        data = data.map(transform),
        meta = meta,
        links = links
    )
}

@Serializable
data class PagingLinks(
    @SerialName("first") val firstLink: String?,
    @SerialName("last") val lastLink: String?,
    @SerialName("prev") val previousLink: String?,
    @SerialName("next") val nextLink: String?
) {

    val firstKey: Int? get() = firstLink?.getKey()
    val lastKey: Int? get() = lastLink?.getKey()
    val prevKey: Int? get() = previousLink?.getKey()
    val nextKey: Int? get() = nextLink?.getKey()

    private fun String.getKey(): Int? {
        val query = URL(this).query?.run {
            split("&")
                .associate {
                    val keyPair = it.split("=")
                    keyPair[0] to keyPair[1].toInt()
                }
        } ?: emptyMap()

        return query["page"]
    }
}

@Serializable
data class PagingMeta(
    @SerialName("current_page")
    val currentPage: Int = 1,
    @SerialName("from")
    val from: Int? = null,
    @SerialName("last_page")
    val lastPage: Int = 0,
    @SerialName("path")
    val path: String? = null,
    @SerialName("per_page")
    val perPage: Int? = null,
    @SerialName("to")
    val to: Int? = null,
    @SerialName("total")
    val total: Int? = null
)


private open class BasePagingSourceImpl<T : Any>(
    private val requestData: suspend (pagingOption: PagingOption) -> BasePagedResponse<T>,
    private val getRefreshKeyCallback: (state: PagingState<Int, T>) -> Int?
) : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val request = requestData(
                PagingOption(
                    count = params.loadSize,
                    page = params.key ?: 1 // First page
                )
            )

            LoadResult.Page(
                data = request.data,
                prevKey = request.links.prevKey,
                nextKey = request.links.nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return getRefreshKeyCallback.invoke(state)
    }
}


fun <T : Any> createPagingSource(
    requestData: suspend (pagingOption: PagingOption) -> BasePagedResponse<T>,
    getRefreshKeyCallback: (state: PagingState<Int, T>) -> Int? = { state ->
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
): PagingSource<Int, T> = BasePagingSourceImpl(requestData, getRefreshKeyCallback)

data class PagingOption(
    val count: Int,
    val page: Int
) : Map<String, Int> by mapOf("per_page" to count, "page" to page)
