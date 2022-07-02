package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.evergreenhud.utils.decimalFormat
import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.*
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.hud.TextHud
import cc.polyfrost.oneconfig.utils.dsl.mc
import net.minecraft.util.MathHelper

class Coordinates: Config(Mod("Coordinates", ModType.HUD), "coordinates.json") {
    @HUD(name = "Main")
    var hud = CoordinatesHud()

    init {
        initialize()
    }

    class CoordinatesHud : TextHud(true) {
        @DualOption(
            name = "Mode",
            left = "Vertical",
            right = "Horizontal"
        )
        var displayMode = false

        @Switch(
            name = "Show Axis"
        )
        var showAxis = true

        @Switch(
            name = "Show Direction"
        )
        var showDirection = false

        @Switch(
            name = "Show X"
        )
        var showX = true

        @Switch(
            name = "Show Y"
        )
        var showY = true

        @Switch(
            name = "Show Z"
        )
        var showZ = true

        @Slider(
            name = "Accuracy",
            min = 0f,
            max = 16f,
            step = 1
        )
        var accuracy = 0

        @Switch(
            name = "Trailing Zeros"
        )
        var trailingZeros = false

        override fun getLines(lines: MutableList<String>) {
            if (mc.thePlayer == null) {
                lines.add("Unknown")
                return
            }

            val df = decimalFormat(accuracy, trailingZeros)

            val sb = StringBuilder()
            val facing = Facing.parseExact(mc.thePlayer!!.rotationYaw)
            if (showX) {
                sb.append(if (showAxis) "X: " else "")
                sb.append(df.format(mc.thePlayer.posX))
                if (showDirection) {
                    sb.append(" (")

                    sb.append(when (facing) {
                        Facing.EAST, Facing.NORTH_EAST, Facing.SOUTH_EAST -> "+"
                        Facing.WEST, Facing.NORTH_WEST, Facing.SOUTH_WEST -> "-"
                        else -> " "
                    })

                    sb.append(")")
                }
                if (!displayMode) {
                    lines.add(sb.toString())
                    sb.setLength(0)
                } else if (showY || showZ) {
                    sb.append(", ")
                }
            }
            if (showY) {
                sb.append(if (showAxis) "Y: " else "")
                sb.append(df.format(mc.thePlayer.posY))
                if (!displayMode) {
                    lines.add(sb.toString())
                    sb.setLength(0)
                } else if (showZ) {
                    sb.append(", ")
                }
            }
            if (showZ) {
                sb.append(if (showAxis) "Z: " else "")
                sb.append(df.format(mc.thePlayer.posZ))
                if (showDirection) {
                    sb.append(" (")

                    sb.append(when (facing) {
                        Facing.NORTH, Facing.NORTH_EAST, Facing.NORTH_WEST -> "-"
                        Facing.SOUTH, Facing.SOUTH_WEST, Facing.SOUTH_EAST -> "+"
                        else -> " "
                    })

                    sb.append(")")
                }
                if (!displayMode) {
                    lines.add(sb.toString())
                    sb.setLength(0)
                }
            }

            if (displayMode) {
                lines.add(sb.toString())
            }
        }

        @Exclude
        enum class Facing(val full: String, val abbreviated: String) {
            NORTH("North", "N"),
            NORTH_EAST("North East", "NE"),
            EAST("East", "E"),
            SOUTH_EAST("South East", "SE"),
            SOUTH("South", "S"),
            SOUTH_WEST("South West", "SW"),
            WEST("West", "W"),
            NORTH_WEST("North West", "NW");

            companion object {
                fun parse(yaw: Float): Facing {
                    val rotationYaw = MathHelper.wrapAngleTo180_float(yaw)

                    return when {
                        rotationYaw >= 165f || rotationYaw <= -165 -> NORTH
                        rotationYaw in -165f..-105f -> NORTH_EAST
                        rotationYaw in -105f..-75f -> EAST
                        rotationYaw in -75f..-15f -> SOUTH_EAST
                        rotationYaw in -15f..15f -> SOUTH
                        rotationYaw in 15f..75f -> SOUTH_WEST
                        rotationYaw in 75f..105f -> WEST
                        rotationYaw in 105f..165f -> NORTH_WEST

                        else -> NORTH
                    }
                }

                fun parseExact(yaw: Float): Facing {
                    val rotationYaw = MathHelper.wrapAngleTo180_float(yaw)

                    return when {
                        rotationYaw == -180f -> NORTH
                        rotationYaw > -180f && rotationYaw < -90f -> NORTH_EAST
                        rotationYaw == -90f -> EAST
                        rotationYaw > -90f && rotationYaw < 0f -> SOUTH_EAST
                        rotationYaw == 0f -> SOUTH
                        rotationYaw > 0f && rotationYaw < 90f -> SOUTH_WEST
                        rotationYaw == 90f -> WEST
                        rotationYaw > 90f && rotationYaw < 180f -> NORTH_WEST

                        else -> NORTH
                    }
                }
            }
        }
    }
}