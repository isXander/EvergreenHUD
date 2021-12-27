/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.utils.elementmeta

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

class ElementMetaSerializer : KSerializer<ElementMeta> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("elementMeta") {
            element<String>("id")
            element<String>("name")
            element<String>("category")
            element<String>("description")
            element<String>("credits")
            element<Int>("maxInstances")
        }

    override fun serialize(encoder: Encoder, value: ElementMeta) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.id)
            encodeStringElement(descriptor, 1, value.name)
            encodeStringElement(descriptor, 2, value.category)
            encodeStringElement(descriptor, 3, value.description)
            encodeStringElement(descriptor, 4, value.credits)
            encodeIntElement(descriptor, 5, value.maxInstances)
        }
    }

    override fun deserialize(decoder: Decoder): ElementMeta {
        return decoder.decodeStructure(descriptor) {
            ElementMeta(
                decodeStringElement(descriptor, 0),
                decodeStringElement(descriptor, 1),
                decodeStringElement(descriptor, 2),
                decodeStringElement(descriptor, 3),
                decodeStringElement(descriptor, 4),
                decodeIntElement(descriptor, 5)
            )
        }
    }
}
