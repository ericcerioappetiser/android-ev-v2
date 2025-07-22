package com.appetiser.core.common.android.util

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Patterns
import androidx.annotation.ArrayRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.IntegerRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import com.appetiser.core.common.android.PHOTO_COMPRESSION_QUALITY
import com.appetiser.core.common.android.PHOTO_MAX_DIMENSION
import com.appetiser.core.common.android.util.dispatchers.DispatcherProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

/**
 * Holder for android related calls so view models would still be testable.
 */
class ResourceManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatcherProvider: DispatcherProvider
) {
    @ColorInt
    fun getColor(@ColorRes resId: Int): Int = context.getColor(resId)

    fun getString(@StringRes resId: Int): String = context.getString(resId)

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String = context.getString(resId, *formatArgs)

    fun getDimen(@DimenRes resId: Int) = context.resources.getDimension(resId)

    fun getInteger(@IntegerRes resId: Int) = context.resources.getInteger(resId)

    fun getAndroidPackageName() = context.applicationContext.packageName

    @SuppressLint("HardwareIds")
    fun getDeviceId(): String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    fun getStringArray(@ArrayRes resId: Int): Array<String> = context.resources.getStringArray(resId)

    fun validateEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun getQuantityString(@PluralsRes resId: Int, quantity: Int, vararg formatArgs: Any): String = context.resources.getQuantityString(resId, quantity, *formatArgs)

    /**
     * Returns Pair of scaled down image path and path of original image.
     *
     * @param imagePath path of image to scale down
     */
    suspend fun scaleDownImage(imagePath: String): Pair<String, String> = if (imagePath.isBlank()) {
        "" to imagePath
    } else {
        withContext(dispatcherProvider.default) {
            val scaledDownImageUri = try {
                FileUtils.scaleDownPhoto(
                    "file:$imagePath",
                    context,
                    PHOTO_MAX_DIMENSION,
                    PHOTO_COMPRESSION_QUALITY,
                    false
                ).path!!
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                ""
            }
            scaledDownImageUri to imagePath
        }
    }

    /**
     * Returns scaled down image if scaling down succeeds. Else, returns original image.
     *
     * @param imagePath path of image to scale down
     */
    suspend fun scaleDownImageToFile(imagePath: String): File {
        require(imagePath.isNotBlank()) {
            "imagePath should not be blank"
        }
        return withContext(dispatcherProvider.default) {
            try {
                val path = FileUtils.scaleDownPhoto(
                    "file:$imagePath",
                    context,
                    PHOTO_MAX_DIMENSION,
                    PHOTO_COMPRESSION_QUALITY,
                    false
                ).path!!
                File(path)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                File(imagePath)
            }
        }
    }

    /**
     * Returns device's country name code (i.e. "ph", "au", "cn").
     */
    fun getDeviceCountryNameCode(): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.networkCountryIso
    }
}
