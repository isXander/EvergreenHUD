package dev.isxander.evergreenhud.utils

import dev.isxander.evergreenhud.compatibility.universal.RESOLUTION

class Position private constructor(var scaledX: Float, var scaledY: Float, var scale: Float) {

    var rawX: Float
        get() = RESOLUTION.getScaledWidth().toFloat() * scaledX
        set(x) { scaledX = MathUtils.getPercent(x, 0f, RESOLUTION.getScaledWidth().toFloat()) }
    var rawY: Float
        get() = RESOLUTION.getScaledHeight().toFloat() * scaledY
        set(y) { scaledY = MathUtils.getPercent(y, 0f, RESOLUTION.getScaledHeight().toFloat()) }

    companion object {
        fun rawPositioning(x: Int, y: Int, scale: Float = 1f): Position =
            Position(MathUtils.getPercent(x.toFloat(), 0f, RESOLUTION.getScaledWidth().toFloat()), MathUtils.getPercent(y.toFloat(), 0f, RESOLUTION.getScaledHeight().toFloat()), scale)

        fun scaledPositioning(x: Float, y: Float, scale: Float = 1f): Position =
            Position(x, y, scale)
    }

}