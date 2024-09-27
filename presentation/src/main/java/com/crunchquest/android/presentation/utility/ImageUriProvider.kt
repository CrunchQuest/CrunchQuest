package com.crunchquest.android.presentation.utility

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.crunchquest.shared.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

class ImageUriProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getDefaultImageUri(): Uri {
        // Copy the default image resource to a temporary file and return its URI
        return getTempFileUriFromDrawable(context, R.drawable.food11)
    }

    fun getTempFileUriFromDrawable(context: Context, drawableResId: Int): Uri {
        // Create a temporary file in the cache directory
        val tempFile = File(context.cacheDir, "${UUID.randomUUID()}.png")

        // Open an OutputStream to the file
        val outputStream = FileOutputStream(tempFile)
        val inputStream = context.resources.openRawResource(drawableResId)

        // Copy the drawable data to the temporary file
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        // Return the URI of the temporary file
        return tempFile.toUri()
    }


    fun isValidContentUri(uri: Uri): Boolean {
        return try {
            context.contentResolver.openInputStream(uri)?.use {
                // If we can open an InputStream, it means the content URI is valid.
                true
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}
