package com.chirag.petmoments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object ImageUtils {

    fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val filename = "IMG_${System.currentTimeMillis()}.jpg"
            val directory = File(context.filesDir, "pet_images")
            if (!directory.exists()) {
                directory.mkdirs()
            }

            val file = File(directory, filename)
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.close()

            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun getAvatarForCategory(category: String): String {
        return when (category.lowercase()) {
            "dog" -> "🐶"
            "cat" -> "🐱"
            "bird" -> "🦜"
            "rabbit" -> "🐰"
            "other" -> "🐾"
            else -> "🐾"
        }
    }
}