package com.nuun.track.utility.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat

fun Context.toastShortExt(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.checkDevicePermission(permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.openNotificationSettings() {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        }
    } else {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
    }
    this.startActivity(intent)
}

fun Context.getMimeType(uri: Uri): String? {
    return this.contentResolver.getType(uri)
}

fun Context.getVideoThumbnailFromUri(uri: Uri): Bitmap? {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(this, uri)
        retriever.frameAtTime
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        retriever.release()
    }
}