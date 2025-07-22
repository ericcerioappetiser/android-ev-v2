package com.appetiser.core.mediapicker.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

object FileUtils {

    fun processPickedFile(context: Context, providerPath: String, uri: Uri): File {
        val fileName = resolveFileName(context, uri)
        return saveFile(context, uri, fileName, providerPath)
    }

    fun resolveFileName(context: Context, uri: Uri): String {
        val nameColumn = MediaStore.Images.Media.DISPLAY_NAME

        val projection = arrayOf(nameColumn)

        context.contentResolver.query(uri, projection, null, null, null)?.use {
            if (it.moveToFirst()) {
                return it.getString(it.getColumnIndexOrThrow(nameColumn))
            }
        }
        return UUID.randomUUID().toString()
    }

    fun saveFile(context: Context, uri: Uri, fileName: String, providerPath: String): File {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val destination = privateFile(context, providerPath, fileName)
            destination.copyInputStreamToFile(inputStream)
            return destination
        }
        throw IllegalStateException("Failed to process picked file")
    }

    fun privateFile(context: Context, directory: String, fileName: String): File {
        val dir = File(context.filesDir, directory)
        dir.mkdirs()
        return File(dir, fileName)
    }

    fun File.copyInputStreamToFile(inputStream: InputStream) {
        this.outputStream().use { fileOut ->
            inputStream.copyTo(fileOut)
        }
    }

    fun fileFromContentUri(context: Context, contentUri: Uri): File {
        val fileExtension = getFileExtension(context, contentUri)
        val fileName = "temporary_file_${UUID.randomUUID()}" + if (fileExtension != null) ".$fileExtension" else ""

        val tempFile = File(context.filesDir, fileName)
        tempFile.createNewFile()

        try {
            val oStream = FileOutputStream(tempFile)
            val inputStream = context.contentResolver.openInputStream(contentUri)

            inputStream?.let {
                copy(inputStream, oStream)
            }

            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tempFile
    }

    private fun getFileExtension(context: Context, uri: Uri): String? {
        val fileType: String? = context.contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }

    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }
}
