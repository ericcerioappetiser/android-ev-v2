package com.appetiser.core.common.compose.components.utils

import com.appetiser.core.common.compose.R

sealed class ImagePickerOption(val label: Int, val onClick: () -> Unit) {
    class TakePicture(onClick: () -> Unit) : ImagePickerOption(R.string.take_picture, onClick)
    class FromGallery(onClick: () -> Unit) : ImagePickerOption(R.string.photo_library, onClick)
    class Cancel(onClick: () -> Unit) : ImagePickerOption(R.string.cancel, onClick)
}
