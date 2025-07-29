package com.appetiser.feature.song.data.utils

import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

@Composable
fun <T : Any> LazyPagingItems<T>.collectPagingState(): PagingState {
    val loadState = this.loadState
    val isRefreshing = loadState.refresh is LoadState.Loading ||
        loadState.append is LoadState.Loading ||
        loadState.prepend is LoadState.Loading

    val isEmpty = !isRefreshing && loadState.append.endOfPaginationReached && itemCount == 0

    val error = (loadState.refresh as? LoadState.Error)?.error
        ?: (loadState.append as? LoadState.Error)?.error
        ?: (loadState.prepend as? LoadState.Error)?.error

    return PagingState(
        isEmpty = isEmpty,
        itemCount = itemCount,
        isRefreshing = isRefreshing,
        error = error
    )
}

data class PagingState(
    val isEmpty: Boolean = false,
    val itemCount: Int = 0,
    val isRefreshing: Boolean = false,
    val error: Throwable? = null
)
