package com.nuun.track.utility.extensions

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns

fun Uri.getFileName( contentResolver: ContentResolver): String? {
    var name: String? = null
    val cursor = contentResolver.query(this, null, null, null, null)
    cursor?.use {
        val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (it.moveToFirst() && index >= 0) {
            name = it.getString(index)
        }
    }
    return name
}