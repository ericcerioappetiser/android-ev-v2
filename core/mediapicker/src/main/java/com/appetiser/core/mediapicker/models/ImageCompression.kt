package com.appetiser.core.mediapicker.models

import android.graphics.Bitmap

/**
 * @property quality from 0 to 100
 * @property maxFileSize in bytes
 */
data class ImageCompression(
    val width: Int = DEFAULT_WIDTH,
    val height: Int = DEFAULT_HEIGHT,
    val quality: Int = DEFAULT_QUALITY,
    val format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
    val maxFileSize: Long? = null
) {

    companion object {

        private const val DEFAULT_WIDTH = 612
        private const val DEFAULT_HEIGHT = 816
        private const val DEFAULT_QUALITY = 80
    }
}
