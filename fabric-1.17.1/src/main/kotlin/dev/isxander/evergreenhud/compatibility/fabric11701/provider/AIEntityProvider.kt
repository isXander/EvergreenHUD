package dev.isxander.evergreenhud.compatibility.fabric11701.provider

import dev.isxander.evergreenhud.compatibility.universal.impl.entity.AIEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.Entity

object AIEntityProvider {

    fun getEntity(entity: Entity?): AIEntity? {
        if (entity == null) return null

        return object : AIEntity() {
            override fun isNull(): Boolean = false

            override fun getX(): Double = entity.x
            override fun getY(): Double = entity.y
            override fun getZ(): Double = entity.z

            override fun setX(x: Double) = entity.setPos(x, getY(), getZ())
            override fun setY(y: Double) = entity.setPos(getX(), y, getZ())
            override fun setZ(z: Double) = entity.setPos(getX(), getY(), z)

            override fun getPrevX(): Double = entity.prevX
            override fun getPrevY(): Double = entity.prevY
            override fun getPrevZ(): Double = entity.prevZ

            override fun getId(): Int = entity.id

            override fun getYaw(): Float = entity.yaw
            override fun getPitch(): Float = entity.pitch

            override fun setYaw(yaw: Float) { entity.yaw = yaw }
            override fun setPitch(pitch: Float) { entity.pitch = pitch }

            override fun getReachDistFromEntity(entity: AIEntity) {
                TODO("Not yet implemented")
            }
        }
    }

    fun getPlayer(): AIEntity {
        val mc = MinecraftClient.getInstance()

        return object : AIEntity() {
            override fun isNull(): Boolean = mc.player == null

            override fun getX(): Double = mc.player!!.x
            override fun getY(): Double = mc.player!!.y
            override fun getZ(): Double = mc.player!!.z

            override fun setX(x: Double) = mc.player!!.setPos(x, getY(), getZ())
            override fun setY(y: Double) = mc.player!!.setPos(getX(), y, getZ())
            override fun setZ(z: Double) = mc.player!!.setPos(getX(), getY(), z)

            override fun getPrevX(): Double = mc.player!!.prevX
            override fun getPrevY(): Double = mc.player!!.prevY
            override fun getPrevZ(): Double = mc.player!!.prevZ

            override fun getId(): Int = mc.player!!.id

            override fun getYaw(): Float = mc.player!!.yaw
            override fun getPitch(): Float = mc.player!!.pitch

            override fun setYaw(yaw: Float) { mc.player!!.yaw = yaw }
            override fun setPitch(pitch: Float) { mc.player!!.pitch = pitch }

            override fun getReachDistFromEntity(entity: AIEntity) {
                TODO("Not yet implemented")
            }
        }
    }

}