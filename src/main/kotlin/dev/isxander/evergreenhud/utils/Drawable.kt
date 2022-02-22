/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import gg.essential.elementa.components.UIBlock
import gg.essential.universal.UMatrixStack
import gg.essential.universal.shader.UShader
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.*
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.OrderedText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.Matrix4f
import java.awt.image.BufferedImage
import java.util.function.BiConsumer

fun MatrixStack.translate(x: Number = 0.0, y: Number = 0.0, z: Number = 0.0) =
    translate(x.toDouble(), y.toDouble(), z.toDouble())

fun MatrixStack.drawHorizontalLine(x1: Float, x2: Float, y: Float, width: Float, color: Int, shader: Boolean = true) {
    fill(x1, y, x2 + width, y + width, color, shader)
}

fun MatrixStack.drawVerticalLine(x: Float, y1: Float, y2: Float, width: Float, color: Int, shader: Boolean = true) {
    fill(x, y1 + width, x + width, y2, color, shader)
}

fun MatrixStack.drawBorderLines(x0: Float, y0: Float, x1: Float, y1: Float, width: Float, color: Int, shader: Boolean = true) {
    drawHorizontalLine(x0 - width, x1, y0 - width, width, color, shader)
    drawVerticalLine(x1, y0 - width, y1 + width, width, color, shader)
    drawHorizontalLine(x0 - width, x1, y1, width, color, shader)
    drawVerticalLine(x0 - width, y0 - width, y1 + width, width, color, shader)
}

fun MatrixStack.fill(x1: Float, y1: Float, x2: Float, y2: Float, color: Int, shader: Boolean = true) {
    RenderSystem.enableBlend()
    RenderSystem.disableTexture()
    RenderSystem.defaultBlendFunc()
    tessellate(DrawMode.QUADS, VertexFormats.POSITION_COLOR, shader) {
        vertex {
            pos(x1, y2, 0f)
            color(color)
        }
        vertex {
            pos(x2, y2, 0f)
            color(color)
        }
        vertex {
            pos(x2, y1, 0f)
            color(color)
        }
        vertex {
            pos(x1, y1, 0f)
            color(color)
        }
    }
    RenderSystem.enableTexture()
    RenderSystem.disableBlend()
}

fun MatrixStack.fillGradient(
    startX: Float,
    startY: Float,
    endX: Float,
    endY: Float,
    colorStart: Int,
    colorEnd: Int,
    z: Float = 0f
) {
    RenderSystem.disableTexture()
    RenderSystem.enableBlend()
    RenderSystem.defaultBlendFunc()
    RenderSystem.setShader { GameRenderer.getPositionColorShader() }
    val tessellator = Tessellator.getInstance()
    val bufferBuilder = tessellator.buffer
    bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)
    fillGradient(
        peek().model,
        bufferBuilder,
        startX,
        startY,
        endX,
        endY,
        z,
        colorStart,
        colorEnd
    )
    tessellator.draw()
    RenderSystem.disableBlend()
    RenderSystem.enableTexture()
}

fun fillGradient(
    matrix: Matrix4f,
    builder: BufferBuilder,
    startX: Float,
    startY: Float,
    endX: Float,
    endY: Float,
    z: Float,
    colorStart: Int,
    colorEnd: Int
) {
    val (startR, startG, startB, startA) = extractRGBA(colorStart)
    val (endR, endG, endB, endA) = extractRGBA(colorEnd)

    builder.vertex(matrix, endX, startY, z).color(startR, startG, startB, startA).next()
    builder.vertex(matrix, startX, startY, z).color(startR, startG, startB, startA).next()
    builder.vertex(matrix, startX, endY, z).color(endR, endG, endB, endA).next()
    builder.vertex(matrix, endX, endY, z).color(endR, endG, endB, endA).next()
}

fun TextRenderer.drawCenteredText(
    matrices: MatrixStack,
    text: String,
    centerX: Float,
    y: Float,
    color: Int
) {
    drawWithShadow(
        matrices,
        text,
        centerX - getWidth(text) / 2,
        y,
        color
    )
}

