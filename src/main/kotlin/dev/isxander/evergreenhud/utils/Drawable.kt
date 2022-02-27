/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import dev.isxander.evergreenhud.EvergreenHUD
import gg.essential.elementa.components.UIBlock
import gg.essential.universal.UMatrixStack
import gg.essential.universal.UResolution
import gg.essential.universal.shader.BlendState
import gg.essential.universal.shader.UShader
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack

fun translate(x: Number = 0.0, y: Number = 0.0, z: Number = 0.0) =
    GlStateManager.translate(x.toDouble(), y.toDouble(), z.toDouble())

fun drawHorizontalLine(x1: Float, x2: Float, y: Float, width: Float, color: Int, shader: Boolean = true) {
    drawRect(x1, y, x2 + width, y + width, color, shader)
}

fun drawVerticalLine(x: Float, y1: Float, y2: Float, width: Float, color: Int, shader: Boolean = true) {
    drawRect(x, y1 + width, x + width, y2, color, shader)
}

fun drawBorderLines(x0: Float, y0: Float, x1: Float, y1: Float, width: Float, color: Int, shader: Boolean = true) {
    drawHorizontalLine(x0 - width, x1, y0 - width, width, color, shader)
    drawVerticalLine(x1, y0 - width, y1 + width, width, color, shader)
    drawHorizontalLine(x0 - width, x1, y1, width, color, shader)
    drawVerticalLine(x0 - width, y0 - width, y1 + width, width, color, shader)
}

fun drawRect(x1: Float, y1: Float, x2: Float, y2: Float, color: Int, shader: Boolean = true) {
    GlStateManager.enableBlend()
    GlStateManager.disableTexture2D()
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
    UMatrixStack.Compat.get().tessellate(DrawMode.QUADS, DefaultVertexFormats.POSITION_COLOR, shader) {
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
    GlStateManager.enableTexture2D()
    GlStateManager.disableBlend()
}

fun FontRenderer.drawCenteredText(
    text: String,
    centerX: Float,
    y: Float,
    color: Int
) {
    drawStringWithShadow(
        text,
        centerX - getStringWidth(text) / 2,
        y,
        color
    )
}

fun FontRenderer.drawCenteredTextWithShadow(
    text: String,
    centerX: Float,
    y: Float,
    color: Int
) {
    drawStringWithShadow(
        text,
        centerX - getStringWidth(text) / 2,
        y,
        color
    )
}

fun push(block: () -> Unit) {
    GlStateManager.pushMatrix()
    block()
    GlStateManager.popMatrix()
}


val shader by lazy { UShader.fromLegacyShader(EvergreenHUD::class.java.getResourceAsStream("/assets/evergreenhud/shaders/chroma.vsh")?.readBytes()?.decodeToString() ?: throw RuntimeException(), EvergreenHUD::class.java.getResourceAsStream("/assets/evergreenhud/shaders/chroma.fsh")?.readBytes()?.decodeToString() ?: throw RuntimeException(), BlendState.NORMAL) }
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

fun drawChromaRectangle() {
    val speed = 1000f

    shader.bind()
    shaderTimeUniform.setValue((System.currentTimeMillis() % speed.toDouble()).toInt())
    shaderSpeedUniform.setValue(speed)
    shaderFrequencyUniform.setValue(2f)

    UIBlock.drawBlockWithActiveShader(UMatrixStack.Compat.get(), Color.white.awt, 0.0, 0.0,
        UResolution.scaledWidth.toDouble(), UResolution.scaledHeight.toDouble()
    )

    shader.unbind()
}

fun EntityLivingBase?.renderEntity(posX: Float, posY: Float, scale: Float, viewRotation: Int = 0) {
    this?.let {
        GlStateManager.pushMatrix()
        GlStateManager.enableDepth()
        GlStateManager.color(1f, 1f, 1f, 1f)

        val ent = this

        GlStateManager.enableColorMaterial()
        GlStateManager.pushMatrix()
        GlStateManager.translate(posX, posY, 50.0f)
        GlStateManager.scale(-scale * 50, scale * 50, scale * 50)
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f)
        val f = ent.renderYawOffset
        val f1 = ent.rotationYaw
        val f2 = ent.rotationPitch
        val f3 = ent.prevRotationYawHead
        val f4 = ent.rotationYawHead
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f)
        RenderHelper.enableStandardItemLighting()
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f)
        val rotation = 360f - viewRotation
        ent.renderYawOffset = rotation
        ent.rotationYaw = rotation
        ent.rotationYawHead = ent.rotationYaw
        ent.prevRotationYawHead = ent.rotationYaw
        GlStateManager.translate(0.0f, 0.0f, 0.0f)
        val rendermanager = mc.renderManager
        rendermanager.setPlayerViewY(180.0f)
        rendermanager.isRenderShadow = false
        rendermanager.renderEntityWithPosYaw(ent, 0.0, 0.0, 0.0, 0.0f, 1.0f)
        rendermanager.isRenderShadow = true
        ent.renderYawOffset = f
        ent.rotationYaw = f1
        ent.rotationPitch = f2
        ent.prevRotationYawHead = f3
        ent.rotationYawHead = f4
        GlStateManager.popMatrix()
        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableRescaleNormal()
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit)
        GlStateManager.disableTexture2D()
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit)
        GlStateManager.popMatrix()
    }
}

/*
 * Taken from KronHUD under CC Attribution-NonCommercial 3.0.
 * Modified in Kotlin
 * https://github.com/Moulberry/NotEnoughUpdates/blob/master/LICENSE
 */
fun drawItemStack(stack: ItemStack?, x: Int, y: Int) {
    if (stack == null) return
    val itemRender = Minecraft.getMinecraft().renderItem
    RenderHelper.enableGUIStandardItemLighting()
    itemRender.zLevel = -145f //Negates the z-offset of the below method.
    itemRender.renderItemAndEffectIntoGUI(stack, x, y)
    itemRender.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRendererObj, stack, x, y, null)
    itemRender.zLevel = 0f
    RenderHelper.disableStandardItemLighting()
}
