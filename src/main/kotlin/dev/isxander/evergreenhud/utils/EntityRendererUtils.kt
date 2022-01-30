/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.entity.EntityLivingBase

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
