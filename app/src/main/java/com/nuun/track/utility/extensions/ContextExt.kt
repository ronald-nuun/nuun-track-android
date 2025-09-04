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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

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

fun Context.openCameraPermissionSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
    }
    startActivity(intent)
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

fun Context.uriToMultipart(uri: Uri): MultipartBody.Part? {
    val contentResolver = this.contentResolver
    val fileName = uri.getFileName(contentResolver) ?: return null
    val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"

    val inputStream = contentResolver.openInputStream(uri) ?: return null
    val requestBody = inputStream.readBytes().toRequestBody(mimeType.toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("files[]", fileName, requestBody)
}