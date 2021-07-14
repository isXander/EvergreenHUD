package dev.isxander.evergreenhud.compatibility.universal.impl

abstract class AIResolution {
    abstract fun getDisplayWidth(): Int
    abstract fun getDisplayHeight(): Int

    abstract fun getScaledWidth(): Int
    abstract fun getScaledHeight(): Int

    abstract fun getScaleFactor(): Double

}