fun TextRenderer.drawCenteredText(
    matrices: MatrixStack,
    text: Text,
    centerX: Float,
    y: Float,
    color: Int
) {
    val orderedText = text.asOrderedText()
    drawWithShadow(
        matrices,
        orderedText,
        centerX - getWidth(orderedText) / 2,
        y,
        color
    )
}

fun TextRenderer.drawCenteredTextWithShadow(
    matrices: MatrixStack,
    text: OrderedText,
    centerX: Float,
    y: Float,
    color: Int
) {
    drawWithShadow(
        matrices,
        text,
        centerX - getWidth(text) / 2,
        y,
        color
    )
}

/**
 * @param renderAction the action to render both the content and the outline, taking x and y positions as input
 */
fun drawWithOutline(x: Float, y: Float, renderAction: BiConsumer<Float, Float>) {
    RenderSystem.blendFuncSeparate(
        GlStateManager.class_4535.ZERO,
        GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA,
        GlStateManager.class_4535.SRC_ALPHA,
        GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA
    )
    renderAction.accept(x + 1, y)
    renderAction.accept(x - 1, y)
    renderAction.accept(x, y + 1)
    renderAction.accept(x, y - 1)
    RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA)
    renderAction.accept(x, y)
}

fun MatrixStack.drawSprite(x: Float, y: Float, z: Float = 0f, width: Float, height: Float, sprite: Sprite) {
    drawTexturedQuad(
        x,
        x + width,
        y,
        y + height,
        z,
        sprite.minU,
        sprite.maxU,
        sprite.minV,
        sprite.maxV
    )
}

/**
 * Draws a textured rectangle from a region in a texture.
 *
 * The width and height of the region are the same as
 * the dimensions of the rectangle.
 *
 * @param x the X coordinate of the rectangle
 * @param y the Y coordinate of the rectangle
 * @param z the Z coordinate of the rectangle
 * @param u the left-most coordinate of the texture region
 * @param v the top-most coordinate of the texture region
 * @param width the width of the rectangle
 * @param height the height of the rectangle
 * @param textureWidth the width of the entire texture
 * @param textureHeight the height of the entire texture
 */
fun MatrixStack.drawTexture(
    x: Float,
    y: Float,
    z: Float = 0f,
    u: Float,
    v: Float,
    width: Float,
    height: Float,
    textureWidth: Float = 256f,
    textureHeight: Float = 256f
) {
    drawTexture(
        x,
        x + width,
        y,
        y + height,
        z,
        width,
        height,
        u,
        v,
        textureWidth,
        textureHeight
    )
}

/**
 * Draws a textured rectangle from a region in a texture.
 *
 * @param x the X coordinate of the rectangle
 * @param y the Y coordinate of the rectangle
 * @param width the width of the rectangle
 * @param height the height of the rectangle
 * @param u the left-most coordinate of the texture region
 * @param v the top-most coordinate of the texture region
 * @param textureWidth the width of the entire texture
 * @param textureHeight the height of the entire texture
 * @param regionHeight the height of the texture region
 * @param regionWidth the width of the texture region
 */
fun MatrixStack.drawTexture(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    u: Float,
    v: Float,
    regionWidth: Float,
    regionHeight: Float,
    textureWidth: Float,
    textureHeight: Float
) {
    drawTexture(
        x,
        x + width,
        y,
        y + height,
        0f,
        regionWidth,
        regionHeight,
        u,
        v,
        textureWidth,
        textureHeight
    )
}

/**
 * Draws a textured rectangle from a region in a texture.
 *
 *
 * The width and height of the region are the same as
 * the dimensions of the rectangle.
 *
 * @param x the X coordinate of the rectangle
 * @param y the Y coordinate of the rectangle
 * @param u the left-most coordinate of the texture region
 * @param v the top-most coordinate of the texture region
 * @param width the width of the rectangle
 * @param height the height of the rectangle
 * @param textureWidth the width of the entire texture
 * @param textureHeight the height of the entire texture
 */
