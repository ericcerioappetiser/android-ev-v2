package com.appetiser.androidecv

import androidx.lifecycle.viewModelScope
import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.domain.user.User
import com.appetiser.feature.auth.data.SessionRepository
import com.appetiser.feature.auth.data.features.session.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.yield
import timber.log.Timber
import javax.inject.Inject

enum class InitialNavigation {
    WALKTHROUGH,
    LOGIN,
    VERIFICATION,
    INPUT_NAME,
    INPUT_BIRTH_DATE,
    INPUT_PROFILE
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : BaseViewModel<MainViewModel.ViewState, MainViewModel.Action>() {

    data class ViewState(
        val isReady: Boolean,
        val initialNavigation: Consumable<InitialNavigation>?,
        val user: User? = null
    )

    sealed interface Action

    override val initialState: ViewState
        get() = ViewState(
            isReady = false,
            initialNavigation = null
        )

    override suspend fun onInitial() {
        viewModelScope.launchWithErrorHandling {
            sessionRepository
                .getSessionFlow()
                .distinctUntilChanged()
                .collect { session ->
                    if (session.user.isEmpty) {
                        modifyState {
                            copy(
                                initialNavigation = if (user == null) {
                                    InitialNavigation.WALKTHROUGH.asConsumable(::markAsReady)
                                } else {
                                    InitialNavigation.LOGIN.asConsumable(::markAsReady)
                                }
                            )
                        }
                    } else {
                        checkOnboardingStatus(session)
                    }

                    modifyState {
                        copy(user = session.user)
                    }
                }
        }
    }

    private suspend fun checkOnboardingStatus(session: Session) {
        val user = session.user
        if (user.onboardedAt != null) {
            modifyState {
                copy(isReady = true)
            }
            return
        }
        if (!user.verified && !session.hasSkippedEmailVerification) {
            modifyState {
                copy(
                    initialNavigation = InitialNavigation.VERIFICATION.asConsumable(::markAsReady),
                )
            }
            return
        }


        return when {
            !user.hasFullName -> {
                modifyState {
                    copy(
                        initialNavigation = InitialNavigation.INPUT_NAME.asConsumable(::markAsReady)
                    )
                }
            }

            !user.hasBirthdate -> {
                modifyState {
                    copy(
                        initialNavigation = InitialNavigation.INPUT_BIRTH_DATE.asConsumable(::markAsReady)
                    )
                }
            }
            !user.hasProfilePhoto -> {
                modifyState {
                    copy(
                        initialNavigation = InitialNavigation.INPUT_PROFILE.asConsumable(::markAsReady)
                    )
                }
            }

            else -> {
                modifyState {
                    copy(isReady = true)
                }
            }
        }
    }

    private fun markAsReady() {
        viewModelScope.launchWithErrorHandling {
            yield() // finish main thread queued tasks; this includes navigating
            modifyState { copy(isReady = true) }
        }
    }

    override suspend fun errorHandler(error: Throwable) {
        // TODO
        Timber.e(error)
    }
}
