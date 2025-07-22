package com.appetiser.core.mediapicker
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.core.content.FileProvider
import com.appetiser.core.mediapicker.models.MediaPickerCameraPermissionError
import com.appetiser.core.mediapicker.models.MediaPickerOptions
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MediaPickerHelper(
    private val context: Context,
    private val options: MediaPickerOptions
) {

    var currentCapturedUri: Uri? = null

    private fun generateFileName(context: Context, uri: Uri, isImage: Boolean = true): String {
        val now = Instant.now()
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))

        val mimeType = context.contentResolver.getType(uri)
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: if (isImage) "jpg" else "mp4"

        return if (isImage) "CAPTURE_IMG_${now}.$extension" else "CAPTURE_VID_${now}.$extension"
    }

    private fun getUriForNewFile(isImage: Boolean = true): Uri {
        val fileName = generateFileName(isImage = isImage, context = context, uri = Uri.EMPTY)
        val file = File(context.filesDir, fileName).apply { createNewFile() }
        return FileProvider.getUriForFile(context, options.authority, file).also {
            currentCapturedUri = it
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun handleCameraActionWithPermission(
        isImage: Boolean,
        cameraPermissionState: PermissionState,
        launcher: ActivityResultLauncher<Uri>,
        onError: (Throwable) -> Unit,
    ) {
        if (cameraPermissionState.status.isGranted) {
            val uri = getUriForNewFile(isImage)
            launcher.launch(uri)
        } else {
            onError(MediaPickerCameraPermissionError)
            cameraPermissionState.launchPermissionRequest()
        }
    }
}

data class MediaPickerLaunchers(
    val pickMediaLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    val pickMultipleMediaLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, List<Uri>>,
    val pickMultipleImageLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, List<Uri>>,
    val pickMultipleVideoLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, List<Uri>>,
    val takePictureLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    val captureVideoLauncher: ManagedActivityResultLauncher<Uri, Boolean>
)
