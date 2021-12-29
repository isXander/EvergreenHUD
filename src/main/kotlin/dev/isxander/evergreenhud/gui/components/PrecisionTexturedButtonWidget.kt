/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2021].
 *
 * This work is licensed under the CC BY-NC-SA 4.0 License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0
 */

package dev.isxander.evergreenhud.gui.components

import com.mojang.blaze3d.systems.RenderSystem
import dev.isxander.evergreenhud.utils.drawTexture
import dev.isxander.evergreenhud.utils.fill
import dev.isxander.evergreenhud.utils.logger
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class PrecisionTexturedButtonWidget(
    val x: () -> Float, val y: () -> Float,
    val width: Float, val height: Float,
    val u: Float, val v: Float,
    val texture: Identifier,
    val textureWidth: Float, val textureHeight: Float,
    val tooltip: ButtonWidget.TooltipSupplier = ButtonWidget.TooltipSupplier { button, matrices, mouseX, mouseY ->  },
    val action: PrecisionTexturedButtonWidget.() -> Boolean
) : Element, Drawable, Selectable {
    constructor(
        x: Float, y: Float,
        width: Float, height: Float,
        u: Float, v: Float,
        texture: Identifier,
        textureWidth: Float, textureHeight: Float,
        tooltip: ButtonWidget.TooltipSupplier = ButtonWidget.TooltipSupplier { button, matrices, mouseX, mouseY ->  },
        action: PrecisionTexturedButtonWidget.() -> Boolean
    ) : this({ x }, { y }, width, height, u, v, texture, textureWidth, textureHeight, tooltip, action)

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        RenderSystem.setShaderTexture(0, texture)

        val x = this.x()
        val y = this.y()

        matrices.fill(x, y, x + 10, y + 10, -1)
        matrices.drawTexture(x, y, u, v, width, height, textureWidth, textureHeight)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        logger.info("clicked the mouse clikity claktiy")
        if (isMouseOver(mouseX, mouseY)) {
            return action(this)
        }

        return false
    }

    override fun isMouseOver(mouseX: Double, mouseY: Double): Boolean {
        val x = this.x()
        val y = this.y()

        return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height
    }

    override fun appendNarrations(builder: NarrationMessageBuilder) {}
    override fun getType(): Selectable.SelectionType = Selectable.SelectionType.HOVERED

}
