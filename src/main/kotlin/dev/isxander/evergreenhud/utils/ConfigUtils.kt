/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.bundleproject.libversion.Version

val json = Json {
    prettyPrint = true
    serializersModule = SerializersModule {
        contextual(VersionContentConverter)
    }
}

object VersionContentConverter : KSerializer<Version> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("version", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Version {
        return Version.of(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: Version) {
        encoder.encodeString(value.toString())
    }
}

inline fun <reified T> JsonObject.decode(key: String): T? {
    return this[key]?.let { json.decodeFromJsonElement(it) }
}
