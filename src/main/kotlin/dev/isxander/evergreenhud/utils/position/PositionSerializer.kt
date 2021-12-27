/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.utils.position

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import org.lwjgl.system.CallbackI

class PositionSerializer : KSerializer<Position2D> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("position") {
            element<Float>("x")
            element<Float>("y")
            element<Float>("scale")
            element<String>("origin")
        }

    override fun serialize(encoder: Encoder, value: Position2D) {
        encoder.encodeStructure(descriptor) {
            encodeFloatElement(descriptor, 0, value.scaledX)
            encodeFloatElement(descriptor, 1, value.scaledY)
            encodeFloatElement(descriptor, 2, value.scale)
            encodeStringElement(descriptor, 3, value.origin.name)
        }
    }

    override fun deserialize(decoder: Decoder) = decoder.decodeStructure(descriptor) {
        var x = 0f
        var y = 0f
        var scale = 0f
        lateinit var origin: Position2D.Origin

        while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> x = decodeFloatElement(descriptor, index)
                1 -> y = decodeFloatElement(descriptor, index)
                2 -> scale = decodeFloatElement(descriptor, index)
                3 -> origin = decodeStringElement(descriptor, index).let { Position2D.Origin.valueOf(it) }
                DECODE_DONE -> break
                else -> error("Unknown index: $index")
            }
        }

        Position2D.scaledPositioning(x, y, scale, origin)
    }
}
