package com.appetiser.core.mediapicker.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Size
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.FileProvider
import com.yalantis.ucrop.UCrop
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UCropContract(
    private val createImageFilename: () -> String = {
        "IMG_${LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)}.jpg"
    }
) : ActivityResultContract<UCropContract.Options, Result<Uri?>>() {

    companion object {

        const val TEMP = "TEMP_"
        const val CHILD = "images"
        const val CANNOT_CREATE_TEMP_FILE_ERROR = "Cannot create temp file"
        const val FILE_PROVIDER = ".fileprovider"
        private const val ERROR_NULL_MESSAGE = "Error is null"
        private const val OUTPUT_URI_NULL_MESSAGE = "Output URI is null"
    }

    data class Options(
        val source: Uri,
        val target: Uri? = null,
        val aspectRatio: Pair<Float, Float>,
        val maxResultSize: Size?
    )

    override fun createIntent(context: Context, input: Options): Intent {
        return UCrop.of(input.source, input.target ?: createTempFileUri(context))
            .withAspectRatio(input.aspectRatio.first, input.aspectRatio.second)
            .run {
                if (input.maxResultSize != null) {
                    withMaxResultSize(input.maxResultSize.width, input.maxResultSize.height)
                } else {
                    this
                }
            }
            .getIntent(context)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Result<Uri?> {
        return when (resultCode) {
            Activity.RESULT_OK -> {
                val outputUri = intent?.let { UCrop.getOutput(it) }
                if (outputUri != null) {
                    Result.success(outputUri)
                } else {
                    Result.failure(IllegalStateException(OUTPUT_URI_NULL_MESSAGE))
                }
            }

            Activity.RESULT_CANCELED -> {
                Result.success(null)
            }

            else -> {
                val error = intent?.let { UCrop.getError(it) }
                if (error != null) {
                    Result.failure(error)
                } else {
                    Result.failure(IllegalStateException(ERROR_NULL_MESSAGE))
                }
            }
        }
    }

    private fun createTempFileUri(
        context: Context,
        forFile: String = createImageFilename()
    ): Uri {
        val tempDir = File(context.cacheDir, CHILD)
        tempDir.mkdirs()

        val tempPath = tempDir.resolve("$TEMP$forFile")

        val uri = FileProvider.getUriForFile(
            context,
            "${context.applicationContext.packageName}$FILE_PROVIDER",
            tempPath
        )
        return checkNotNull(uri) { CANNOT_CREATE_TEMP_FILE_ERROR }
    }
}