fun MatrixStack.drawTexture(
    x: Float,
    y: Float,
    u: Float,
    v: Float,
    width: Float,
    height: Float,
    textureWidth: Float,
    textureHeight: Float
) {
    drawTexture(x, y, width, height, u, v, width, height, textureWidth, textureHeight)
}

fun MatrixStack.drawTexture(
    x0: Float,
    x1: Float,
    y0: Float,
    y1: Float,
    z: Float = 0f,
    regionWidth: Float,
    regionHeight: Float,
    u: Float,
    v: Float,
    textureWidth: Float,
    textureHeight: Float
) {
    drawTexturedQuad(
        x0,
        x1,
        y0,
        y1,
        z,
        (u + 0.0f) / textureWidth,
        (u + regionWidth) / textureWidth,
        (v + 0.0f) / textureHeight,
        (v + regionHeight) / textureHeight
    )
}

fun MatrixStack.drawTexturedQuad(
    x0: Float,
    x1: Float,
    y0: Float,
    y1: Float,
    z: Float = 0f,
    u0: Float,
    u1: Float,
    v0: Float,
    v1: Float
) {
    RenderSystem.setShader { GameRenderer.getPositionTexShader() }
    val matrix = peek().model
    val bufferBuilder = Tessellator.getInstance().buffer
    bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE)
    bufferBuilder.vertex(matrix, x0, y1, z).texture(u0, v1).next()
    bufferBuilder.vertex(matrix, x1, y1, z).texture(u1, v1).next()
    bufferBuilder.vertex(matrix, x1, y0, z).texture(u1, v0).next()
    bufferBuilder.vertex(matrix, x0, y0, z).texture(u0, v0).next()
    bufferBuilder.end()
    BufferRenderer.draw(bufferBuilder)
}

fun MatrixStack.push(block: () -> Unit) {
    push()
    block()
    pop()
}

fun <T> scissor(x: Int, y: Int, width: Int, height: Int, block: () -> T) {
    RenderSystem.enableScissor(x, y, width, height)
    block()
    RenderSystem.disableScissor()
}

fun BufferedImage.toNativeImage(): NativeImage {
    val nativeImage = NativeImage(width, height, false)
    for (x in 0 until nativeImage.width) {
        for (y in 0 until nativeImage.height) {
            nativeImage.setPixelColor(x, y, getRGB(x, y))
        }
    }

    return nativeImage
}

val shader by lazy { UShader.readFromLegacyShader(resource("shaders/chroma")) }
val shaderTimeUniform by lazy { shader.getIntUniform("u_time") }
val shaderSpeedUniform by lazy { shader.getFloatUniform("u_speed") }
val shaderFrequencyUniform by lazy { shader.getFloatUniform("u_frequency") }

fun chroma(properties: Color.ChromaProperties, block: () -> Unit) {
    if (!properties.hasChroma) {
        block()
        return
    }

    shader.bind()
    shaderTimeUniform.setValue((System.currentTimeMillis() % properties.chromaSpeed.toDouble()).toInt())
    shaderSpeedUniform.setValue(properties.chromaSpeed)
    shaderFrequencyUniform.setValue(properties.chromaFrequency)

    block()

    shader.unbind()
}
fun MatrixStack.drawChromaRectangle() {
    val speed = 1000f

    shader.bind()
    shaderTimeUniform.setValue((System.currentTimeMillis() % speed.toDouble()).toInt())
    shaderSpeedUniform.setValue(speed)
    shaderFrequencyUniform.setValue(2f)

    UIBlock.drawBlockWithActiveShader(UMatrixStack(this), Color.white.awt, 0.0, 0.0, mc.window.scaledWidth.toDouble(), mc.window.scaledHeight.toDouble())

    shader.unbind()
}
