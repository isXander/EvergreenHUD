/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
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
