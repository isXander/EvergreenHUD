package dev.isxander.evergreenhud.compatibility.fabric11701.impl

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import dev.isxander.evergreenhud.compatibility.fabric11701.Main
import dev.isxander.evergreenhud.compatibility.universal.impl.render.AIGL11
import net.minecraft.client.render.GameRenderer
import org.lwjgl.opengl.GL11

class GLImpl : AIGL11() {
    override fun color(r: Float, g: Float, b: Float, a: Float) = GL11.glColor4f(r, g, b, a)

    override fun bindTexture(texture: Int) = RenderSystem.bindTexture(texture)

    override fun scissor(x: Int, y: Int, width: Int, height: Int) = GlStateManager._scissorBox(x, y, width, height)
    override fun enableScissor() = GlStateManager._enableScissorTest()
    override fun disableScissor() = GlStateManager._disableScissorTest()

    override fun lineWidth(width: Float) = RenderSystem.lineWidth(width)

    override fun enable(target: Int) = GL11.glEnable(target)
    override fun disable(target: Int) = GL11.glDisable(target)

    override fun begin(mode: Int) = GL11.glBegin(mode)
    override fun end() = GL11.glEnd()

    override fun enableBlend() = RenderSystem.enableBlend()
    override fun disableBlend() = RenderSystem.disableBlend()

    override fun enableTexture() = RenderSystem.enableTexture()
    override fun disableTexture() = RenderSystem.disableTexture()

    override fun enableAlpha() = enable(GL_ALPHA_TEST)
    override fun disableAlpha() = disable(GL_ALPHA_TEST)

    override fun enableDepth() = RenderSystem.enableDepthTest()
    override fun disableDepth() = RenderSystem.disableDepthTest()

    override fun blendFuncSeparate(srcFactorRGB: Int, dstFactorRGB: Int, srcFactorAlpha: Int, dstFactorAlpha: Int) =
        RenderSystem.blendFuncSeparate(srcFactorRGB, dstFactorRGB, srcFactorAlpha, dstFactorAlpha)
    override fun blendFunc(srcFactor: Int, dstFactor: Int) = RenderSystem.blendFunc(srcFactor, dstFactor)

    override fun rect(x: Float, y: Float, width: Float, height: Float, color: Int) {
        RenderSystem.setShader { GameRenderer.getPositionColorShader() }
        super.rect(x, y, width, height, color)
    }

    override fun modalRect(x: Float, y: Float, u: Float, v: Float, uWidth: Float, vHeight: Float, width: Float, height: Float, tileWidth: Float, tileHeight: Float) {
        RenderSystem.setShader { GameRenderer.getPositionColorShader() }
        super.modalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight)
    }

}