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
class ZonedPosition(
    var zoneX: Float,
    var zoneY: Float,
    var scale: Float,
    var zone: Zone,
) {
    var scaledX: Float
        get() = (zone.x2 - zone.x1) * zoneX + zone.x1
        set(value) {
            zone = calculateZone(value, scaledY)
            zoneX = getZoneX(value, zone)
        }

    var scaledY: Float
        get() = (zone.y2 - zone.y1) * zoneY + zone.y1
        set(value) {
            zone = calculateZone(scaledX, value)
            zoneY = getZoneY(value, zone)
        }

    var rawX: Float
        get() = scaledX * mc.window.scaledWidth
        set(x) {
            scaledX = x / mc.window.scaledWidth
        }

    var rawY: Float
        get() = scaledY * mc.window.scaledHeight
        set(y) {
            scaledY = y / mc.window.scaledHeight
        }

    override fun toString(): String {
        return "ZonedPosition(zoneX=$zoneX, zoneY=$zoneY, scale=$scale, zone=$zone)"
    }

    companion object {
        fun rawPositioning(x: Float, y: Float, scale: Float = 1f, origin: Zone = calculateZone(x / mc.window.scaledWidth, y / mc.window.scaledHeight)): ZonedPosition =
            scaledPositioning(calculateScaledZoneX(x, origin), calculateScaledZoneY(y, origin), scale, origin)

        fun scaledPositioning(x: Float, y: Float, scale: Float = 1f, zone: Zone = calculateZone(x, y)): ZonedPosition =
            ZonedPosition(getZoneX(x, zone), getZoneY(y, zone), scale, zone)

        fun calculateZone(x: Float, y: Float): Zone {
            return Zone.values().minByOrNull {
                it.distFrom(x, y)
            }!!
        }

        fun getZoneX(scaledX: Float, zone: Zone): Float =
            (scaledX - zone.x1) / (zone.x2 - zone.x1)
        fun getZoneY(scaledY: Float, zone: Zone): Float =
            (scaledY - zone.y1) / (zone.y2 - zone.y1)

        fun calculateScaledZoneX(rawX: Float, zone: Zone): Float =
            getZoneX(rawX / mc.window.scaledWidth, zone)
        fun calculateScaledZoneY(rawY: Float, zone: Zone): Float =
            getZoneY(rawY / mc.window.scaledHeight, zone)

        fun center(scale: Float = 1f) = scaledPositioning(0.5f, 0.5f, scale)
    }

    enum class Zone(override val x1: Float, override val y1: Float, override val x2: Float, override val y2: Float) : Rectangle {
        TOP_LEFT(0f, 0f, 0.5f, 0.5f),
        TOP_RIGHT(0.5f, 0f, 1f, 0.5f),
        BOTTOM_RIGHT(0.5f, 0.5f, 1f, 1f),
        BOTTOM_LEFT(0f, 0.5f, 0.5f, 1f);
    }
}
