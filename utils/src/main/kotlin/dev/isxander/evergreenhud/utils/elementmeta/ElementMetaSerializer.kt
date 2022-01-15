/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils.elementmeta

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
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

    override fun deserialize(decoder: Decoder) = decoder.decodeStructure(descriptor) {
        lateinit var id: String
        lateinit var name: String
        lateinit var category: String
        lateinit var description: String
        lateinit var credits: String
        var maxInstances = 0

        while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> id = decodeStringElement(descriptor, index)
                1 -> name = decodeStringElement(descriptor, index)
                2 -> category = decodeStringElement(descriptor, index)
                3 -> description = decodeStringElement(descriptor, index)
                4 -> credits = decodeStringElement(descriptor, index)
                5 -> maxInstances = decodeIntElement(descriptor, index)
                DECODE_DONE -> break
                else -> error("Unknown index: $index")
            }
        }

        ElementMeta(id, name, category, description, credits, maxInstances)
    }
}
