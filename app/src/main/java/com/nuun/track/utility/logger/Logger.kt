package com.nuun.track.utility.logger

interface Logger {
    fun debug(tag: String, msg: String)
    fun debug(msg: String)

    fun info(tag: String, msg: String)
    fun info(msg: String)

    fun warning(tag: String, msg: String)
    fun warning(msg: String)

    fun error(tag: String, msg: String)
    fun error(msg: String)

    fun separator()
}