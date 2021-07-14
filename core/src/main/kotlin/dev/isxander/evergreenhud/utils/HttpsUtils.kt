package dev.isxander.evergreenhud.utils

import dev.isxander.evergreenhud.EvergreenInfo
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object HttpsUtils {

    fun setupRequest(url: String): Request.Builder {
        return Request.Builder()
            .url(url)
            .addHeader("User-Agent", "EvergreenHUD/${EvergreenInfo.MOD_VERSION}")
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

    fun getBytes(url: String): ByteArray {
        return getResponse(url)!!.body!!.bytes()
    }

    fun getString(url: String): String {
        return getResponse(url)!!.body!!.string()
    }

    fun downloadFile(url: String, file: File) {
        FileOutputStream(file).use {
            it.write(getBytes(url))
        }
    }

}