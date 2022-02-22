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
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.*
import net.minecraft.client.render.VertexConsumerProvider.Immediate
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.screen.PlayerScreenHandler
import java.awt.Color
import kotlin.math.ceil
import kotlin.math.floor


/*
 * Taken from KronHUD under GNU-GPLv3.
 * Modified to work in Kotlin
 * https://github.com/DarkKronicle/KronHUD/blob/master/src/main/java/io/github/darkkronicle/kronhud/util/ItemUtil.java
 */

fun renderGuiItemModel(matrices: MatrixStack, stack: ItemStack?, x: Float, y: Float) {
    val model: BakedModel = mc.itemRenderer.getHeldItemModel(stack, null, mc.player, (x * y).toInt())
    mc.textureManager.getTexture(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).setFilter(false, false)
    RenderSystem.setShaderTexture(0, PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)
    RenderSystem.enableBlend()
    RenderSystem.blendFunc(GlStateManager.class_4535.SRC_ALPHA, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA)
    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
    matrices.push()
    matrices.translate(x.toDouble(), y.toDouble(), 100.0f + mc.itemRenderer.zOffset)
    matrices.translate(8.0, 8.0, 0.0)
    matrices.scale(1.0f, -1.0f, 1.0f)
    matrices.scale(16.0f, 16.0f, 16.0f)
    RenderSystem.applyModelViewMatrix()
    val immediate: Immediate = mc.bufferBuilders.entityVertexConsumers
    val bl = !model.isSideLit
    if (bl) {
        DiffuseLighting.disableGuiDepthLighting()
    }
    mc.itemRenderer.renderItem(
        stack, ModelTransformation.Mode.GUI, false, matrices, immediate, 15728880,
        OverlayTexture.DEFAULT_UV, model
    )
    immediate.draw()
    RenderSystem.enableDepthTest()
    if (bl) {
        DiffuseLighting.enableGuiDepthLighting()
    }
    matrices.pop()
    RenderSystem.applyModelViewMatrix()
}

fun renderGuiItemOverlay(
    matrices: MatrixStack, stack: ItemStack, x: Float, y: Float,
    countLabel: String?, showDurabilityBar: Boolean, textColor: Int, shadow: Boolean
) {
    if (stack.isEmpty) {
        return
    }
    if (stack.count != 1 || countLabel != null) {
        val string = countLabel ?: stack.count.toString()
        matrices.translate(0.0, 0.0, mc.itemRenderer.zOffset + 200.0f)
        drawString(matrices, string, x + 19 - 2 - mc.textRenderer.getWidth(string), y + 6 + 3, textColor, shadow = shadow)
    }
    if (stack.isItemBarVisible && showDurabilityBar) {
        RenderSystem.disableDepthTest()
        RenderSystem.disableTexture()
        RenderSystem.disableBlend()
        val i = stack.itemBarStep
        val j = stack.itemBarColor

        HitBox2D(x + 2, y + 13, 13f, 2f)
            .let { matrices.fill(it.x1, it.y1, it.x2, it.y2, 0) }

        val fill = HitBox2D(x + 2, y + 13, i.toFloat(), 1f)
        val color = Color(j shr 16 and 255, j shr 8 and 255, j and 255, 255).rgb
        matrices.fill(fill.x1, fill.y1, fill.x2, fill.y2, color)

        RenderSystem.enableBlend()
        RenderSystem.enableTexture()
        RenderSystem.enableDepthTest()
    }
    val clientPlayerEntity = mc.player
    val f = clientPlayerEntity?.itemCooldownManager?.getCooldownProgress(
        stack.item,
        mc.tickDelta
    )
        ?: 0.0f
    if (f > 0.0f) {
        RenderSystem.disableDepthTest()
        RenderSystem.disableTexture()
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()

        HitBox2D(x, y + floor(16f * (1f - f)), 16f, ceil(16f * f))
            .let { matrices.fill(it.x1, it.y1, it.x2, it.y2, Color(255, 255, 255, 127).rgb) }

        RenderSystem.enableTexture()
        RenderSystem.enableDepthTest()
    }
}
