package dev.isxander.evergreenhud.compatibility.universal.impl.entity

abstract class AIEntity {
    abstract fun isNull(): Boolean

    abstract fun getX(): Double
    abstract fun getY(): Double
    abstract fun getZ(): Double

    abstract fun getPrevX(): Double
    abstract fun getPrevY(): Double
    abstract fun getPrevZ(): Double

    abstract fun getId(): Int

    abstract fun getYaw(): Float
    abstract fun getPitch(): Float

    abstract fun getReachDistFromEntity(entity: AIEntity): Double

}