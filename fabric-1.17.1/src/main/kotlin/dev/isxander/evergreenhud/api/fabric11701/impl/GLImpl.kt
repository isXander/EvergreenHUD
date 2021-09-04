/*
 | EvergreenHUD - A mod to improve on your heads-up-display.
 | Copyright (C) isXander [2019 - 2021]
 |
 | This program comes with ABSOLUTELY NO WARRANTY
 | This is free software, and you are welcome to redistribute it
 | under the certain conditions that can be found here
 | https://www.gnu.org/licenses/lgpl-3.0.en.html
 |
 | If you have any questions or concerns, please create
 | an issue on the github page that can be found here
 | https://github.com/isXander/EvergreenHUD
 |
 | If you have a private concern, please contact
 | isXander @ business.isxander@gmail.com
 */

package dev.isxander.evergreenhud.api.fabric11701.impl

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import dev.isxander.evergreenhud.api.impl.render.UGL
import dev.isxander.evergreenhud.api.impl.render.UResourceLocation
import dev.isxander.evergreenhud.api.fabric11701.Main
import dev.isxander.evergreenhud.api.fabric11701.mc
import net.minecraft.client.render.GameRenderer
import net.minecraft.util.Identifier
import net.minecraft.util.math.Quaternion
import net.minecraft.util.math.Vec3f
import org.lwjgl.opengl.GL20

class GLImpl : UGL() {
    override fun push() = Main.matrices.push()
    override fun pop() = Main.matrices.pop()
    override fun scale(x: Float, y: Float, z: Float) = Main.matrices.scale(x, y, z)
    override fun translate(x: Float, y: Float, z: Float) = Main.matrices.translate(x.toDouble(), y.toDouble(), z.toDouble())
    override fun rotate(angle: Float, x: Float, y: Float, z: Float) = Main.matrices.multiply(Quaternion(Vec3f(x, y, z), angle, true))

    override fun color(r: Float, g: Float, b: Float, a: Float) = RenderSystem.clearColor(r, g, b, a)

    override fun bindTexture(texture: Int) = RenderSystem.bindTexture(texture)

    override fun scissor(x: Int, y: Int, width: Int, height: Int) = GlStateManager._scissorBox(x, y, width, height)
    override fun enableScissor() = GlStateManager._enableScissorTest()
    override fun disableScissor() = GlStateManager._disableScissorTest()

    override fun lineWidth(width: Float) = RenderSystem.lineWidth(width)

    override fun enable(target: Int) = GL20.glEnable(target)
    override fun disable(target: Int) = GL20.glDisable(target)

    override fun begin(mode: Int) = GL20.glBegin(mode)
    override fun end() = GL20.glEnd()

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

    override fun bindTexture(location: UResourceLocation) {
        mc.textureManager.bindTexture(Identifier(location.namespace, location.path))
    }

    override fun rect(x: Float, y: Float, width: Float, height: Float, color: Int) {
        RenderSystem.setShader { GameRenderer.getPositionColorShader() }
        super.rect(x, y, width, height, color)
    }

    override fun modalRect(x: Float, y: Float, u: Float, v: Float, uWidth: Float, vHeight: Float, width: Float, height: Float, tileWidth: Float, tileHeight: Float) {
        RenderSystem.setShader { GameRenderer.getPositionColorShader() }
        super.modalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight)
    }

    override fun partialCircle(x: Float, y: Float, radius: Float, startAngle: Int, endAngle: Int, color: Int) {
        RenderSystem.setShader { GameRenderer.getPositionColorShader() }
        super.partialCircle(x, y, radius, startAngle, endAngle, color)
    }

}