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
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class ChildTexturedButtonWidget(
    val parent: Positionable<Float>,
    override var x: Float, override var y: Float,
    val width: Float, val height: Float,
    val u: Float, val v: Float,
    val texture: Identifier,
    val textureWidth: Float, val textureHeight: Float,
    val action: ChildTexturedButtonWidget.() -> Boolean
) : Element, Drawable, Selectable, Positionable<Float> {
    val renderX: Float
        get() = parent.x + x
    val renderY: Float
        get() = parent.y + y

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        RenderSystem.setShaderTexture(0, texture)

        matrices.fill(renderX, renderY, renderX + 10, renderY + 10, -1)
        matrices.drawTexture(renderX, renderY, u, v, width, height, textureWidth, textureHeight)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (isMouseOver(mouseX, mouseY)) {
            return action(this)
        }

        return false
    }

    override fun isMouseOver(mouseX: Double, mouseY: Double) =
        mouseX >= renderX && mouseY >= renderY && mouseX <= renderX + width && mouseY <= renderY + height

    override fun appendNarrations(builder: NarrationMessageBuilder) {}
    override fun getType(): Selectable.SelectionType = Selectable.SelectionType.HOVERED

}
