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

package dev.isxander.evergreenhud.api.fabric11701.impl

import dev.isxander.evergreenhud.api.impl.UEntity
import net.minecraft.entity.Entity

class EntityImpl(val entity: Entity?) : UEntity() {

    override val isNull: Boolean = false

    override val x: Double = entity!!.x
    override val y: Double = entity!!.y
    override val z: Double = entity!!.z

    override val prevX: Double = entity!!.prevX
    override val prevY: Double = entity!!.prevY
    override val prevZ: Double = entity!!.prevZ

    override val id: Int = entity!!.id

    override val yaw: Float = entity!!.yaw
    override val pitch: Float = entity!!.pitch

    override fun getReachDistFromEntity(entity: UEntity): Double {
        return -1.0
    }

}