/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils.position

import dev.isxander.evergreenhud.utils.Rectangle
import dev.isxander.evergreenhud.utils.mc
import kotlinx.serialization.Serializable

@Serializable(ZonedPositionSerializer::class)
class ZonedPosition private constructor(
    var scaledX: Float,
    var scaledY: Float,
    var scale: Float,
    var zone: Zone,
) {
    var rawX: Float
        get() = ((zone.x2 - zone.x1) * scaledX + zone.x1) * mc.window.scaledWidth
        set(x) {
            zone = calculateOrigin(x, rawY)
            scaledX = calculateScaledX(x, zone)
        }

    var rawY: Float
        get() = ((zone.y2 - zone.y1) * scaledY + zone.y1) * mc.window.scaledHeight
        set(y) {
            zone = calculateOrigin(rawX, y)
            scaledY = calculateScaledY(y, zone)
        }

    companion object {
        fun rawPositioning(x: Float, y: Float, scale: Float = 1f, origin: Zone = calculateOrigin(x / mc.window.scaledWidth, y / mc.window.scaledHeight)): ZonedPosition =
            scaledPositioning(calculateScaledX(x, origin), calculateScaledY(y, origin), scale, origin)

        fun scaledPositioning(x: Float, y: Float, scale: Float = 1f, origin: Zone = calculateOrigin(x, y)): ZonedPosition =
            ZonedPosition(x, y, scale, origin)

        fun calculateOrigin(x: Float, y: Float): Zone {
            return Zone.values().minByOrNull {
                it.distFrom(x, y)
            }!!
        }

        fun calculateScaledX(rawX: Float, zone: Zone): Float =
            (rawX / mc.window.scaledWidth - zone.x1) / (zone.x2 - zone.x1)
        fun calculateScaledY(rawY: Float, zone: Zone): Float =
            (rawY / mc.window.scaledHeight - zone.y1) / (zone.y2 - zone.y1)
    }

    enum class Zone(override val x1: Float, override val y1: Float, override val x2: Float, override val y2: Float) : Rectangle {
        TOP_LEFT(0f, 0f, 0.5f, 0.5f),
        TOP_RIGHT(0.5f, 0f, 1f, 0.5f),
        BOTTOM_RIGHT(0.5f, 0.5f, 1f, 1f),
        BOTTOM_LEFT(0f, 0.5f, 0.5f, 1f);
    }
}
