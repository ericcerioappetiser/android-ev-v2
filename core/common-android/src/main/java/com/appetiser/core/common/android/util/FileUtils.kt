package com.appetiser.core.common.android.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import androidx.core.net.toUri
import coil3.SingletonImageLoader
import coil3.request.ImageRequest
import coil3.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.Locale
import kotlin.math.min
import kotlin.math.roundToInt

object FileUtils {

    // Function to create image URI (adjust path as needed)
    fun createImageUri(context: Context): Uri {
        val file =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo+${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    fun Context.getFileFromUri(uri: Uri): File? = try {
        // Create a temporary file in the cache directory
        val file = File(cacheDir, "${uri.lastPathSegment}")

        // Open InputStream from the content URI
        contentResolver.openInputStream(uri)?.use { inputStream ->
            // Copy the InputStream to the temp file
            file.outputStream().use { outputStream ->
                copyStream(inputStream, outputStream)
            }
        }
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    /**
     * Saves a bitmap to internal storage.
     *
     * @param context The context to use.
     * @param bitmap The bitmap to save.
     * @param filename The name of the file.
     * @param quality The quality of the saved bitmap.
     * @param recycle Whether to recycle the bitmap after saving.
     * @return The URI of the saved file.
     */
    fun saveBitmap(
        context: Context,
        bitmap: Bitmap,
        filename: String,
        quality: Int,
        recycle: Boolean
    ): Uri {
        val fileOutputStream: FileOutputStream
        val photo = File(context.filesDir.path, filename)
        val mimeType = getMimeType(context, Uri.parse(photo.absolutePath))
        Timber.d("mimeType: %s", mimeType)

        try {
            fileOutputStream = FileOutputStream(photo)

            val bos = BufferedOutputStream(fileOutputStream)
            var compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG

            if (mimeType.isNotBlank()) {
                val lowerMimeType = mimeType.lowercase(Locale.getDefault())

                if (lowerMimeType.contains("png")) {
                    compressFormat = Bitmap.CompressFormat.PNG
                } else if (lowerMimeType.contains("webp")) {
                    compressFormat = Bitmap.CompressFormat.WEBP
                }
            }

            bitmap.compress(compressFormat, quality, bos)
            Timber.d("photo saved: %s", photo.toString())

            try {
                bos.flush()
                bos.close()
                fileOutputStream.flush()
                fileOutputStream.close()
            } catch (e: IOException) {
                Timber.e(e, "Error in cleaning up streams: %s", filename)
            }
        } catch (e: IOException) {
            Timber.e(e, "IOException in saving bitmap: %s", filename)
        } catch (e: Exception) {
            Timber.e(e, "Error in saving bitmap: %s", filename)
        }

        if (recycle) {
            bitmap.recycle()
        }

        return Uri.parse("file:" + photo.absolutePath)
    }

    /**
     * Returns the MIME type for a given URI.
     *
     * @param context The context to use.
     * @param uri The URI to get the MIME type for.
     * @return The MIME type as a string.
     */
    fun getMimeType(
        context: Context,
        uri: Uri
    ): String {
        var mimeType: String?

        if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
            val cr = context.contentResolver
            mimeType = cr.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                fileExtension.lowercase(Locale.getDefault())
            )

            if (mimeType.isNullOrEmpty()) {
                mimeType = if ("json" == fileExtension) {
                    "application/json"
                } else {
                    "text/$fileExtension"
                }
            }
        }

        return mimeType ?: ""
    }

    /**
     * Scales down a photo to fit within the specified dimensions.
     *
     * @param photoPath The path of the photo to scale down.
     * @param context The context to use.
     * @param maxDimension The maximum dimension of the scaled down photo.
     * @param compressionQuality The quality of the scaled down photo.
     * @param filter Whether to use a bilinear filter for scaling.
     * @return The URI of the scaled down photo.
     */
    suspend fun scaleDownPhoto(
        photoPath: String,
        context: Context,
        maxDimension: Float,
        compressionQuality: Int,
        filter: Boolean
    ): Uri = withContext(Dispatchers.Default) {
        Timber.d("resizePhoto start --> path: %s", photoPath)

        val photoUri = photoPath.toUri()

        val result = SingletonImageLoader.get(context)
            .execute(
                ImageRequest.Builder(context)
                    .data(photoPath)
                    .build()
            )

        val bitmap = result.image?.toBitmap()

        if (bitmap == null) {
            Timber.d("cannot convert")
            return@withContext photoUri
        }

        if (bitmap.width < maxDimension && bitmap.height < maxDimension) {
            return@withContext photoUri
        } else {
            val ratio = min(maxDimension / bitmap.width, maxDimension / bitmap.height)
            val width = (ratio * bitmap.width).roundToInt()
            val height = (ratio * bitmap.height).roundToInt()
            val scaledDownBitmap = bitmap.scale(width, height, filter)
            val scaledDownUri = saveBitmap(
                context,
                scaledDownBitmap,
                photoUri.lastPathSegment!!,
                compressionQuality,
                true
            )

            Timber.d(
                "resizePhotos finish --> path: %s, scaledDown: %s",
                photoUri,
                scaledDownUri
            )

            return@withContext scaledDownUri
        }
    }

    private fun copyStream(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (input.read(buffer).also { bytesRead = it } != -1) {
            output.write(buffer, 0, bytesRead)
        }
    }
}
