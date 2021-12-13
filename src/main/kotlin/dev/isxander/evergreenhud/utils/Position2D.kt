/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.utils

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class Position2D private constructor(
    var scaledX: Float,
    var scaledY: Float,
    var scale: Float,
    var origin: Origin,
) {
    var rawX: Float
        get() = (origin.x + scaledX) * (mc.window.scaledWidth / 2)
        set(x) {
            origin = calculateOrigin(x, rawY)
            scaledX = calculateScaledX(x, origin)
        }
    var rawY: Float
        get() = (origin.y + scaledY) * (mc.window.scaledHeight / 2)
        set(y) {
            origin = calculateOrigin(rawX, y)
            scaledY = calculateScaledY(y, origin)
        }

    companion object {
        fun rawPositioning(x: Float, y: Float, scale: Float = 1f, origin: Origin = calculateOrigin(x / mc.window.scaledWidth, y / mc.window.scaledHeight)): Position2D =
            scaledPositioning(calculateScaledX(x, origin), calculateScaledY(y, origin), scale, origin)

        fun scaledPositioning(x: Float, y: Float, scale: Float = 1f, origin: Origin = calculateOrigin(x, y)): Position2D =
            Position2D(x, y, scale, origin)

        fun calculateOrigin(x: Float, y: Float): Origin {
            return Origin.values().sortedBy { sqrt(abs(it.x - x).pow(2) + abs(it.y - y).pow(2)) }[0]
        }

        fun calculateScaledX(rawX: Float, origin: Origin): Float =
            rawX / (mc.window.scaledWidth / 2) - origin.x
        fun calculateScaledY(rawY: Float, origin: Origin): Float =
            rawY / (mc.window.scaledHeight / 2) - origin.y
    }

    enum class Origin(val x: Float, val y: Float) {
        TOP_LEFT(0f, 0f),
        TOP_CENTER(0.5f, 0f),
        TOP_RIGHT(1f, 0f),
        RIGHT(1f, 0.5f),
        BOTTOM_RIGHT(1f, 1f),
        BOTTOM_CENTER(0.5f, 1f),
        BOTTOM_LEFT(0f, 1f),
        LEFT(0f, 0.5f),
        CENTER(0.5f, 0.5f),
    }
}

fun rawPosition(lambda: PositionBuilder.() -> Unit): Position2D =
    with(PositionBuilder().apply(lambda)) {
        Position2D.rawPositioning(x, y, scale)
    }

fun scaledPosition(lambda: PositionBuilder.() -> Unit): Position2D =
    with(PositionBuilder().apply(lambda)) {
        Position2D.scaledPositioning(x, y, scale)
    }

class PositionBuilder {
    var x: Float = 0f
    var y: Float = 0f
    var scale: Float = 1f
}
