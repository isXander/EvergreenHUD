/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.utils

import dev.isxander.evergreenhud.EvergreenInfo
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun setupRequest(url: String): Request.Builder {
    return Request.Builder()
        .url(url)
        .addHeader("User-Agent", "EvergreenHUD/${EvergreenInfo.VERSION_FULL}")
}

fun getResponse(url: String): Response? {
    try {
        val client = OkHttpClient()
        val request: Request = setupRequest(url).build()
        return client.newCall(request).execute()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

fun getRemoteBytes(url: String): ByteArray {
    return getResponse(url)!!.body!!.bytes()
}

fun getRemoteString(url: String): String {
    return getResponse(url)!!.body!!.string()
}

fun downloadFile(url: String, file: File) {
    FileOutputStream(file).use {
        it.write(getRemoteBytes(url))
    }
}