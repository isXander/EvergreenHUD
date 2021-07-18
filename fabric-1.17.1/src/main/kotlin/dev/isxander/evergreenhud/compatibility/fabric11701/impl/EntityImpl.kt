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