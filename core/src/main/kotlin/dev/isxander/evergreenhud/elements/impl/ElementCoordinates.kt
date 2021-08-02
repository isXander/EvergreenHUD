/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.elements.impl

import dev.isxander.evergreenhud.compatibility.universal.MC
import dev.isxander.evergreenhud.elements.ElementMeta
import dev.isxander.evergreenhud.elements.type.MultiLineTextElement
import dev.isxander.evergreenhud.settings.impl.BooleanSetting
import dev.isxander.evergreenhud.settings.impl.IntSetting
import dev.isxander.evergreenhud.settings.impl.OptionContainer
import dev.isxander.evergreenhud.settings.impl.OptionSetting
import dev.isxander.evergreenhud.utils.Facing
import java.text.DecimalFormat

@ElementMeta(id = "COORDINATES", name = "Coordinates", description = "Show your current coordinates in the world.", category = "World")
class ElementCoordinates : MultiLineTextElement() {

    @OptionSetting(name = "Mode", category = ["Advanced"], description = "How the coordinates should be displayed.")
    var displayMode = DisplayMode.VERTICAL

    @BooleanSetting(name = "Show Axis", category = ["Advanced"], description = "Show the 'X: ' before the number.")
    var showAxis = true

    @BooleanSetting(name = "Show Direction", category = ["Advanced"], description = "Show if the axis is going to increase or decrease based on your direction.")
    var showDirection = false

    @BooleanSetting(name = "Show X", category = ["Advanced"], description = "Show the X axis.")
    var showX = true

    @BooleanSetting(name = "Show Y", category = ["Advanced"], description = "Show the Y axis.")
    var showY = true

    @BooleanSetting(name = "Show Z", category = ["Advanced"], description = "Show the Z axis.")
    var showZ = true

    @IntSetting(name = "Accuracy", category = ["Advanced"], description = "How many decimal places the value should display.", min = 0, max = 4, suffix = " places")
    var accuracy = 0

    @BooleanSetting(name = "Trailing Zeros", category = ["Advanced"], description = "Match the accuracy using zeros.")
    var trailingZeros = false

    override var title: String = "Coords"

    override fun calculateValue(): ArrayList<String> {
        val lines = arrayListOf<String>()
        if (MC.player.equals(null)) {
            lines.add("Unknown")
            return lines
        }

        val formatter = if (trailingZeros) "0" else "#"
        val formatBuilder = StringBuilder(if (accuracy < 1) formatter else "$formatter.")
        for (i in 0 until accuracy) formatBuilder.append(formatter)
        val df = DecimalFormat(formatBuilder.toString())

        val sb = StringBuilder()
        val facing = Facing.parseExact(MC.player.yaw)
        if (showX) {
            sb.append(if (showAxis) "X: " else "")
            sb.append(df.format(MC.player.x))
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
            sb.append(df.format(MC.player.y))
            if (displayMode == DisplayMode.VERTICAL) {
                lines.add(sb.toString())
                sb.setLength(0)
            } else if (showZ) {
                sb.append(", ")
            }
        }
        if (showZ) {
            sb.append(if (showAxis) "Z: " else "")
            sb.append(df.format(MC.player.z))
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