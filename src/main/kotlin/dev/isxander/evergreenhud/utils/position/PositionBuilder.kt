/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.utils.position

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
