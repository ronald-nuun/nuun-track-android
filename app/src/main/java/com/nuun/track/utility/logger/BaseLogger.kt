package com.nuun.track.utility.logger

import android.util.Log
import com.nuun.track.utility.consts.NuunTag

abstract class BaseLogger(
    private val defaultTag: String = NuunTag.DEFAULT,
    private val isLogEnabled: Boolean = true
) : Logger {

    protected fun log(tag: String?, level: Int, msg: String) {
        if (!isLogEnabled) return

        val stackTrace = Throwable().stackTrace
        val caller = stackTrace.firstOrNull { element ->
            element.className !in listOf(
                this::class.java.name,
                BaseLogger::class.java.name
            ) && !element.className.contains("Logger")
        }

        val callerInfo = caller?.let {
            "[${it.fileName}:${it.lineNumber}] ${it.methodName}()"
        } ?: "UnknownCaller"

        val formattedTag = buildString {
            append("[$defaultTag]")
            if (!tag.isNullOrBlank()) append(" [$tag]")
        }

        val fullMessage = "$callerInfo: $msg"

        when (level) {
            Log.DEBUG -> Log.d(formattedTag, fullMessage)
            Log.INFO -> Log.i(formattedTag, fullMessage)
            Log.WARN -> Log.w(formattedTag, fullMessage)
            Log.ERROR -> Log.e(formattedTag, fullMessage)
        }
    }

    override fun debug(msg: String) {
        log(null, Log.DEBUG, msg)
    }

    override fun info(msg: String) {
        log(null, Log.INFO, msg)
    }

    override fun warning(msg: String) {
        log(null, Log.WARN, msg)
    }

    override fun error(msg: String) {
        log(null, Log.ERROR, msg)
    }

    override fun debug(tag: String, msg: String) {
        log(tag, Log.DEBUG, msg)
    }

    override fun info(tag: String, msg: String) {
        log(tag, Log.INFO, msg)
    }

    override fun warning(tag: String, msg: String) {
        log(tag, Log.WARN, msg)
    }

    override fun error(tag: String, msg: String) {
        log(tag, Log.ERROR, msg)
    }

    override fun separator() {
        Log.d(defaultTag, "====================")
    }
}