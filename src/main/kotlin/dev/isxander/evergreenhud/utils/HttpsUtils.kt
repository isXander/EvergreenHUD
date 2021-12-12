/*
 * EvergreenHUD - A mod to improve on your heads-up-display.
 * Copyright (C) isXander [2019 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-2.1.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.utils

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import org.bundleproject.libversion.Version

val http = HttpClient(Apache) {
    install(JsonFeature) {
        serializer = GsonSerializer {
            registerTypeAdapter(Version::class.java, VersionTypeAdapter)
        }
    }
}

object VersionTypeAdapter : TypeAdapter<Version>() {
    override fun write(out: JsonWriter?, value: Version?) {
        out?.let {
            value?.let { out.value(it.toString()) } ?: out.nullValue()
        }
    }

    override fun read(`in`: JsonReader?): Version {
        `in`?.let {
            return Version.of(it.nextString())
        } ?: error("The JsonReader given to the VersionTypeAdapter was null")
    }
}
