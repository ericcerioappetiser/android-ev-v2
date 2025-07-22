package com.appetiser.feature.report.network.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

private fun String.convertImageBitmapToBase64(): String {
    val baos = ByteArrayOutputStream()
    val bitmap = BitmapFactory.decodeFile(File(this).absolutePath) ?: return ""
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    return "data:image/png;base64, plus(Base64.encodeToString(output.toByteArray(), Base64.DEFAULT))".plus(
        Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
    )
}

fun String.convertImageFileStreamToBase64(): String {
    val inputStream = FileInputStream(File(this).absolutePath)
    val buffer = ByteArray(8192)
    val output = ByteArrayOutputStream()
    try {
        while (true) {
            val length = inputStream.read(buffer)
            if (length <= 0) {
                break
            }
            output.write(buffer, 0, length)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    if (output.toByteArray().isNotEmpty()) {
        return "data:image/png;base64,".plus(Base64.encodeToString(output.toByteArray(), Base64.DEFAULT))
            .plus(Base64.encodeToString(output.toByteArray(), Base64.DEFAULT))
    }

    return this.convertImageBitmapToBase64()
}

fun List<String>.convertAttachmentsToMultiPartBody(key: String): ArrayList<MultipartBody.Part> {
    val imageParts = ArrayList<MultipartBody.Part>()
    this.forEach { uri ->
        imageParts.add(attachmentToMultipartBody(key, uri))
    }
    return imageParts
}

private fun attachmentToMultipartBody(key: String, imagePath: String): MultipartBody.Part {
    val imageFile = File(imagePath)
    val requestImageBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(
        "$key[]",
        "${key}_${imageFile.name}",
        requestImageBody
    )
}
