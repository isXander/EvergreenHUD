/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import gg.essential.universal.UResolution
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.serializer
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

@Serializable(OriginedPositionSerializer::class)
class OriginedPosition private constructor(
    var scaledX: Float,
    var scaledY: Float,
    var scale: Float,
    var origin: Origin,
) {
    var rawX: Float
        get() = (origin.x + scaledX) * (UResolution.scaledWidth / 2)
        set(x) {
            origin = calculateOrigin(x, rawY)
            scaledX = calculateScaledX(x, origin)
        }
    var rawY: Float
        get() = (origin.y + scaledY) * (UResolution.scaledHeight / 2)
        set(y) {
            origin = calculateOrigin(rawX, y)
            scaledY = calculateScaledY(y, origin)
        }

    companion object {
        fun rawPositioning(x: Float, y: Float, scale: Float = 1f, origin: Origin = calculateOrigin(x / UResolution.scaledWidth, y / UResolution.scaledHeight)): OriginedPosition =
            scaledPositioning(calculateScaledX(x, origin), calculateScaledY(y, origin), scale, origin)

        fun scaledPositioning(x: Float, y: Float, scale: Float = 1f, origin: Origin = calculateOrigin(x, y)): OriginedPosition =
            OriginedPosition(x, y, scale, origin)

        fun center(scale: Float = 1f): OriginedPosition =
            scaledPositioning(1f, 1f, scale, Origin.TOP_LEFT)

        fun calculateOrigin(x: Float, y: Float): Origin {
            return Origin.values().minByOrNull { sqrt(abs(it.x - x).pow(2) + abs(it.y - y).pow(2)) }!!
        }

        fun calculateScaledX(rawX: Float, origin: Origin): Float =
            rawX / (UResolution.scaledWidth / 2) - origin.x
        fun calculateScaledY(rawY: Float, origin: Origin): Float =
            rawY / (UResolution.scaledHeight / 2) - origin.y
    }

    @Serializable
    enum class Origin(val x: Float, val y: Float) {
        TOP_LEFT(0f, 0f),
        TOP_RIGHT(1f, 0f),
        BOTTOM_RIGHT(1f, 1f),
        BOTTOM_LEFT(0f, 1f),
    }
}

class OriginedPositionSerializer : KSerializer<OriginedPosition> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("position") {
            element<Float>("x")
            element<Float>("y")
            element<Float>("scale")
            element<OriginedPosition.Origin>("origin")
        }

    override fun serialize(encoder: Encoder, value: OriginedPosition) {
        encoder.encodeStructure(descriptor) {
            encodeFloatElement(descriptor, 0, value.scaledX)
            encodeFloatElement(descriptor, 1, value.scaledY)
            encodeFloatElement(descriptor, 2, value.scale)
            encodeSerializableElement(descriptor, 3, serializer(), value.origin)
        }
    }

    override fun deserialize(decoder: Decoder) = decoder.decodeStructure(descriptor) {
        var x = 0f
        var y = 0f
        var scale = 0f
        lateinit var origin: OriginedPosition.Origin

        while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                0 -> x = decodeFloatElement(descriptor, index)
                1 -> y = decodeFloatElement(descriptor, index)
                2 -> scale = decodeFloatElement(descriptor, index)
                3 -> origin = decodeSerializableElement(descriptor, index, serializer())
                DECODE_DONE -> break
                else -> error("Unknown index: $index")
            }
        }

        OriginedPosition.scaledPositioning(x, y, scale, origin)
    }
}
