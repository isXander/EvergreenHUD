/*
 * EvergreenHUD - A mod to improve your heads-up-display.
 * Copyright (c) isXander [2019 - 2022].
 *
 * This work is licensed under the GPL-3 License.
 * To view a copy of this license, visit https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package dev.isxander.evergreenhud.utils

import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import java.util.function.BiConsumer

fun translate(x: Number = 0.0, y: Number = 0.0, z: Number = 0.0) =
    GlStateManager.translate(x.toDouble(), y.toDouble(), z.toDouble())

fun drawHorizontalLine(x1: Number, x2: Number, y: Number, width: Number, color: Int) {
    Gui.drawRect(x1.toInt(), y.toInt(), x2.toInt() + width.toInt(), y.toInt() + width.toInt(), color)
}

fun drawVerticalLine(x: Number, y1: Number, y2: Number, width: Number, color: Int) {
    Gui.drawRect(x.toInt(), y1.toInt() + width.toInt(), x.toInt() + width.toInt(), y2.toInt(), color)
}

fun drawBorderLines(x0: Float, y0: Float, x1: Float, y1: Float, width: Float, color: Int) {
    drawHorizontalLine(x0, x1, y0 - width / 2, width, color)
    drawVerticalLine(x1 - width / 2, y0, y1, width, color)
    drawHorizontalLine(x0, x1, y1 - width / 2, width, color)
    drawVerticalLine(x0 - width / 2, y0, y1, width, color)
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

/**
 * @param renderAction the action to render both the content and the outline, taking x and y positions as input
 */
fun drawWithOutline(x: Float, y: Float, renderAction: BiConsumer<Float, Float>) {
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
    renderAction.accept(x + 1, y)
    renderAction.accept(x - 1, y)
    renderAction.accept(x, y + 1)
    renderAction.accept(x, y - 1)
    GlStateManager.blendFunc(770, 771)
    renderAction.accept(x, y)
}

fun push(block: () -> Unit) {
    GlStateManager.pushMatrix()
    block()
    GlStateManager.popMatrix()
}
