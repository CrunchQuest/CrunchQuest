package com.crunchquest.android.presentation.utility

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream

object FileUtils {

    // Function to convert a drawable resource to a file URI
    fun getUriFromDrawable(context: Context, drawableId: Int): Uri {
        // Create a file in the cache directory
        val file = File(context.cacheDir, "image_${drawableId}.png")
        val inputStream = context.resources.openRawResource(drawableId)
        val outputStream = FileOutputStream(file)

        // Copy the content of the drawable to the file
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return file.toUri()
    }
}