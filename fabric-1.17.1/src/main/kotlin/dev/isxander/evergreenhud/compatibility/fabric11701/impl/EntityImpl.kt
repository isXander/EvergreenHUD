/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/gpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.compatibility.fabric11701.impl

import dev.isxander.evergreenhud.compatibility.universal.impl.entity.AIEntity
import net.minecraft.entity.Entity

class EntityImpl(val entity: Entity?) : AIEntity() {

    override fun isNull(): Boolean = false

    override fun getX(): Double = entity!!.x
    override fun getY(): Double = entity!!.y
    override fun getZ(): Double = entity!!.z

    override fun getPrevX(): Double = entity!!.prevX
    override fun getPrevY(): Double = entity!!.prevY
    override fun getPrevZ(): Double = entity!!.prevZ

    override fun getId(): Int = entity!!.id

    override fun getYaw(): Float = entity!!.yaw
    override fun getPitch(): Float = entity!!.pitch

    override fun getReachDistFromEntity(entity: AIEntity): Double {
        return -1.0
    }

}