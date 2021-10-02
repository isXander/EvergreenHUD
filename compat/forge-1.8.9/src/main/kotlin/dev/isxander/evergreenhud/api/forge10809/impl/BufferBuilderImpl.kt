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

package dev.isxander.evergreenhud.api.forge10809.impl

import dev.isxander.evergreenhud.api.impl.render.DrawMode
import dev.isxander.evergreenhud.api.impl.render.UBufferBuilder
import dev.isxander.evergreenhud.api.impl.render.VertexFormats
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats

class BufferBuilderImpl : UBufferBuilder() {
    private val tes = Tessellator.getInstance()
    private val buf: WorldRenderer = tes.worldRenderer

    override fun vertex(x: Double, y: Double, z: Double): UBufferBuilder {
        buf.pos(x, y, z)
        return this
    }

    override fun color(r: Float, g: Float, b: Float, a: Float): UBufferBuilder {
        buf.color(r, g, b, a)
        return this
    }

    override fun tex(u: Double, v: Double): UBufferBuilder {
        buf.tex(u, v)
        return this
    }

    override fun next(): UBufferBuilder {
        buf.endVertex()
        return this
    }
    override fun end(): UBufferBuilder {
        buf.endVertex()
        return this
    }

    override fun begin(mode: DrawMode, format: VertexFormats): UBufferBuilder {
        val parsedFormat = when (format) {
            VertexFormats.POSITION -> DefaultVertexFormats.POSITION
            VertexFormats.POSITION_COLOR -> DefaultVertexFormats.POSITION_COLOR
            VertexFormats.POSITION_TEXTURE -> DefaultVertexFormats.POSITION_TEX
            VertexFormats.POSITION_COLOR_TEXTURE -> DefaultVertexFormats.POSITION_TEX_COLOR
        }

        buf.begin(mode.glNum, parsedFormat)
        return this
    }

    override fun draw() = tes.draw()
}
