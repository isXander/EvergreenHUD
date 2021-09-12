/*
 * EvergreenHUD - A mod to improve on your heads-up-display.
 * Copyright (C) isXander [2019 - 2021]
 *
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-2.1.en.html
 *
 * If you have any questions or concerns, please create
 * an issue on the github page that can be found here
 * https://github.com/isXander/EvergreenHUD
 *
 * If you have a private concern, please contact
 * isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.utils

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
            val rotationYaw = wrapAngleTo180(yaw)

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
            val rotationYaw = wrapAngleTo180(yaw)

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