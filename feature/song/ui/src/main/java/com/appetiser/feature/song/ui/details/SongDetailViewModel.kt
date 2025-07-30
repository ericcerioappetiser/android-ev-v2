package com.appetiser.feature.song.ui.details

import androidx.lifecycle.SavedStateHandle
import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.BaseViewModel.Consumable
import com.appetiser.core.common.android.base.ConsumableError
import com.appetiser.feature.song.data.repository.SongRepository
import com.appetiser.feature.song.domain.models.Song
import com.appetiser.feature.song.ui.list.SongListViewModel.NavigateDestination
import com.appetiser.feature.song.ui.list.SongListViewModel.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SongDetailViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<SongDetailViewModel.ViewState, SongDetailViewModel.ActionState>() {

    val songId = savedStateHandle.get<Int>("id")

    init {
        onAction {
            handleAction(it)
        }
    }
    override suspend fun onInitial() {
        fetchSong()
    }

    private suspend fun fetchSong() {
        songId?.let {
            val song = songRepository.getSongDetails(it.toLong())
            modifyState { copy(song = song) }
        }
    }

    override suspend fun errorHandler(error: Throwable) {
        modifyState { copy(error = ConsumableError(error)) }
    }

    override val initialState: SongDetailViewModel.ViewState
        get() = ViewState()

    sealed interface ActionState {
        data class ClickSongItem(val songId: Int) : ActionState
    }

    data class ViewState(
        val song: Song? = null,
        val isLoading: Boolean = false,
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
        }
    }
}
