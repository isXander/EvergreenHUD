package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Exclude
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.hud.BasicHud
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import cc.polyfrost.oneconfig.utils.dsl.mc
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.entity.EntityLivingBase


class PlayerPreview: Config(Mod("Player Preview", ModType.HUD), "evergreenhud/playerpreview.json", false) {
    @HUD(
        name = "Self Preview"
    )
    var selfPreview = SelfPreviewHud()

    init {
        initialize()
    }

    class SelfPreviewHud: BasicHud(true, 1920 - 80f, 1080 - 120f) {

        @Slider(
            name = "Rotation",
            min = 0F,
            max = 360F,
        )
        var rotation = 0

        @Transient private var drawBackground = false
        override fun shouldDrawBackground() = drawBackground

        override fun draw(matrices: UMatrixStack?, x: Float, y: Float, scale: Float, example: Boolean) {
            if (drawBackground) return
            GlStateManager.pushMatrix()
            GlStateManager.enableDepth()
            drawBackground = true
            try {
                drawAll(matrices, example)
            } finally {
                drawBackground = false
            }
            if (mc.thePlayer == null) {
                GlStateManager.disableDepth()
                GlStateManager.popMatrix()
                return
            }

            GlStateManager.color(1f, 1f, 1f, 1f)
            renderLiving(mc.thePlayer, matrices, x, y, scale, rotation)
            RenderHelper.disableStandardItemLighting()
            GlStateManager.disableRescaleNormal()
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit)
            GlStateManager.disableTexture2D()
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit)
            GlStateManager.popMatrix()
        }

        override fun getWidth(scale: Float, example: Boolean): Float = 80 * scale

        override fun getHeight(scale: Float, example: Boolean): Float = 120 * scale
    }

    @Exclude
    companion object {
        private fun renderLiving(ent: EntityLivingBase, matrices: UMatrixStack?, x: Float, y: Float, scale: Float, rotation: Int) {
            GlStateManager.enableColorMaterial()
            GlStateManager.pushMatrix()
            GlStateManager.translate(x.toDouble() + (40 * scale), y.toDouble() + (107 * scale), 50.0)
            GlStateManager.scale(-(scale * 50), scale * 50, scale * 50)
            GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f)
            val f = ent.renderYawOffset
            val f1 = ent.rotationYaw
            val f2 = ent.rotationPitch
            val f3 = ent.prevRotationYawHead
            val f4 = ent.rotationYawHead
            GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f)
            RenderHelper.enableStandardItemLighting()
            GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f)
            val actualRotation = 360F - rotation
            ent.renderYawOffset = actualRotation
            ent.rotationYaw = actualRotation
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
        }
    }
}