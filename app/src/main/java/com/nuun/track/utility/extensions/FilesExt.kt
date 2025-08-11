package com.nuun.track.utility.extensions

import android.webkit.MimeTypeMap
import java.io.File

fun File.getMimeType(): String? {
    val extension = MimeTypeMap.getFileExtensionFromUrl(this.absolutePath)
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.lowercase())
}