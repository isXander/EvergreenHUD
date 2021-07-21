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

import dev.isxander.evergreenhud.compatibility.universal.impl.render.AIBufferBuilder
import dev.isxander.evergreenhud.compatibility.universal.impl.render.DrawMode
import dev.isxander.evergreenhud.compatibility.universal.impl.render.VertexFormats
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.BufferRenderer
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormat

class BufferBuilderImpl : AIBufferBuilder() {

    private val tes = Tessellator.getInstance()
    private val buf: BufferBuilder = tes.buffer

    override fun vertex(x: Double, y: Double, z: Double): AIBufferBuilder {
        buf.vertex(x, y, z)
        return this
    }

    override fun color(r: Float, g: Float, b: Float, a: Float): AIBufferBuilder {
        buf.color(r, g, b, a)
        return this
    }

    override fun tex(u: Double, v: Double): AIBufferBuilder {
        buf.texture(u.toFloat(), v.toFloat())
        return this
    }

    override fun next(): AIBufferBuilder {
        buf.next()
        return this
    }
    override fun end(): AIBufferBuilder {
        buf.end()
        return this
    }

    override fun begin(mode: DrawMode, format: VertexFormats): AIBufferBuilder {
        val parsedFormat = when (format) {
            VertexFormats.POSITION -> net.minecraft.client.render.VertexFormats.POSITION
            VertexFormats.POSITION_COLOR -> net.minecraft.client.render.VertexFormats.POSITION_COLOR
            VertexFormats.POSITION_TEXTURE -> net.minecraft.client.render.VertexFormats.POSITION_TEXTURE
            VertexFormats.POSITION_COLOR_TEXTURE -> net.minecraft.client.render.VertexFormats.POSITION_COLOR_TEXTURE
        }
        val parsedMode = when (mode) {
            DrawMode.LINES -> VertexFormat.DrawMode.LINES
            DrawMode.LINE_STRIP -> VertexFormat.DrawMode.LINE_STRIP
            DrawMode.TRIANGLES -> VertexFormat.DrawMode.TRIANGLES
            DrawMode.TRIANGLE_STRIP -> VertexFormat.DrawMode.TRIANGLE_STRIP
            DrawMode.TRIANGLE_FAN -> VertexFormat.DrawMode.TRIANGLE_FAN
            DrawMode.QUADS -> VertexFormat.DrawMode.QUADS
        }

        buf.begin(parsedMode, parsedFormat)

        return this
    }

    override fun draw() = BufferRenderer.draw(buf)

}