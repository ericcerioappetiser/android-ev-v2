package com.appetiser.feature.report.ui

import androidx.lifecycle.viewModelScope
import com.appetiser.core.common.android.base.BaseViewModel
import com.appetiser.core.common.android.base.ConsumableError
import com.appetiser.core.common.android.util.launchWithTimber
import com.appetiser.core.domain.report.UserReportRequest
import com.appetiser.feature.report.data.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ReportAProblemViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
) : BaseViewModel<ReportAProblemViewModel.ReportAProblemViewState, ReportAProblemViewModel.ActionState>() {


    init {
        onAction { handleAction(it) }
    }

    override val initialState: ReportAProblemViewState
        get() = ReportAProblemViewState()

    override suspend fun errorHandler(error: Throwable) {
        Timber.e(error)
        modifyState { copy(error = error.asConsumable()) }
    }

    private suspend fun handleAction(action: ActionState) {
        when (action) {
            ActionState.OnReportAProblem -> submitReport()

            is ActionState.OnSubjectChange -> modifyState {
                copy(subject = action.value)
            }

            is ActionState.OnDescriptionChange -> modifyState { copy(description = action.value) }
            is ActionState.OnAddImage -> modifyState { copy(images = images + action.imagePath) }
            is ActionState.OnRemoveImage -> modifyState {
                copy(images = images.filter { it != action.imagePath })
            }
        }
    }

    private fun submitReport() {
        val currentState = state
        viewModelScope.launchWithTimber {
            modifyState { copy(isLoading = true) }
            runCatching {
                reportRepository.submitUserReportedIssue(
                    currentState.userReportRequest
                )
            }.onSuccess {
                modifyState { copy(successReport = it.asConsumable()) }
            }.onFailure {
                errorHandler(it)
            }
            modifyState { copy(isLoading = false) }
        }
    }

    data class ReportAProblemViewState(
        val isLoading: Boolean = false,
        val description: String = "",
        val subject: String = "",
        val error: ConsumableError? = null,
        val showImagePickerBottomSheet: Boolean = false,
        val images: List<String> = emptyList(),
        val successReport: Consumable<Boolean>? = null,
    ) {

        val userReportRequest: UserReportRequest
            get() = UserReportRequest(
                subjectText = subject,
                description = description,
                photos = images
            )
        val isContinueButton: Boolean
            get() = subject.isNotBlank() && description.isNotBlank()
    }

    sealed interface ActionState {
        data class OnSubjectChange(val value: String) : ActionState
        data class OnDescriptionChange(val value: String) : ActionState
        data object OnReportAProblem : ActionState
        data class OnAddImage(val imagePath: String) : ActionState
        data class OnRemoveImage(val imagePath: String) : ActionState
    }
}
