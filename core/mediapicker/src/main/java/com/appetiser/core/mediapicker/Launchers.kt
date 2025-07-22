package com.appetiser.core.mediapicker

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.appetiser.core.mediapicker.models.MediaPickerCancelled

@Composable
fun rememberPickMultipleMediaLauncher(
    onResult: (Result<List<Uri>?>) -> Unit
) = rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
    onResult(if (uris.isNotEmpty()) Result.success(uris) else Result.failure(MediaPickerCancelled))
}

@Composable
fun rememberPickMediaLauncher(
    onResult: (Result<Uri?>) -> Unit
) = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
    onResult(uri?.let { Result.success(it) } ?: Result.failure(MediaPickerCancelled))
}

@Composable
fun rememberTakePictureLauncher(
    onResult: (Result<Uri?>) -> Unit,
    onError: (Throwable) -> Unit,
    mediaPickerHelper: MediaPickerHelper
) = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
    handleResult(
        success,
        onResult,
        onError
    ) { mediaPickerHelper.currentCapturedUri }
}

@Composable
fun rememberCaptureVideoLauncher(
    onResult: (Result<Uri?>) -> Unit,
    onError: (Throwable) -> Unit,
    mediaPickerHelper: MediaPickerHelper
) = rememberLauncherForActivityResult(ActivityResultContracts.CaptureVideo()) { success ->
    handleResult(
        success,
        onResult,
        onError
    ) { mediaPickerHelper.currentCapturedUri }
}

@Composable
fun mediaPickerLaunchers(
    onResult: (Result<Any?>) -> Unit, // Result Either List<URI> or URI
    onError: (Throwable) -> Unit,
    mediaPickerHelper: MediaPickerHelper
): MediaPickerLaunchers = MediaPickerLaunchers(
    captureVideoLauncher = rememberCaptureVideoLauncher(onResult, onError, mediaPickerHelper),
    pickMultipleMediaLauncher = rememberPickMultipleMediaLauncher(onResult),
    pickMediaLauncher = rememberPickMediaLauncher(onResult),
    takePictureLauncher = rememberTakePictureLauncher(onResult, onError, mediaPickerHelper),
    pickMultipleImageLauncher = rememberPickMultipleMediaLauncher(onResult),
    pickMultipleVideoLauncher = rememberPickMultipleMediaLauncher(onResult)
)

private fun handleResult(
    success: Boolean,
    onResult: (Result<Uri?>) -> Unit,
    onError: (Throwable) -> Unit,
    getFileUri: () -> Uri?
) {
    try {
        onResult(if (success) Result.success(getFileUri()) else Result.failure(MediaPickerCancelled))
    } catch (e: Exception) {
        onError(e)
    }
}
