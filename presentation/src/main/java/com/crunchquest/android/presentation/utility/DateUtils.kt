package com.crunchquest.android.presentation.utility

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private val inputDateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    private val outputDateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

    fun formatDate(date: Date): String {
        return outputDateFormat.format(date)
    }

    fun parseDate(dateString: String): Date? {
        return inputDateFormat.parse(dateString)
    }
}