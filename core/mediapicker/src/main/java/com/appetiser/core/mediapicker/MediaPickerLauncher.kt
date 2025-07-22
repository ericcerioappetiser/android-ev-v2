package com.appetiser.core.mediapicker

import android.Manifest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberMediaPickerLaunchers(
    mediaPickerHelper: MediaPickerHelper,
    onResult: (Result<Any?>) -> Unit,
    onError: (Throwable) -> Unit,
    onGranted: () -> Unit,
    onDenied: @Composable (Boolean) -> Unit

): MediaPickerActions {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    val launcher = mediaPickerLaunchers(
        onError = onError,
        onResult = onResult,
        mediaPickerHelper = mediaPickerHelper
    )

    val pickMediaAction: () -> Unit = {
        launcher.pickMediaLauncher.launch(imageOnlyRequest())
    }

    val pickMultipleMediaAction: () -> Unit = {
        launcher.pickMultipleMediaLauncher.launch(imageAndVideoRequest())
    }

    val pickMultipleImageAction: () -> Unit = {
        launcher.pickMultipleMediaLauncher.launch(imageOnlyRequest())
    }

    val pickMultipleVideoAction: () -> Unit = {
        launcher.pickMultipleMediaLauncher.launch(videoOnlyRequest())
    }

    val takePictureAction: () -> Unit = {
        mediaPickerHelper.handleCameraActionWithPermission(
            true,
            cameraPermissionState,
            launcher.takePictureLauncher,
            onError
        )
    }

    val captureVideoAction: () -> Unit = {
        mediaPickerHelper.handleCameraActionWithPermission(
            false,
            cameraPermissionState,
            launcher.captureVideoLauncher,
            onError
        )
    }

    when (cameraPermissionState.status) {
        is PermissionStatus.Denied -> onDenied(cameraPermissionState.status.shouldShowRationale)
        PermissionStatus.Granted -> onGranted()
    }

    return MediaPickerActions(
        pickMediaAction = pickMediaAction,
        pickMultipleMediaAction = pickMultipleMediaAction,
        takePictureAction = takePictureAction,
        captureVideoAction = captureVideoAction,
        pickMultipleImageAction = pickMultipleImageAction,
        pickMultipleVideoAction = pickMultipleVideoAction,
    )
}

data class MediaPickerActions constructor(
    val pickMediaAction: () -> Unit,
    val pickMultipleMediaAction: () -> Unit,
    val takePictureAction: () -> Unit,
    val captureVideoAction: () -> Unit,
    val pickMultipleImageAction: () -> Unit,
    val pickMultipleVideoAction: () -> Unit,
)

private fun imageOnlyRequest() = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
private fun videoOnlyRequest() = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
private fun imageAndVideoRequest() = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)









