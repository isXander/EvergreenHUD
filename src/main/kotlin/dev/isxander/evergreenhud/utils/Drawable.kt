/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.platform.GlStateManager.DstFactor
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor
import com.mojang.blaze3d.systems.RenderSystem
import gg.essential.elementa.components.UIBlock
import gg.essential.universal.UMatrixStack
import gg.essential.universal.shader.UShader
import net.minecraft.client.MinecraftClient
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.*
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.text.OrderedText
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Matrix4f
import net.minecraft.util.math.Quaternion
import net.minecraft.util.math.Vec3f
import java.awt.image.BufferedImage
import java.util.function.BiConsumer
import kotlin.math.ceil
import kotlin.math.floor

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
        peek().positionMatrix,
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
        GlStateManager.SrcFactor.ZERO,
        GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA,
        GlStateManager.SrcFactor.SRC_ALPHA,
        GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA
    )
    renderAction.accept(x + 1, y)
    renderAction.accept(x - 1, y)
    renderAction.accept(x, y + 1)
    renderAction.accept(x, y - 1)
    RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA)
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
    val matrix = peek().positionMatrix
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
            nativeImage.setColor(x, y, getRGB(x, y))
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

fun LivingEntity.renderEntity(x: Float, y: Float, size: Float, viewRotation: Float = 0f) {
    val matrixStack = RenderSystem.getModelViewStack()
    matrixStack.push()
    matrixStack.translate(
        x,
        y,
        1050f
    )
    matrixStack.scale(1.0f, 1.0f, -1.0f)
    RenderSystem.applyModelViewMatrix()
    val matrixStack2 = MatrixStack()
    matrixStack2.translate(0.0, 0.0, 1000.0)
    matrixStack2.scale(size, size, size)
    val quaternion = Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0f)
    matrixStack2.multiply(quaternion)

    val interpolatedYaw = lerp(prevYaw, yaw, mc.tickDelta)
    matrixStack2.multiply(Quaternion(Vec3f(0f, 1f, 0f), interpolatedYaw - 180f + viewRotation, true))
    val prevHeadYaw: Float = prevHeadYaw
    val headYaw: Float = headYaw

    this.headYaw = interpolatedYaw
    this.prevHeadYaw = interpolatedYaw
    DiffuseLighting.method_34742()
    val entityRenderDispatcher = mc.entityRenderDispatcher
    entityRenderDispatcher.rotation = quaternion
    entityRenderDispatcher.setRenderShadows(false)
    val immediate = mc.bufferBuilders.entityVertexConsumers
    RenderSystem.runAsFancy {
        entityRenderDispatcher.render(
            this,
            0.0,
            0.0,
            0.0,
            0f,
            mc.tickDelta,
            matrixStack2,
            immediate,
            0xF000F0
        )
    }
    immediate.draw()
    entityRenderDispatcher.setRenderShadows(true)
    this.prevHeadYaw = prevHeadYaw
    this.headYaw = headYaw
    matrixStack.pop()
    RenderSystem.applyModelViewMatrix()
    DiffuseLighting.enableGuiDepthLighting()
}

/*
 * Taken from KronHUD under GNU-GPLv3.
 * Modified to work in Kotlin
 * https://github.com/DarkKronicle/KronHUD/blob/master/src/main/java/io/github/darkkronicle/kronhud/util/ItemUtil.java
 */

fun renderGuiItemModel(stack: ItemStack?, x: Float, y: Float, matrices: (MatrixStack) -> Unit) {
    val model = mc.itemRenderer.getModel(stack, null, null, 0)
    mc.textureManager.getTexture(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).setFilter(false, false)
    RenderSystem.setShaderTexture(0, PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)
    RenderSystem.enableBlend()
    RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA)
    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
    val matrixStack = RenderSystem.getModelViewStack()
    matrixStack.push()
    matrices.invoke(matrixStack)
    matrixStack.push()
    matrixStack.translate(x.toDouble(), y.toDouble(), (100.0f + mc.itemRenderer.zOffset).toDouble())
    matrixStack.translate(8.0, 8.0, 0.0)
    matrixStack.scale(1.0f, -1.0f, 1.0f)
    matrixStack.scale(16.0f, 16.0f, 16.0f)
    RenderSystem.applyModelViewMatrix()
    val matrixStack2 = MatrixStack()
    val immediate = MinecraftClient.getInstance().bufferBuilders.entityVertexConsumers
    val bl: Boolean = !model.isSideLit
    if (bl) {
        DiffuseLighting.disableGuiDepthLighting()
    }

    mc.itemRenderer.renderItem(
        stack,
        ModelTransformation.Mode.GUI,
        false,
        matrixStack2,
        immediate,
        15728880,
        OverlayTexture.DEFAULT_UV,
        model
    )
    immediate.draw()
    RenderSystem.enableDepthTest()
    if (bl) {
        DiffuseLighting.enableGuiDepthLighting()
    }

    matrixStack.pop()
    matrixStack.pop()
    RenderSystem.applyModelViewMatrix()
}

fun renderGuiItemOverlay(
    stack: ItemStack, x: Float, y: Float,
    countLabel: String?, showDurabilityBar: Boolean, textColor: Int, shadow: Boolean, matrices: (MatrixStack) -> Unit
) {
    if (!stack.isEmpty) {
        val matrixStack = MatrixStack()
        matrixStack.push()
        matrices.invoke(matrixStack)
        if (stack.count != 1 || countLabel != null) {
            val string = countLabel ?: stack.count.toString()
            matrixStack.translate(0.0, 0.0, (mc.itemRenderer.zOffset + 200.0f).toDouble())
            val immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().buffer)
            mc.textRenderer.draw(
                string, (x + 19 - 2 - mc.textRenderer.getWidth(string)), y + 6 + 3,
                textColor,
                shadow,
                matrixStack.peek().positionMatrix,
                immediate,
                false,
                0,
                15728880
            )
            immediate.draw()
        }
        if (stack.isItemBarVisible && showDurabilityBar) {
            RenderSystem.disableDepthTest()
            RenderSystem.disableTexture()
            RenderSystem.disableBlend()
            val i = stack.itemBarStep
            val j = stack.itemBarColor
            matrixStack.fill(x + 2, y + 13, 13f, 2f, Color(0, 0, 0, 255).rgba)
            matrixStack.fill(x + 2, y + 13, i.toFloat(), 1f, j)
            RenderSystem.enableBlend()
            RenderSystem.enableTexture()
            RenderSystem.enableDepthTest()
        }
        val clientPlayerEntity = MinecraftClient.getInstance().player
        val f = clientPlayerEntity?.itemCooldownManager?.getCooldownProgress(
            stack.item,
            MinecraftClient.getInstance().tickDelta
        )
            ?: 0.0f
        if (f > 0.0f) {
            RenderSystem.disableDepthTest()
            RenderSystem.disableTexture()
            RenderSystem.enableBlend()
            RenderSystem.defaultBlendFunc()
            matrixStack.fill(x, y + floor(16f * (1f - f)), 16f, ceil(16f * f), Color.white.withAlpha(127).rgba)
            RenderSystem.enableTexture()
            RenderSystem.enableDepthTest()
        }
        matrixStack.pop()
    }
}
