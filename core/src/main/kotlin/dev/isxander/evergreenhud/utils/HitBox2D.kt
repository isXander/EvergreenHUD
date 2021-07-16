package dev.isxander.evergreenhud.utils

data class HitBox2D(val x: Float, val y: Float, val width: Float, val height: Float) {

    fun doesPositionOverlap(x: Float, y: Float): Boolean {
        return x >= this.x && x <= this.x + width && y >= y && y <= y + height
    }

}