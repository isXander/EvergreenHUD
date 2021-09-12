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

package dev.isxander.evergreenhud.api.impl

abstract class UEntity {
    abstract val isNull: Boolean

    override operator fun equals(other: Any?): Boolean {
        if (other == null && isNull) return true
        return super.equals(other)
    }

    abstract val x: Double
    abstract val y: Double
    abstract val z: Double

    abstract val prevX: Double
    abstract val prevY: Double
    abstract val prevZ: Double

    abstract val id: Int

    abstract val yaw: Float
    abstract val pitch: Float

    abstract fun getReachDistFromEntity(entity: UEntity): Double

}