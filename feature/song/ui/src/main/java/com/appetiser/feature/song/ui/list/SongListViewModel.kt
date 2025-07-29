package com.appetiser.feature.song.ui.list

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableError
import com.appetiser.core.common.android.util.launchWithTimber
import com.appetiser.feature.song.data.repository.SongRepository
import com.appetiser.feature.song.domain.models.Song
import com.appetiser.feature.song.network.model.payload.SongPayload
import com.appetiser.feature.song.domain.models.Constants.DEFAULT_LIMIT
import com.appetiser.feature.song.domain.models.Constants.MINIMUM_SEARCH_LENGTH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(
    private val songRepository: SongRepository
) : BaseViewModel<SongListViewModel.ViewState, SongListViewModel.ActionState>() {

    init {
        onAction {
            handleAction(it)
        }
    }

    override suspend fun onInitial() {
        fetchSongs()
    }
    override val initialState: ViewState
        get() = ViewState()

    sealed interface ActionState {
        data class ClickSongItem(val songId: Int) : ActionState
        data class OnSearch(val searchKey: String) : ActionState
        data class ToggleFavorite(val song: Song) : ActionState
    }

    sealed interface ErrorState {
        data class CommonError(val throwable: Throwable) : ErrorState
    }

    data class ViewState(
        val songs: Flow<PagingData<Song>> = flowOf(PagingData.empty()),
        val isLoading: Boolean = false,
        val updateSongs: Map<Int, Song>? = emptyMap(),
        val filter: SongPayload = SongPayload(),
        val error: ConsumableError? = null,
        val onNavigateTo: Consumable<NavigateDestination>? = null,
        val clickSongId: Int? = null,
    )

    private suspend fun handleAction(action: ActionState) {
        when (action) {
            is ActionState.ClickSongItem -> {
                modifyState { copy(clickSongId = action.songId) }
                modifyState { copy(onNavigateTo = NavigateDestination.SONG_DETAILS.asConsumable()) }
            }

            is ActionState.OnSearch -> setSearchKey(action.searchKey)

            is ActionState.ToggleFavorite -> setIsFavorite(action.song.isFavorite)
        }
    }

    private suspend fun setSearchKey(searchKey: String) {
        if (searchKey.length >= MINIMUM_SEARCH_LENGTH) {
            modifyState { copy(filter = filter.copy(search = searchKey)) }
            fetchSongs()
        }
    }

    private suspend fun setIsFavorite(isFavorites: Boolean) {
        modifyState { copy(filter = filter.copy(isFavorite = if (isFavorites) 1 else 0)) }
        fetchSongs()
    }

    private suspend fun setLimit(limit: Int) {
        modifyState { copy(filter = filter.copy(limit = limit)) }
        fetchSongs()
    }

    private suspend fun setPage(page: Int) {
        modifyState { copy(filter = filter.copy(page = page)) }
        fetchSongs()
    }

    private fun fetchSongs() {
        viewModelScope.launchWithTimber {
            val baseFlow = Pager(
                config = PagingConfig(
                    pageSize = DEFAULT_LIMIT,
                    prefetchDistance = 1,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    songRepository.getSongs(state.filter)
                }

            ).flow.cachedIn(viewModelScope)

            val mergedFlow = combine(baseFlow, stateFlow) { pagingData, viewState ->
                pagingData.map { original ->
                    viewState.updateSongs?.get(original.id) ?: original
                }
            }
            modifyState { copy(songs = mergedFlow) }
        }
    }

    override suspend fun errorHandler(error: Throwable) {
        modifyState { copy(error = ConsumableError(error)) }
    }

    enum class NavigateDestination {
        SONG_DETAILS
    }
}
