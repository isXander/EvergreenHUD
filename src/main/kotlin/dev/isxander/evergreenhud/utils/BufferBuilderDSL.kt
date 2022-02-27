/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import gg.essential.universal.UGraphics
import gg.essential.universal.UMatrixStack
import net.minecraft.client.renderer.vertex.VertexFormat

fun UMatrixStack.tessellate(drawMode: DrawMode, vertexFormat: VertexFormat, defaultShader: Boolean = true, action: BufferBuilderScope.() -> Unit) {
    val tessellator = UGraphics.getFromTessellator()

    if (defaultShader)
        tessellator.beginWithDefaultShader(drawMode, vertexFormat)
    else
        tessellator.beginWithActiveShader(drawMode, vertexFormat)

    BufferBuilderScope(this, tessellator).apply(action)
    tessellator.drawDirect()
}

class BufferBuilderScope(private val matrices: UMatrixStack, private val tessellator: UGraphics) {
    fun vertex(action: VertexScope.() -> Unit) {
        with(VertexScope().apply(action)) {
            pos?.let { tessellator.pos(matrices, it.first, it.second, it.third) }
            norm?.let { tessellator.norm(matrices, it.first, it.second, it.third) }
            tex?.let { tessellator.tex(it.first, it.second) }
            color?.let { tessellator.color(it.red, it.green, it.blue, it.alpha) }
            overlay?.let { tessellator.overlay(it.first, it.second) }
            light?.let { tessellator.light(it.first, it.second) }
        }

        tessellator.endVertex()
    }
}

class VertexScope {
    var pos: Triple<Double, Double, Double>? = null
        private set
    var tex: Pair<Double, Double>? = null
        private set
    var color: Color? = null
        private set
    var norm: Triple<Float, Float, Float>? = null
        private set
    var overlay: Pair<Int, Int>? = null
        private set
    var light: Pair<Int, Int>? = null
        private set

    fun pos(x: Number, y: Number, z: Number) {
        pos = Triple(x.toDouble(), y.toDouble(), z.toDouble())
    }
    fun tex(u: Number, v: Number) {
        tex = u.toDouble() to v.toDouble()
    }

    fun color(red: Float, green: Float, blue: Float, alpha: Float = 1f) {
        color = Color(red * 255, green * 255, blue * 255, alpha * 255)
    }
    fun color(red: Int, green: Int, blue: Int, alpha: Int = 255) {
        color = Color(red, green, blue, alpha)
    }
    fun color(rgba: Int) {
        color = Color(rgba)
    }
    fun color(color: Color) {
        this.color = color
    }
    fun color(color: java.awt.Color) {
        color(Color(color))
    }

    fun norm(x: Number, y: Number, z: Number) {
        norm = Triple(x.toFloat(), y.toFloat(), z.toFloat())
    }

    fun overlay(u: Number, v: Number) {
        overlay = u.toInt() to v.toInt()
    }

    fun light(u: Number, v: Number) {
        light = u.toInt() to v.toInt()
    }
}

typealias DrawMode = UGraphics.DrawMode
