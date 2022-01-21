/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils.position

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.serializer

class ZonedPositionSerializer : KSerializer<ZonedPosition> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("position") {
            element<Float>("x")
            element<Float>("y")
            element<Float>("scale")
            element<ZonedPosition.Zone>("zone")
        }

    override fun serialize(encoder: Encoder, value: ZonedPosition) {
        encoder.encodeStructure(descriptor) {
            encodeFloatElement(descriptor, 0, value.zoneX)
            encodeFloatElement(descriptor, 1, value.zoneY)
            encodeFloatElement(descriptor, 2, value.scale)
            encodeSerializableElement(descriptor, 3, serializer(), value.zone)
        }
    }

    override fun deserialize(decoder: Decoder) = decoder.decodeStructure(descriptor) {
        var x = 0f
        var y = 0f
        var scale = 0f
        lateinit var zone: ZonedPosition.Zone

        while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> x = decodeFloatElement(descriptor, index)
                1 -> y = decodeFloatElement(descriptor, index)
                2 -> scale = decodeFloatElement(descriptor, index)
                3 -> zone = decodeSerializableElement(descriptor, index, serializer())
                DECODE_DONE -> break
                else -> error("Unknown index: $index")
            }
        }

        ZonedPosition(x, y, scale, zone)
    }
}
