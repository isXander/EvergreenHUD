/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.config.element

import dev.isxander.evergreenhud.EvergreenHUD
import dev.isxander.evergreenhud.elements.Element
import dev.isxander.evergreenhud.utils.position.Position2D
import dev.isxander.settxi.serialization.asJson
import dev.isxander.settxi.serialization.populateFromJson
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.serializer

class ElementSerializer : KSerializer<Element> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("elements") {
            element<String>("id")
            element<Position2D>("position")
            element<JsonObject>("settings")
        }

    override fun serialize(encoder: Encoder, value: Element) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.metadata.id)
            encodeSerializableElement(descriptor, 1, serializer(), value.position)
            encodeSerializableElement(descriptor, 2, serializer(), value.settings.asJson())
        }
    }

    override fun deserialize(decoder: Decoder) = decoder.decodeStructure(descriptor) {
        lateinit var id: String
        lateinit var position: Position2D
        lateinit var settings: JsonObject

        while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> id = decodeStringElement(descriptor, index)
                1 -> position = decodeSerializableElement(descriptor, index, serializer())
                2 -> settings = decodeSerializableElement(descriptor, index, serializer())
                DECODE_DONE -> break
                else -> error("Unexpected index: $index")
            }
        }

        val element = EvergreenHUD.elementManager.getNewElementInstance<Element>(id)
            ?: error("Found unknown element id ($id) in json! This probably means someone tampered with the json!")

        element.position = position
        element.settings.populateFromJson(settings)

        element
    }
}
