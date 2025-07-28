package com.appetiser.feature.song.ui.list

import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableError
import com.appetiser.feature.song.data.repository.SongRepository
import com.appetiser.feature.song.domain.models.Song
import com.appetiser.feature.song.network.model.payload.SongPayload
import com.appetiser.feature.song.ui.Constants.MINIMUM_SEARCH_LENGTH
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
class SongListViewModel @Inject constructor(
    private val songRepository: SongRepository
) : BaseViewModel<SongListViewModel.SongListViewState, SongListViewModel.ActionState>() {

    init {
        onAction {
            handleAction(it)
        }
    }

    override suspend fun onInitial() {
        fetchSongs()
    }
    override val initialState: SongListViewState
        get() = SongListViewState()

    sealed interface ActionState {
        data class ClickSongItem(val id: String) : ActionState
        data class OnSearch(val searchKey: String) : ActionState
        data class ToggleFavorite(val song: Song) : ActionState
    }

    sealed interface ErrorState {
        data class CommonError(val throwable: Throwable) : ErrorState
    }

    data class SongListViewState(
        val isLoading: Boolean = false,
        val songs: List<Song> = emptyList(),
        val filter: SongPayload = SongPayload(),
        val error: ConsumableError? = null,
        val onNavigateTo: Consumable<NavigateDestination>? = null,
        val clickSongItem: String? = null,
    )

    private suspend fun handleAction(action: ActionState) {
        when (action) {
            is ActionState.ClickSongItem -> {
                modifyState { copy(clickSongItem = action.id) }
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

    private suspend fun fetchSongs() {
        val songs = songRepository.getSongs(state.filter)
        modifyState { copy(songs = songs) }
    }

    override suspend fun errorHandler(error: Throwable) {
        modifyState { copy(error = ConsumableError(error)) }
    }

    enum class NavigateDestination {
        SONG_DETAILS
    }
}
