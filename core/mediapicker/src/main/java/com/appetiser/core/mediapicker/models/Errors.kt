package com.appetiser.core.mediapicker.models

object MediaPickerCancelled : RuntimeException() {

    private fun readResolve(): Any = MediaPickerCancelled
}

object MediaPickerCameraPermissionError : RuntimeException() {

    private fun readResolve(): Any = MediaPickerCameraPermissionError
}

data class MediaPickerError(override val message: String) : RuntimeException(message)
