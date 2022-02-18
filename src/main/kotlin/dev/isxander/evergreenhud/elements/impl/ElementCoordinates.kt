/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.elements.type.MultiLineTextElement
import dev.isxander.evergreenhud.utils.Facing
import dev.isxander.evergreenhud.utils.decimalFormat
import dev.isxander.evergreenhud.utils.elementmeta.ElementMeta
import dev.isxander.evergreenhud.utils.mc
import dev.isxander.settxi.impl.*
import java.text.DecimalFormat

@ElementMeta(id = "evergreenhud:coordinates", name = "Coordinates", description = "Show your current coordinates in the world.", category = "World")
class ElementCoordinates : MultiLineTextElement("Coords") {
    var displayMode by option(DisplayMode.VERTICAL) {
        name = "Mode"
        category = "Coordinates"
        description = "How the coordinates should be displayed."
    }

    var showAxis by boolean(true) {
        name = "Show Axis"
        category = "Coordinates"
        description = "Show the 'X: ' before the number."
    }

    var showDirection by boolean(false) {
        name = "Show Direction"
        category = "Coordinates"
        description = "Show if the axis is going to increase or decrease depending on the direction you are facing."
    }

    var showX by boolean(true) {
        name = "Show X"
        category = "Coordinates"
        description = "Show the X axis."
    }

    var showY by boolean(true) {
        name = "Show Y"
        category = "Coordinates"
        description = "Show the Y axis."
    }

    var showZ by boolean(true) {
        name = "Show Z"
        category = "Coordinates"
        description = "Show the Z axis."
    }

    var accuracy by int(0) {
        name = "Accuracy"
        category = "Coordinates"
        description = "How many decimal places the value should display."
        range = 0..16
    }

    var trailingZeros by boolean(false) {
        name = "Trailing Zeros"
        category = "Coordinates"
        description = "Match the accuracy using zeros."
    }

    override fun calculateValue(): MutableList<String> {
        val lines = mutableListOf<String>()
        if (mc.player == null) {
            lines.add("Unknown")
            return lines
        }

        val df = decimalFormat(accuracy, trailingZeros)

        val sb = StringBuilder()
        val facing = Facing.parseExact(mc.player!!.yaw)
        if (showX) {
            sb.append(if (showAxis) "X: " else "")
            sb.append(df.format(mc.player!!.x))
            if (showDirection) {
                sb.append(" (")

                sb.append(when (facing) {
                    Facing.EAST, Facing.NORTH_EAST, Facing.SOUTH_EAST -> "+"
                    Facing.WEST, Facing.NORTH_WEST, Facing.SOUTH_WEST -> "-"
                    else -> " "
                })

                sb.append(")")
            }
            if (displayMode == DisplayMode.VERTICAL) {
                lines.add(sb.toString())
                sb.setLength(0)
            } else if (showY || showZ) {
                sb.append(", ")
            }
        }
        if (showY) {
            sb.append(if (showAxis) "Y: " else "")
            sb.append(df.format(mc.player!!.y))
            if (displayMode == DisplayMode.VERTICAL) {
                lines.add(sb.toString())
                sb.setLength(0)
            } else if (showZ) {
                sb.append(", ")
            }
        }
        if (showZ) {
            sb.append(if (showAxis) "Z: " else "")
            sb.append(df.format(mc.player!!.z))
            if (showDirection) {
                sb.append(" (")

                sb.append(when (facing) {
                    Facing.NORTH, Facing.NORTH_EAST, Facing.NORTH_WEST -> "-"
                    Facing.SOUTH, Facing.SOUTH_WEST, Facing.SOUTH_EAST -> "+"
                    else -> " "
                })

                sb.append(")")
            }
            if (displayMode == DisplayMode.VERTICAL) {
                lines.add(sb.toString())
                sb.setLength(0)
            }
        }

        if (displayMode == DisplayMode.HORIZONTAL) {
            lines.add(sb.toString())
        }
        return lines
    }

    object DisplayMode : OptionContainer() {
        val VERTICAL = option("Vertical", "Display each axis in a list.")
        val HORIZONTAL = option("Horizontal", "Display each axis in-line, seperated by a comma.")
    }
}
