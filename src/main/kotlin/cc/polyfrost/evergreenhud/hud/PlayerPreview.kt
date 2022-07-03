package cc.polyfrost.evergreenhud.hud

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Exclude
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.hud.Hud
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import cc.polyfrost.oneconfig.utils.dsl.mc
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.entity.EntityLivingBase
import kotlin.math.roundToInt


class PlayerPreview: Config(Mod("Player Preview", ModType.HUD), "evergreenhud/playerpreview.json") {
    @HUD(
        name = "Self Preview"
    )
    var selfPreview = SelfPreviewHud()

    init {
        initialize()
    }

    class SelfPreviewHud: Hud(false) {

        @Slider(
            name = "Rotation",
            min = 0F,
            max = 360F,
        )
        var rotation = 0

        @Transient private var drawBackground = false
        override fun drawBackground() = drawBackground

        override fun draw(matrices: UMatrixStack?, x: Int, y: Int, scale: Float) {
            if (drawBackground) return
            GlStateManager.pushMatrix()
            GlStateManager.enableDepth()
            drawBackground = true
            try {
                drawAll(matrices, x.toFloat(), y.toFloat(), scale, true)
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

        override fun getWidth(scale: Float) = (80 * scale).roundToInt()

        override fun getHeight(scale: Float) = (120 * scale).roundToInt()
    }

    @Exclude
    companion object {
        private fun renderLiving(ent: EntityLivingBase, matrices: UMatrixStack?, x: Int, y: Int, scale: Float, rotation: Int) {
            GlStateManager.enableColorMaterial()
            GlStateManager.pushMatrix()
            GlStateManager.translate(x.toDouble(), y.toDouble(), 50.0)
            GlStateManager.scale(-(scale * 50), scale * 50, scale * 50) //todo
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