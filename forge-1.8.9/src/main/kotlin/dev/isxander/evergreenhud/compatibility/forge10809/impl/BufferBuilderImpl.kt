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

package dev.isxander.evergreenhud.compatibility.forge10809.impl

import dev.isxander.evergreenhud.compatibility.universal.impl.render.AIBufferBuilder
import dev.isxander.evergreenhud.compatibility.universal.impl.render.DrawMode
import dev.isxander.evergreenhud.compatibility.universal.impl.render.VertexFormats
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.renderer.vertex.VertexFormat

class BufferBuilderImpl : AIBufferBuilder() {

    private val tes = Tessellator.getInstance()
    private val buf: WorldRenderer = tes.worldRenderer

    override fun vertex(x: Double, y: Double, z: Double): AIBufferBuilder {
        buf.pos(x, y, z)
        return this
    }

    override fun color(r: Float, g: Float, b: Float, a: Float): AIBufferBuilder {
        buf.color(r, g, b, a)
        return this
    }

    override fun tex(u: Double, v: Double): AIBufferBuilder {
        buf.tex(u, v)
        return this
    }

    override fun next(): AIBufferBuilder {
        buf.endVertex()
        return this
    }
    override fun end(): AIBufferBuilder {
        buf.endVertex()
        return this
    }

    override fun begin(mode: DrawMode, format: VertexFormats): AIBufferBuilder {
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