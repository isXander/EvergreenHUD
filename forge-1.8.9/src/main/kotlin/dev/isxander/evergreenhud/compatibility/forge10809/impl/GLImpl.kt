package dev.isxander.evergreenhud.compatibility.forge10809.impl

import dev.isxander.evergreenhud.compatibility.universal.impl.render.AIGL11
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11

class GLImpl : AIGL11() {
    override fun color(r: Float, g: Float, b: Float, a: Float) = GlStateManager.color(r, g, b, a)

    override fun bindTexture(texture: Int) = GlStateManager.bindTexture(texture)

    override fun scissor(x: Int, y: Int, width: Int, height: Int) = GL11.glScissor(x, y, width, height)
    override fun enableScissor() = enable(GL_SCISSOR_TEST)
    override fun disableScissor() = disable(GL_SCISSOR_TEST)

    override fun lineWidth(width: Float) = GL11.glLineWidth(width)

    override fun enable(target: Int) = GL11.glEnable(target)
    override fun disable(target: Int) = GL11.glDisable(target)

    override fun begin(mode: Int) = GL11.glBegin(mode)
    override fun end() = GL11.glEnd()

    override fun enableBlend() = GlStateManager.enableBlend()
    override fun disableBlend() = GlStateManager.disableBlend()

    override fun enableTexture() = GlStateManager.enableTexture2D()
    override fun disableTexture() = GlStateManager.disableTexture2D()

    override fun enableAlpha() = enable(GL_ALPHA_TEST)
    override fun disableAlpha() = disable(GL_ALPHA_TEST)

    override fun enableDepth() = GlStateManager.enableDepth()
    override fun disableDepth() = GlStateManager.disableDepth()

    override fun blendFuncSeparate(srcFactorRGB: Int, dstFactorRGB: Int, srcFactorAlpha: Int, dstFactorAlpha: Int) =
        GlStateManager.tryBlendFuncSeparate(srcFactorRGB, dstFactorRGB, srcFactorAlpha, dstFactorAlpha)
    override fun blendFunc(srcFactor: Int, dstFactor: Int) = GlStateManager.blendFunc(srcFactor, dstFactor)

}