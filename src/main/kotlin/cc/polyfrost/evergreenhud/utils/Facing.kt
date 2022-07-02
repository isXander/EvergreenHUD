package cc.polyfrost.evergreenhud.utils

import net.minecraft.util.MathHelper

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